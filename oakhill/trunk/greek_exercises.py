import webapp2

from google.appengine.ext import db
from google.appengine.api import users

from main import send_template
from duff_entities import *

class MainPage(webapp2.RequestHandler):
	def get(self):
		chapters = DChapter.all().order('name')
		completed = DAnswer.all().ancestor(get_answer_parent()).filter('student =', users.get_current_user()).order('-submit_date')
		
		# Remove any completed chapters from the list of chapters to be completed
		# The Google Data Store makes this hard (if not impossible) to do in a query
		completed_chapters = set([answer.chapter.key().id() for answer in completed])
		chapters = [chapter for chapter in chapters if chapter.key().id() not in completed_chapters]
		
		msg = self.request.get('msg')
		
		template_values = {
			'chapters': chapters,
			'completed': completed,
			'msg': msg
		}
		
		send_template(self.response, 'exercises/index.html', template_values)

class Submit(webapp2.RequestHandler):
	def get(self, chapter_id):
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		questions = DQuestion.all().ancestor(chapter).order('name')
		
		template_values = {
			'chapter': chapter,
			'questions': questions
		}
		
		send_template(self.response, 'exercises/submit.html', template_values)
	
	def post(self, chapter_id):
		chapter = DChapter.get_by_id(int(chapter_id), get_chapter_parent())
		questions = DQuestion.all().ancestor(chapter).order('name')
		
		xg_options = db.create_transaction_options(xg=True)
		db.run_in_transaction_options(xg_options, self.create_answer, chapter, questions)
		
		self.redirect(webapp2.uri_for('exercises-main', msg='Your exercise has been submitted!'))
		
	def create_answer(self, chapter, questions):
		user = users.get_current_user()
		
		answer = DAnswer(student=user, chapter=chapter, parent=get_answer_parent())
		answer.put()
		
		for question in questions:
			answer_text = self.request.get('answer_%d' % question.key().id())
			answer_question = DAnswerQuestion(question=question, answer=answer_text, parent=answer)
			answer_question.put()