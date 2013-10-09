package jk_5.nailed.event

import jk_5.nailed.players.Player

/**
 * No description given
 *
 * @author jk-5
 */
class PlayerEvent(var player: Player) extends NailedEvent
class PlayerChatEvent(player: Player, var message: String) extends PlayerEvent(player)
class PlayerJoinServerEvent(player: Player) extends PlayerEvent(player)
class PlayerLeaveServerEvent(player: Player) extends PlayerEvent(player)
