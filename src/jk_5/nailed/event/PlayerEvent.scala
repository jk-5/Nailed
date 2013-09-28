package jk_5.nailed.event

import jk_5.nailed.event.NailedEvent
import jk_5.nailed.players.Player

/**
 * No description given
 *
 * @author jk-5
 */
class PlayerEvent(var player: Player) extends NailedEvent
class PlayerChatEvent(private var player: Player, var message: String) extends PlayerEvent(player)
class PlayerJoinServerEvent(private var player: Player) extends PlayerEvent(player)
class PlayerLeaveServerEvent(private var player: Player) extends PlayerEvent(player)
