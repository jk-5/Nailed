from sqlalchemy import Column, Integer, String

from sqlalchemy.ext.declarative import declarative_base
Base = declarative_base()

class Player(Base):
    __tablename__ = 'players'

    id = Column(Integer, primary_key=True)
    name = Column(String)
    total = Column(Integer)
    wins = Column(Integer)
    kills = Column(Integer)

    def __init__(self, name, total=0, wins=0, kills=0, op=False):
        self.name = name
        self.total = total
        self.wins = wins
        self.kills = kills

    def __repr__(self):
        return '<Player ("%s","%s","%s","%s")>' % (self.name, self.total, self.wins, self.kills)

class Game(Base):
    __tablename__ = 'game'

    player_id = Column(Integer, primary_key=True)
    team = Column(Integer)

    def __init__(self, player_id, team):
        self.player_id = player_id
        self.team = team

    def __repr__(self):
        return '<Game ("%s","%s")>' % (self.player_id, self.team)
