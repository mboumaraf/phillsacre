# This module contains the entities for the Duff Greek chapter exercises

from google.appengine.ext import db

CHAPTER_PARENT = 'chapter'

# The Google datastore only returns strongly consistent results
# when we use ancestor queries (i.e. querying within the same entity group)
# So ensure that we store all chapters with the same ancestor
def get_chapter_parent():
	return db.Key.from_path('Chapter', CHAPTER_PARENT)

def get_answer_parent():
	return db.Key.from_path('Answer', 'answer')

class DChapter(db.Model):
	name = db.StringProperty(required=True)

class DQuestion(db.Model):
	name = db.StringProperty(required=True)
	modelAnswer = db.StringProperty()

class DQuestionComment(db.Model):
	code = db.StringProperty(required=True)
	text = db.TextProperty(required=True)


class DAnswer(db.Model):
	student = db.UserProperty(required=True)
	chapter = db.ReferenceProperty(DChapter, required=True)
	submit_date = db.DateTimeProperty(required=True, auto_now_add=True)
	marked = db.BooleanProperty(default=False)
	
class DAnswerQuestion(db.Model):
	question = db.ReferenceProperty(DQuestion, required=True)
	answer = db.StringProperty()


class DMarkedAnswer(db.Model):
	answer = db.ReferenceProperty(DAnswer, required=True)
	comment = db.TextProperty()

class DMarkedQuestion(db.Model):
	question = db.ReferenceProperty(DAnswerQuestion, required=True)
	originalQuestion = db.ReferenceProperty(DQuestion, required=True)
	comment = db.StringProperty()

class DMarkedQuestionComment(db.Model):
	comment = db.ReferenceProperty(DQuestionComment, required=True)


class DStatistic(db.Model):
	chapter = db.ReferenceProperty(DChapter, required=True)
	question = db.ReferenceProperty(DQuestion, required=True)
	comment = db.ReferenceProperty(DQuestionComment, required=True)
	number = db.IntegerProperty(required=True)