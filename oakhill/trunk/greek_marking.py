# -*- coding: utf-8 -*-

from main import send_template
from main import JINJA_ENVIRONMENT
from duff_entities import *

from webapp2 import uri_for
from google.appengine.api import mail

import webapp2
import logging

class MainPage(webapp2.RequestHandler):
	def get(self):
		marking_list = DAnswer.all().filter('marked =', False).order('chapter')
		
		template_values = {
			'marking_list': marking_list
		}
		
		send_template(self.response, 'marking/index.html', template_values)

class Mark(webapp2.RequestHandler):
	def get(self, answer_id):
		answer = DAnswer.get_by_id(int(answer_id), parent=get_answer_parent())
		answers = DAnswerQuestion.all().ancestor(answer)
		
		# Ensure questions / answers are sorted by the question number
		# (not possible to do this using Google data store)
		answers = sorted(answers, key=lambda a: a.question.name)
		
		# Load the comments from the data store
		def load_comments(aq):
			aq.comments = DQuestionComment.all().ancestor(aq.question).order('code')
		
		map(load_comments, answers)
		
		template_values = {
			'answer': answer,
			'answers': answers
		}
		
		send_template(self.response, 'marking/mark.html', template_values)
	
	def post(self, answer_id):
		answer = DAnswer.get_by_id(int(answer_id), parent=get_answer_parent())
		answers = DAnswerQuestion.all().ancestor(answer)
		
		xg_options = db.create_transaction_options(xg=True)
		markedAnswer = db.run_in_transaction_options(xg_options, self.mark_answer, answer, answers)
		
		self.send_email(markedAnswer)
		self.update_statistics(markedAnswer)
		
		self.redirect(uri_for('marking-main'))
	
	def send_email(self, markedAnswer):
		email = markedAnswer.answer.student.email()
		
		markedQuestions = DMarkedQuestion.all().ancestor(markedAnswer)
		markedQuestions = sorted(markedQuestions, key=lambda a: a.question.question.name)
		
		# Load any individual comments as Google does not do this automatically
		def load_comments(mq):
			mq.comments = DMarkedQuestionComment.all().ancestor(mq)
		
		map(load_comments, markedQuestions)
		
		values = {
			'markedAnswer': markedAnswer,
			'markedQuestions': markedQuestions
		}
		
		template = JINJA_ENVIRONMENT.get_template('marking/marked_email.html')
		html_msg = template.render(values)
		
		if mail.is_email_valid(email):
			message = mail.EmailMessage(
				to = email,
				sender = 'phillip.sacre@gmail.com',
				subject = 'Exercise Feedback',
				html = unicode(html_msg)
			)
			message.send()
	
	def update_statistics(self, markedAnswer):
		chapter = markedAnswer.answer.chapter
		questions = DQuestion.all().ancestor(chapter)
		
		for question in questions:
			comments = DQuestionComment.all().ancestor(question)
			mq = DMarkedQuestion.all().ancestor(markedAnswer).filter('originalQuestion =', question)
			
			for comment in comments:
				mqc = DMarkedQuestionComment.all().filter('comment =', comment).ancestor(mq.get())
				
				if mqc.count() > 0:
					statistic = None
					query = DStatistic.all()
					query.filter('chapter =', chapter)
					query.filter('question =', question)
					query.filter('comment =', comment)
					
					if query.count() > 0:
						logging.debug('Creating statistic for (%s, %s, %s)' % (chapter.name, question.name, comment.code))
						statistic = query.get()
					else:
						logging.debug('Updating statistic for (%s, %s, %s)' % (chapter.name, question.name, comment.code))
						statistic = DStatistic(chapter=chapter, question=question, comment=comment, number=0)
					
					statistic.number = statistic.number + 1
					statistic.put()
	
	def mark_answer(self, answer, answers):
		markedAnswer = DMarkedAnswer(answer=answer)
		markedAnswer.comment = self.request.get('general_comments')
		markedAnswer.put()
		
		for aq in answers:
			comment = self.request.get('marker_comment_%d' % aq.key().id())
			
			markedQuestion = DMarkedQuestion(question=aq, originalQuestion=aq.question, parent=markedAnswer)
			markedQuestion.comment = comment
						
			markedQuestion.put()
			
			comment_ids = self.request.get_all('comment_%d' % aq.key().id())
			comment_ids = map(int, comment_ids)
			comments = DQuestionComment.get_by_id(comment_ids, aq.question)
			
			for c in comments:
				mqc = DMarkedQuestionComment(comment=c, parent=markedQuestion)
				mqc.put()
		
		answer.marked = True
		answer.put()
		
		return markedAnswer