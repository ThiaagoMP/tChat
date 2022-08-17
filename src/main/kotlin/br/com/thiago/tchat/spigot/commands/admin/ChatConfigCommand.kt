package br.com.thiago.tchat.spigot.commands.admin

import br.com.thiago.tchat.data.channels.system.controller.ChannelController
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.spigot.inventories.ChannelsInventory
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.command.Context
import me.saiintbrisson.minecraft.command.target.CommandTarget
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChatConfigCommand(
    private val chatPlayerController: ChatPlayerController,
    private val channelController: ChannelController
) {

    @Command(name = "chat.config", target = CommandTarget.PLAYER, permission = "chat.config.use")
    fun handleCommand(context: Context<CommandSender>) {
        val player = context.sender as Player
        val chatPlayer = chatPlayerController.players[player]!!

        val channelsInventory = ChannelsInventory(chatPlayer, channelController).init<SimpleInventory>()
        channelsInventory.openInventory(player) { viewer: Viewer ->
            val propertyMap = viewer.propertyMap
            propertyMap["admin"] = true
        }
    }

}