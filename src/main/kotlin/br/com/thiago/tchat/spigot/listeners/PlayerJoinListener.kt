package br.com.thiago.tchat.spigot.listeners

import br.com.thiago.tchat.dao.players.PlayersDao
import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.players.configuration.IChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.configuration.impl.ChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val chatPlayerController: ChatPlayerController, val playersDao: PlayersDao) :
    Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (playersDao.hasAccount(player.name)) {
            val chatPlayer = playersDao.findChatPlayer(event.player.name)
            chatPlayerController.players[player] = chatPlayer
        } else {
            val configurations = emptyMap<ChannelType, IChannelSystemPlayerConfiguration>().toMutableMap()
            ChannelType.values().forEach {
                configurations[it] = ChannelSystemPlayerConfiguration(true)
            }
            val chatPlayer = ChatPlayer(player, ChannelType.GLOBAL, configurations)
            playersDao.addAccount(chatPlayer)
            chatPlayerController.players[player] = chatPlayer
        }
    }

}