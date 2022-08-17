package br.com.thiago.tchat.data.channels.system.entity

import br.com.thiago.tchat.TChat
import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ChannelDefault(val channelType: ChannelType, var activated: Boolean) {

    fun sendMessage(player: Player, message: String, players: List<ChatPlayer>) {
        if (!activated && !player.hasPermission("channel.disabled.bypass")) {
            player.sendMessage("§cChat disabled!")
            return
        }

        val split = message.split(" ")
        val forbiddenWords = TChat.instance!!.config.getStringList("ForbiddenWords")
        split.forEach {
            if (forbiddenWords.contains(it)) {
                player.sendMessage("§cPlease do not use the word '§7$it§c'")
                return
            }
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