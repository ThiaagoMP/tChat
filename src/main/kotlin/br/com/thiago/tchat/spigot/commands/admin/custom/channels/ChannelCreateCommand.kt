package br.com.thiago.tchat.spigot.commands.admin.custom.channels

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import br.com.thiago.tchat.data.channels.custom.entity.CustomChannel
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChannelCreateCommand(private val customChannelController: CustomChannelController) {

    private val blackListNames = listOf("create", "add", "exit", "toggle", "delete")

    @Command(
        name = "channel.create",
        target = CommandTarget.PLAYER,
        usage = "/channel create (public or private) (name)",
        permission = "channel.create.use"
    )
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player

        if (context.argsCount() != 2) {
            player.sendMessage("§cPlease type: §7/channel create (public or private) (name)")
            return
        }
        val isChannelPublic = when (context.getArg(0)) {
            "public" -> true
            "private" -> false
            else -> {
                player.sendMessage("§cPlease type '§7public§c' or '§7private§c'")
                return
            }
        }

        val name = context.getArg(1)

        if (blackListNames.contains(name)) {
            player.sendMessage("§cPlease use another name on the channel!")
            return
        }


        if (customChannelController.filterByName(name) != null) {
            player.sendMessage("§cThis channel already exists! Please use another name!")
            return
        }

        val customChannel =
            CustomChannel(
                name,
                ChannelType.CUSTOM,
                true,
                isChannelPublic,
                listOf(player).toMutableList()
            )
        customChannelController.customChannels.add(customChannel)
        customChannelController.save(customChannel)
        player.sendMessage("§aChannel created!")
    }

}