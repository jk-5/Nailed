from tornado.web import RequestHandler

from nailed import env, DBSession
from nailed.models import Player

class IndexPageHandler(RequestHandler):
    def initialize(self):
        pass

    def get(self):
        template = env.get_template('index.html')

        game_state = 'start'
        leaderboard = []

        self.write(template.render(game_state=game_state, leaderboard=leaderboard))

    def on_finish(self):
        pass

class LeaderboardPageHandler(RequestHandler):
    def initialize(self):
        pass

    def get(self):
        template = env.get_template('leaderboard.html')

        self.write(template.render())

class LoginPageHandler(RequestHandler):
    def initialize(self):
        pass

    def get(self):
        template = env.get_template('login.html')

        self.write(template.render())

    def post(self):
        username = self.get_argument('username')
        password = self.get_argument('password')

class StartPageHandler(RequestHandler):
    def initialize(self):
        pass

    def get(self):
        template = env.get_template('start.html')

        self.write(template.render())