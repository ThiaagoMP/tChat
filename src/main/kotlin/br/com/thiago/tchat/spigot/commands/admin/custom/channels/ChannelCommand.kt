package br.com.thiago.tchat.spigot.commands.admin.custom.channels

import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChannelCommand(private val customChannelController: CustomChannelController) {

    @Command(
        name = "channel",
        target = CommandTarget.PLAYER,
        usage = "/channel (channel name) (message)",
    )
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player

        if (context.argsCount() < 2) {
            player.sendMessage("§cPlease type: §7/channel (channel name) (message)")
            return
        }

        val name = context.getArg(0)
        val message = StringUtils.join(context.getArgs(1, context.argsCount()), " ").replace('&', '§')

        val customChannel = customChannelController.filterByName(name)

        if (customChannel == null) {
            player.sendMessage("§cChannel not found! Please check the channel name!")
            return
        }

        if (!customChannel.players.contains(player) && !customChannel.isPublic) {
            player.sendMessage("§cYou are not on this channel!")
            return
        }

        if (!customChannel.activated) {
            player.sendMessage("§cChat is currently disabled!")
            return
        }

        if (!customChannel.players.contains(player)) {
            customChannel.players.add(player)
            customChannelController.save(customChannel)
        }

        customChannel.players.forEach {
            if (it.isOnline)
                it.sendMessage("§7[§a$name§7] >> $message")
        }
    }

}