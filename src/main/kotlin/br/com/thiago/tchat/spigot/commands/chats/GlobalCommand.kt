package br.com.thiago.tchat.spigot.commands.chats

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.system.controller.ChannelController
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GlobalCommand(
    private val chatPlayerController: ChatPlayerController, private val channelController: ChannelController
) {

    @Command(name = "g", target = CommandTarget.PLAYER)
    fun handleCommand(context: Context<CommandSender>) {

        val player = context.sender as Player
        val channelType = ChannelType.GLOBAL
        val chatPlayer = chatPlayerController.players[player]!!

        if (context.argsCount() == 0) {
            chatPlayer.channelActive = channelType
            player.sendMessage("§aChat stuck on ${channelType.key}")
            return
        }

        sendMessage(chatPlayer, channelType, player, StringUtils.join(context.args, " "))
    }

    fun sendMessage(
        chatPlayer: ChatPlayer,
        channelType: ChannelType,
        player: Player,
        message: String
    ) {
        if (!chatPlayer.systemChannelsConfigurations[channelType]!!.activated) chatPlayer.systemChannelsConfigurations[channelType]!!.activated =
            true

        val players =
            chatPlayerController.players.filter { it.key != player && it.value.systemChannelsConfigurations[channelType]!!.activated }.values.toMutableList()

        channelController.channels[channelType]!!.sendMessage(player, "&7[&aG&7] $message", players)
    }

}