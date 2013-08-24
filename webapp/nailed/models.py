from sqlalchemy import Column, ForeignKey, Boolean, Integer, String

from sqlalchemy.ext.declarative import declarative_base
Base = declarative_base()

class Player(Base):
    __tablename__ = 'players'

    id = Column(Integer, primary_key=True)
    name = Column(String(16))
    total = Column(Integer)
    wins = Column(Integer)
    kills = Column(Integer)
    op = Column(Boolean)

    def __init__(self, name, total=0, wins=0, kills=0, op=False):
        self.name = name
        self.total = total
        self.wins = wins
        self.kills = kills
        self.op = op

    def __repr__(self):
        return '<Player ("%s","%s","%s","%s","%s")>' % (self.name, self.total, self.wins, self.kills, self.op)

class Game(Base):
    __tablename__ = 'game'

    player_id = Column(Integer, ForeignKey('players.id'), primary_key=True)
    team = Column(Integer)
    captain = Column(Boolean)

    def __init__(self, player_id, team, captain=False):
        self.player_id = player_id
        self.team = team
        self.captain = captain

    def __repr__(self):
        return '<Game ("%s","%s","%s")>' % (self.player_id, self.team, self.captain)
