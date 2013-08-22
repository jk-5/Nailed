from flask import Flask
app = Flask(__name__)

import nail.views

# From here on, database stuff.
#
#     ___-----___
#    |           |
#    |---_____---|
#    |___     ___|
#    |   -----   |
#    |---_____---|
#    |___     ___|
#        -----

from sqlalchemy import create_engine
engine = create_engine('sqlite:///:memory:', echo=True)

from nail.models import Base
Base.metadata.create_all(engine) 
