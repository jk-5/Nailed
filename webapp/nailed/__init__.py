# Load the config file
from nailed.config import Config
config = Config()

# Set up SQLAlchemy
from sqlalchemy import create_engine
engine = create_engine(config.database, echo=False)

from sqlalchemy.orm import sessionmaker
DBSession = sessionmaker(bind=engine)

from nailed.models import Base
Base.metadata.create_all(engine)

# Jinja setup
from jinja2 import Environment, PackageLoader
env = Environment(loader=PackageLoader('nailed', 'templates'))

# Get directory path for static files.
# On production, these files should be handled by the reverse proxy.
from os.path import basename
static_path = '/'.join(__file__.split('/')[:-1]) + '/static'

# Passlib setup
from passlib.context import CryptContext

crypt_context = CryptContext(
    schemes=["pbkdf2_sha256", "des_crypt" ],
    default="pbkdf2_sha256",
    all__vary_rounds = 0.1,
    pbkdf2_sha256__default_rounds = 8000,
)

# Controllers
from nailed.controllers import UserController
user_controller = UserController(crypt_context, DBSession)

# Initialize the application
from tornado.web import Application, StaticFileHandler
from nailed.ipc import MinecraftSocketHandler
from nailed.views import IndexPageHandler, LeaderboardPageHandler, LoginPageHandler, StartPageHandler

app = Application([
    ('/', IndexPageHandler),
    ('/leaderboard', LeaderboardPageHandler),
    ('/login', LoginPageHandler),
    ('/start', StartPageHandler),
    
    ('/minecraft', MinecraftSocketHandler),
    ('/static/(.*)', StaticFileHandler, dict(path=static_path)),
])