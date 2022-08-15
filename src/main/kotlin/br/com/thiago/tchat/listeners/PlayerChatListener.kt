package br.com.thiago.tchat.listeners

import br.com.thiago.tchat.commands.GlobalCommand
import br.com.thiago.tchat.commands.LocalCommand
import br.com.thiago.tchat.commands.ShoutCommand
import br.com.thiago.tchat.commands.WhisperCommand
import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.controller.ChannelController
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatListener(
    private val chatPlayerController: ChatPlayerController,
    private val channelController: ChannelController,
    private val config: FileConfiguration
) : Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        event.isCancelled = true
        val player = event.player
        val chatPlayer = chatPlayerController.players[player]
        val channelDefault = channelController.channels[chatPlayer!!.channelActive]

        when (channelDefault!!.channelType) {
            ChannelType.GLOBAL -> GlobalCommand(chatPlayerController, channelController).sendMessage(
                chatPlayer,
                channelDefault.channelType,
                player,
                event.message
            )
            ChannelType.LOCAL -> LocalCommand(chatPlayerController, channelController, config).sendMessage(
                chatPlayer,
                channelDefault.channelType,
                player,
                event.message
            )
            ChannelType.SHOUT -> ShoutCommand(chatPlayerController, channelController).sendMessage(
                chatPlayer,
                channelDefault.channelType,
                player,
                event.message
            )
            ChannelType.WHISPER -> WhisperCommand(chatPlayerController, channelController, config).sendMessage(
                chatPlayer,
                channelDefault.channelType,
                player,
                event.message
            )
            ChannelType.CUSTOM -> TODO()
        }
    }

}