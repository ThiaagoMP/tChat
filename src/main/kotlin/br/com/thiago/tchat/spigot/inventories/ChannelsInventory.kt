package br.com.thiago.tchat.spigot.inventories

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.system.controller.ChannelController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import br.com.thiago.tchat.spigot.utils.ItemBuilder
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer
import org.bukkit.DyeColor
import org.bukkit.Material

class ChannelsInventory(private val chatPlayer: ChatPlayer, private val channelController: ChannelController) :
    SimpleInventory("toggle.inventory", "§8Channels", 3 * 9) {

    override fun configureInventory(viewer: Viewer, editor: InventoryEditor) {
        var counter = 10

        val isAdmin = viewer.propertyMap.get<Boolean>("admin")
        val player = viewer.player
        ChannelType.values().forEach {
            if (it == ChannelType.CUSTOM) return@forEach
            if (isAdmin) {
                val channel = channelController.channels[it]!!
                val activated = channel.activated

                val itemBuilder =
                    ItemBuilder(Material.INK_SACK).setDisplayName("§7Click to §a${if (activated) "disable" else "activate"} §7channel §a${it.key} §7to all players!")
                        .setColor(if (activated) DyeColor.LIME else DyeColor.GRAY)
                val item = InventoryItem.of(itemBuilder)

                editor.setItem(counter, item.defaultCallback { _ ->
                    channel.activated = !activated
                    player.closeInventory()
                    player.sendMessage("§aChannel §7${it.key} ${if (activated) "disabled" else "activated"}§a for all players!")
                })
            } else {
                val configuration = chatPlayer.systemChannelsConfigurations[it]!!
                val activated = configuration.activated
                editor.setItem(counter, InventoryItem.of(
                    ItemBuilder(Material.INK_SACK).setDisplayName("§7Click to §a${if (activated) "disabled" else "activated"} §7channel §a${it.key}§7!")
                        .setColor(if (activated) DyeColor.LIME else DyeColor.GRAY)
                ).defaultCallback { _ ->
                    configuration.activated = !activated
                    player.closeInventory()
                    player.sendMessage("§aChannel ${it.key} §7${if (activated) "disabled" else "activated"}§a!")
                })
            }
            counter += 2
        }
    }
}