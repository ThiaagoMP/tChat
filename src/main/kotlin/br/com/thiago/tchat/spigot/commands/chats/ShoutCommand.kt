package br.com.thiago.tchat.spigot.commands.chats

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.system.controller.ChannelController
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ShoutCommand(
    private val chatPlayerController: ChatPlayerController, private val channelController: ChannelController
) {

    @Command(name = "shout", target = CommandTarget.PLAYER)
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player
        val chatPlayer = chatPlayerController.players[player]!!
        val channelType = ChannelType.SHOUT

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

        val players = emptyList<ChatPlayer>().toMutableList()
        Bukkit.getOnlinePlayers().forEach {
            if (it != player && it.world == player.world && chatPlayerController.players.containsKey(it)
                && chatPlayerController.players[it]!!.systemChannelsConfigurations[channelType]!!.activated
            )
                players.add(chatPlayerController.players[it]!!)
        }

        channelController.channels[channelType]!!.sendMessage(player, "&7[&aS&7] $message", players)
    }
}