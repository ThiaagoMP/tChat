package br.com.thiago.tchat.spigot.listeners

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.players.configuration.IChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.configuration.impl.ChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val chatPlayerController: ChatPlayerController) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val configurations = emptyMap<ChannelType, IChannelSystemPlayerConfiguration>().toMutableMap()
        ChannelType.values().forEach {
            configurations[it] = ChannelSystemPlayerConfiguration(true)
            //pegar o chat ativo e as configurations do sql
        }
        chatPlayerController.players[player] = ChatPlayer(player, ChannelType.GLOBAL, configurations)
    }

}