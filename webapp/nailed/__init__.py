# Load the config file
from nailed.config import Config
config = Config()

# Set up SQLAlchemy
from sqlalchemy import create_engine
engine = create_engine(config.database, echo=False)

from sqlalchemy.orm import sessionmaker
Session = sessionmaker(bind=engine)

from nailed.models import Base
Base.metadata.create_all(engine)

# Jinja setup
from jinja2 import Environment, PackageLoader
env = Environment(loader=PackageLoader('nailed', 'templates'))

# Get directory path for static files.
# On production, these files should be handled by the reverse proxy.
from os.path import basename
static_path = '/'.join(__file__.split('/')[:-1]) + '/static'

# Initialize the application
from tornado.web import Application, StaticFileHandler
from nailed.views import IndexPageHandler

app = Application([
    ('/', IndexPageHandler),
    ('/static/(.*)', StaticFileHandler, dict(path=static_path)),
])

# Initialize the Minecraft socket
from nailed.ipc import MinecraftSocket
ipc = MinecraftSocket()
