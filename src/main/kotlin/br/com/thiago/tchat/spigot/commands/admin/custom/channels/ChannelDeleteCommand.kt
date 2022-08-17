package br.com.thiago.tchat.spigot.commands.admin.custom.channels

import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChannelDeleteCommand(private val customChannelController: CustomChannelController) {

    @Command(
        name = "channel.delete",
        target = CommandTarget.PLAYER,
        usage = "/channel delete (name)",
        permission = "channel.delete.use"
    )
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player

        if (context.argsCount() != 1) {
            player.sendMessage("§cPlease type: §7/channel delete (name)")
            return
        }

        val name = context.getArg(0)

        val customChannel = customChannelController.filterByName(name)

        if (customChannel == null) {
            player.sendMessage("§cChannel not found! Please check the channel name!")
            return
        }

        customChannelController.customChannels.remove(customChannel)
        customChannelController.remove(customChannel)
        player.sendMessage("§aChannel deleted!")
    }

}