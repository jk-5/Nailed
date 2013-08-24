from tornado.web import RequestHandler

from nailed import env, Session
from nailed.models import Player

class IndexPageHandler(RequestHandler):
    def initialize(self):
        self.session = Session()

    def get(self):
        template = env.get_template('index.html')

        game_state = 'start'
        leaderboard = []

        for player in self.session.query(Player).order_by(Player.wins).limit(10):
            leaderboard.append(player)

        self.write(template.render(game_state=game_state, leaderboard=leaderboard))

    def on_finish(self):
        self.session.close()
