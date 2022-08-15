package br.com.thiago.tchat.data.channels.system

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ChannelDefault(val channelType: ChannelType, var activated: Boolean, var timeToActivate: Long) {

    fun sendMessage(player: Player, message: String, players: List<ChatPlayer>) {
        if (!activated) {
            if (timeToActivate == 0L || timeToActivate > System.currentTimeMillis()) {
                player.sendMessage("§cChat disabled!")
                return
            } else activated = true
        }

        val messageReplaced = ChatColor.translateAlternateColorCodes('&', "${player.displayName} &a>> &7$message")
        player.sendMessage(messageReplaced)
        players.forEach {
            if (it.systemChannelsConfigurations[channelType]!!.activated) {
                it.player.sendMessage(
                    messageReplaced
                )
            }
        }
    }


}