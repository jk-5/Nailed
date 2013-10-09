package jk_5.nailed.irc.lib

class IrcException(msg: String) extends Exception(msg)
class NickAlreadyInUseException(msg: String) extends Exception(msg)