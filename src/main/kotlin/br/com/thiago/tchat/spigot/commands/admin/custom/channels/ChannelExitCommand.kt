package br.com.thiago.tchat.spigot.commands.admin.custom.channels

import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChannelExitCommand(private val customChannelController: CustomChannelController) {

    @Command(
        name = "channel.exit",
        target = CommandTarget.PLAYER,
        usage = "/channel exit (channel name)",
    )
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player

        if (context.argsCount() != 1) {
            player.sendMessage("§cPlease type: §7/channel exit (channel name)")
            return
        }

        val name = context.getArg(0)

        val customChannel = customChannelController.filterByName(name)

        if (customChannel == null) {
            player.sendMessage("§cChannel not found! Please check the channel name!")
            return
        }

        if (!customChannel.players.contains(player)) {
            player.sendMessage("§cYou are not on this channel!")
            return
        }

        customChannel.players.remove(player)
        customChannelController.save(customChannel)
        player.sendMessage("§cYou left the channel!")

    }

}