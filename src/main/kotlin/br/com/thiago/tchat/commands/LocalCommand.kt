package br.com.thiago.tchat.commands

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.controller.ChannelController
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class LocalCommand(
    private val chatPlayerController: ChatPlayerController,
    private val channelController: ChannelController,
    private val config: FileConfiguration
) {

    @Command(name = "local", target = CommandTarget.PLAYER)
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player
        val chatPlayer = chatPlayerController.players[player]!!
        val channelType = ChannelType.LOCAL

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

        val distance = config.getInt("Local.DistanceInBlocks")
        val players = emptyList<ChatPlayer>().toMutableList()

        player.getNearbyEntities(distance.toDouble(), distance.toDouble(), distance.toDouble()).forEach {
            if (it.type == EntityType.PLAYER && chatPlayerController.players.containsKey(it) &&
                chatPlayerController.players[it]!!.systemChannelsConfigurations[channelType]!!.activated
            )
                players.add(chatPlayerController.players[it as Player]!!)
        }

        channelController.channels[channelType]!!.sendMessage(player, "&7[&aL&7] $message", players)
    }
}
