package br.com.thiago.tchat

import br.com.thiago.tchat.commands.GlobalCommand
import br.com.thiago.tchat.commands.LocalCommand
import br.com.thiago.tchat.commands.ShoutCommand
import br.com.thiago.tchat.commands.WhisperCommand
import br.com.thiago.tchat.configurations.ConfigController
import br.com.thiago.tchat.configurations.impl.ChannelsConfig
import br.com.thiago.tchat.dao.channel.ChannelDao
import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.controller.ChannelController
import br.com.thiago.tchat.data.players.configuration.IChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.configuration.impl.ChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import br.com.thiago.tchat.listeners.PlayerChatListener
import br.com.thiago.tchat.listeners.PlayerJoinListener
import me.saiintbrisson.bukkit.command.BukkitFrame
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class TChat : JavaPlugin() {

    companion object {
        var instance: TChat? = null
            private set
    }

    var chatPlayerController: ChatPlayerController = ChatPlayerController()
    var configController: ConfigController = ConfigController()
    var channelController: ChannelController? = null


    override fun onEnable() {
        instance = this
        configController.load(this)
        channelController =
            ChannelController(ChannelDao().load(configController.configs[ChannelsConfig::class.java]!!.getConfig()!!))

        registerCommands()
        registerListeners()
        loadPlayersInReload()
    }

    private fun loadPlayersInReload() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val configurations = emptyMap<ChannelType, IChannelSystemPlayerConfiguration>().toMutableMap()
            ChannelType.values().forEach {
                configurations[it] = ChannelSystemPlayerConfiguration(true)
                //pegar o chat ativo e as configurations do sql
            }
            chatPlayerController.players[player] = ChatPlayer(player, ChannelType.GLOBAL, configurations)
        }
    }

    private fun registerListeners() {
        Bukkit.getPluginManager()
            .registerEvents(PlayerChatListener(chatPlayerController, channelController!!, this.config), this)
        Bukkit.getPluginManager().registerEvents(PlayerJoinListener(chatPlayerController), this)
    }

    private fun registerCommands() {
        val bukkitFrame = BukkitFrame(this)
        bukkitFrame.registerCommands(
            GlobalCommand(chatPlayerController, channelController!!),
            LocalCommand(chatPlayerController, channelController!!, this.config),
            ShoutCommand(chatPlayerController, channelController!!),
            WhisperCommand(chatPlayerController, channelController!!, this.config)
        )
    }
}