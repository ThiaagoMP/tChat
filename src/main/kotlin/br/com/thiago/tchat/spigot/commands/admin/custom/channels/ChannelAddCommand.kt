package br.com.thiago.tchat.spigot.commands.admin.custom.channels

import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChannelAddCommand(private val customChannelController: CustomChannelController) {

    @Command(
        name = "channel.add",
        target = CommandTarget.PLAYER,
        usage = "/channel add (player name) (channel name)",
        permission = "channel.add.use"
    )
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player

        if (context.argsCount() != 2) {
            player.sendMessage("§cPlease type: §7/channel add (player name) (channel name)")
            return
        }
        val playerName = context.getArg(0)
        val playerExact = Bukkit.getPlayerExact(playerName)
        if (playerExact == null || !playerExact.isOnline) {
            player.sendMessage("§cPlayer offline!")
            return
        }

        val name = context.getArg(1)

        val customChannel = customChannelController.filterByName(name)

        if (customChannel == null) {
            player.sendMessage("§cChannel not found! Please check the channel name!")
            return
        }

        customChannel.players.add(playerExact)
        customChannelController.save(customChannel)
        player.sendMessage("§aPlayer added!")
        playerExact.sendMessage("§aYou have been added to channel ${customChannel.name}, try /channel ${customChannel.name} (message)")
    }

}