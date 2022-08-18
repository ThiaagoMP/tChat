package br.com.thiago.tchat

import br.com.thiago.tchat.spigot.commands.ToggleCommand
import br.com.thiago.tchat.spigot.commands.admin.ChatConfigCommand
import br.com.thiago.tchat.spigot.commands.admin.custom.channels.*
import br.com.thiago.tchat.spigot.commands.chats.*
import br.com.thiago.tchat.spigot.listeners.PlayerChatListener
import br.com.thiago.tchat.spigot.listeners.PlayerJoinListener
import br.com.thiago.tchat.spigot.listeners.PlayerQuitListener
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager
import me.saiintbrisson.bukkit.command.BukkitFrame
import org.bukkit.Bukkit

class SpigotLoader {

    companion object {
        fun registerListeners(tChat: TChat) {
            InventoryManager.enable(tChat)
            Bukkit.getPluginManager()
                .registerEvents(
                    PlayerChatListener(tChat.chatPlayerController, tChat.channelController!!, tChat.config),
                    tChat
                )
            Bukkit.getPluginManager()
                .registerEvents(PlayerJoinListener(tChat.chatPlayerController, tChat.playersDao!!), tChat)
            Bukkit.getPluginManager()
                .registerEvents(PlayerQuitListener(tChat.chatPlayerController, tChat.playersDao!!), tChat)
        }

        fun registerCommands(tChat: TChat) {
            val bukkitFrame = BukkitFrame(tChat)
            bukkitFrame.registerCommands(
                GlobalCommand(tChat.chatPlayerController, tChat.channelController!!),
                LocalCommand(tChat.chatPlayerController, tChat.channelController!!, tChat.config),
                ShoutCommand(tChat.chatPlayerController, tChat.channelController!!),
                WhisperCommand(tChat.chatPlayerController, tChat.channelController!!, tChat.config),
                ToggleCommand(tChat.chatPlayerController, tChat.channelController!!),
                ChatConfigCommand(tChat.chatPlayerController, tChat.channelController!!),
                ChannelAddCommand(tChat.customChannelController!!),
                ChannelCommand(tChat.customChannelController!!),
                ChannelCreateCommand(tChat.customChannelController!!),
                ChannelExitCommand(tChat.customChannelController!!),
                ChannelToggleCommand(tChat.customChannelController!!),
                ChannelDeleteCommand(tChat.customChannelController!!)
            )
        }
    }

}