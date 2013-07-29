import os
import urllib
import jinja2
import webapp2

from webapp2_extras import routes
from webapp2 import uri_for
from google.appengine.ext import db

from duff_entities import *
from main import send_template

class MainPage(webapp2.RequestHandler):
    def get(self):
		chapters = DChapter.all()
		chapters.ancestor(get_chapter_parent())
		chapters.order('name')
		
		template_values = {
			'chapters': chapters
		}
		
		send_template(self.response, 'admin/index.html', template_values)

class CreateChapter(webapp2.RequestHandler):
	def post(self):
		name = self.request.get('name')
		
		# Check there are no chapters with that name already
		chapter = DChapter.all()
		chapter.ancestor(get_chapter_parent())
		chapter.filter("name=", name)
		
		if chapter.count() > 0:
			query_params = { 'error': 'A chapter with that name exists already!' }
			self.redirect(uri_for('edit-chapter') + '?' + urllib.urlencode(query_params))
		else:
			chapter = DChapter(name=name, parent=get_chapter_parent())
			
			chapter.put()
			
			query_params = { 'id': chapter.key().id() }
			self.redirect(uri_for('edit-chapter') + '?' + urllib.urlencode(query_params))

class EditChapter(webapp2.RequestHandler):
	def get(self):
		id = self.request.get('id')
		
		chapter = DChapter.get_by_id(int(id), get_chapter_parent())
		questions = DQuestion.all().ancestor(chapter).order('name')
		
		template_values = {
			'chapter': chapter,
			'questions': questions
		}
		
		send_template(self.response, 'admin/edit_chapter.html', template_values)
		
	def post(self):
		id = self.request.get('id')
		name = self.request.get('name')
		
		chapter = DChapter.get_by_id(int(id), get_chapter_parent())
		chapter.name = name
		chapter.put()
		
		query_params = { 'id': id }
		self.redirect(uri_for('edit-chapter') + '?' + urllib.urlencode(query_params))

class DeleteChapter(webapp2.RequestHandler):
	def get(self):
		id = self.request.get('id')
		
		deleted = db.run_in_transaction(self.delete, id)
		
		if (deleted):
			self.redirect(uri_for('greek-admin-main'))
		else:
			query_params = { 'error': 'Could not find chapter with that ID!' }
			self.redirect(uri_for('greek-admin-main') + '?' + urllib.urlencode(query_params))
	
	def delete(self, id):
		chapter = DChapter.get_by_id(int(id), get_chapter_parent())
		
		if chapter:
			questions = DQuestion.all().ancestor(chapter)
			chapter.delete()
			
			db.delete(questions)
			
		return chapter

class AddQuestion(webapp2.RequestHandler):
	def post(self):
		chapter_id = self.request.get('chapter_id')
		name = self.request.get('name')
		model_answer = self.request.get('model_answer')
		
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		
		question = DQuestion(name=name, modelAnswer=model_answer, parent=chapter)
		question.put()
		
		query_params = { 'id': chapter_id }
		self.redirect(uri_for('edit-chapter') + '?' + urllib.urlencode(query_params))

class DeleteQuestion(webapp2.RequestHandler):
	def get(self):
		chapter_id = self.request.get('chapter_id')
		question_id = self.request.get('id')
		
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		
		db.run_in_transaction(self.delete, chapter, question_id)
		
		query_params = { 'id': chapter.key().id() }
		self.redirect(uri_for('edit-chapter') + '?' + urllib.urlencode(query_params))
	
	def delete(self, chapter, question_id):
		question = DQuestion.get_by_id(int(question_id), chapter)
		comments = DQuestionComment.all().ancestor(question)
		
		question.delete()
		db.delete(comments)

class EditComments(webapp2.RequestHandler):
	def get(self):
		chapter_id = self.request.get('chapter_id')
		question_id = self.request.get('question_id')
		
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		question = DQuestion.get_by_id(int(question_id), chapter)
		
		comments = DQuestionComment.all().ancestor(question).order('code')
		
		template_values = {
			'chapter': chapter,
			'question': question,
			'comments': comments
		}
		
		send_template(self.response, 'admin/edit_comments.html', template_values)
	
	def post(self):
		chapter_id = self.request.get('chapter_id')
		question_id = self.request.get('question_id')
		comment_id = self.request.get('comment_id')
		
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		question = DQuestion.get_by_id(int(question_id), chapter)
		comment = DQuestionComment.get_by_id(int(comment_id), question)
		
		comment.text = self.request.get('text')
		comment.code = self.request.get('code')
		comment.put()
		
		query_params = { 'chapter_id': chapter_id, 'question_id': question_id }
		self.redirect(uri_for('edit-comments') + '?' + urllib.urlencode(query_params))


class AddComment(webapp2.RequestHandler):
	def post(self):
		chapter_id = self.request.get('chapter_id')
		question_id = self.request.get('question_id')
		
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		question = DQuestion.get_by_id(int(question_id), chapter)
		
		code = self.request.get('code')
		text = self.request.get('text')
		
		comment = DQuestionComment(code=code, text=text, parent=question)
		comment.put()
		
		query_params = { 'chapter_id': chapter_id, 'question_id': question_id }
		self.redirect(uri_for('edit-comments') + '?' + urllib.urlencode(query_params))
	
class DeleteComment(webapp2.RequestHandler):
	def get(self, chapter_id, question_id, comment_id):
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		question = DQuestion.get_by_id(int(question_id), chapter)
		comment = DQuestionComment.get_by_id(int(comment_id), question)
		
		comment.delete()
		
		self.redirect(uri_for('edit-comments', question_id=question_id, chapter_id=chapter_id))

class Statistics(webapp2.RequestHandler):
	def get(self):
		stats = DStatistic.all()
		stats = sorted(stats, key=lambda x: (x.chapter.name, x.question.name, x.comment.code))
		
		template_values = {
			'stats': stats
		}
		
		send_template(self.response, 'admin/statistics.html', template_values)

application = webapp2.WSGIApplication([
	routes.PathPrefixRoute('/admin', [
		webapp2.Route('/', MainPage, 'greek-admin-main'),
		webapp2.Route('/create-chapter', CreateChapter, 'create-chapter'),
		webapp2.Route('/edit-chapter', EditChapter, 'edit-chapter'),
		webapp2.Route('/delete-chapter', DeleteChapter, 'delete-chapter'),
		webapp2.Route('/add-question', AddQuestion, 'add-question'),
		webapp2.Route('/delete-question', DeleteQuestion, 'delete-question'),
		webapp2.Route('/edit-comments', EditComments, 'edit-comments'),
		webapp2.Route('/add-comment', AddComment, 'add-comment'),
		webapp2.Route('/delete-comment/<chapter_id>/<question_id>/<comment_id>', DeleteComment, 'delete-comment'),
		webapp2.Route('/statistics', Statistics, 'statistics')
	])
], debug=True)