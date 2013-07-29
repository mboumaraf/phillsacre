import os
import urllib
import jinja2
import webapp2

from webapp2_extras import routes
from google.appengine.ext import db
from google.appengine.api import users

# Set Jinja to look for templates in /templates
JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.join(os.path.dirname(__file__), 'templates')),
    extensions=['jinja2.ext.autoescape'])

def send_template(response, template_name, values=[]):
	template = JINJA_ENVIRONMENT.get_template(template_name)
	response.write(template.render(values))

def datetimeformat(value, format='%d-%b-%Y %H:%M'):
	return value.strftime(format)

class MainPage(webapp2.RequestHandler):
    def get(self):
		admin = users.is_current_user_admin()
		
		template_values = {
			'admin': admin,
			'user': users.get_current_user(),
			'logout_url': users.create_logout_url('/')
		}
		send_template(self.response, 'index.html', template_values)

JINJA_ENVIRONMENT.filters['datetimeformat'] = datetimeformat

application = webapp2.WSGIApplication([
	('/', MainPage),
	routes.PathPrefixRoute('/exercises', [
		webapp2.Route('/', 'greek_exercises.MainPage', 'exercises-main'),
		webapp2.Route('/submit/<chapter_id>', 'greek_exercises.Submit', 'exercises-submit')
	]),
	routes.PathPrefixRoute('/marking', [
		webapp2.Route('/', 'greek_marking.MainPage', 'marking-main'),
		webapp2.Route('/mark/<answer_id>', 'greek_marking.Mark', 'marking-mark')
	])
], debug=True)