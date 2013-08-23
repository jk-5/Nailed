from tornado.tcpserver import TCPServer

class MinecraftSocket(TCPServer):
    def on_chunk(self, data):
        print "Got some data."

        # Check magic number of the incoming data
        if not (data[0] == chr(0x55) and data[1] == chr(0xaa)):
            print "Invalid data."
            return

    def on_closed(self, data):
        print "Closed."

    def handle_stream(self, stream, address):
        stream.read_until_close(self.on_closed, self.on_chunk)
