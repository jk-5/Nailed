#!/usr/bin/env python

from tornado.web import Application
from tornado.ioloop import IOLoop

from nailed import app, ipc

if __name__ == '__main__':
    app.listen(5000)
    ipc.listen(5001)
    
    IOLoop.instance().start()
