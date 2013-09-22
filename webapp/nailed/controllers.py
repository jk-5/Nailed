from nailed.models import Player

class UserController(object):
    def __init__(self, crypt_context, db_session):
        self.crypt_context = crypt_context
        self.db_session = db_session

    def create_user(self, username, password, op):
        hash = self.crypt_context.encrypt(password)

        player = Player(username, hash)


    def get_best_players(self, count=10):
        s = self.db_session()
        players = s.query(Player)
        
        return players