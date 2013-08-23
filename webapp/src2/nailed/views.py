from tornado.web import RequestHandler

from nailed import env

class IndexPageHandler(RequestHandler):
    def get(self):
        template = env.get_template('index.html')
        self.write(template.render())
