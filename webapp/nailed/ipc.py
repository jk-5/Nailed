from json import loads
from tornado.websocket import WebSocketHandler

class MinecraftSocketHandler(WebSocketHandler):
    _packet_handlers = {}

    def open(self):
        # This should be redone.
        self._packet_handlers = {
            1:  self.on_connected_received,
            21: self.on_player_joined,
            22: self.on_player_left,
        }

        print "Socket was opened"

    def on_connected_received(self, data):
        print "Minecraft server has started: ", data

    def on_player_joined(self, data):
        print "Player has joined: ", data

    def on_player_left(self, data):
        print "Player has left: ", data

    def on_message(self, message):
        data = loads(message)

        try:
            self._packet_handlers[data['id']](data['data'])
        except Exception as e:
            print e
            print "Received invalid packet: ", data

    def on_close(self):
        print "Socket was closed."

    