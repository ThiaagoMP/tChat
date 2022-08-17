package br.com.thiago.tchat

import br.com.thiago.tchat.configurations.ConfigController
import br.com.thiago.tchat.configurations.impl.ChannelsConfig
import br.com.thiago.tchat.configurations.impl.CustomChannelsConfig
import br.com.thiago.tchat.dao.channel.custom.ChannelCustomDao
import br.com.thiago.tchat.dao.channel.system.ChannelDao
import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import br.com.thiago.tchat.data.channels.system.controller.ChannelController
import br.com.thiago.tchat.data.players.configuration.IChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.configuration.impl.ChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class TChat : JavaPlugin() {

    companion object {
        var instance: TChat? = null
            private set
    }

    var chatPlayerController: ChatPlayerController = ChatPlayerController()
    var configController: ConfigController = ConfigController()
    var customChannelController: CustomChannelController? = null
    var channelController: ChannelController? = null
    val channelCustomDao = ChannelCustomDao()

    override fun onEnable() {

        instance = this
        configController.load(this)
        val customChannelsConfig = configController.configs[CustomChannelsConfig::class.java]!!

        channelController =
            ChannelController(ChannelDao().load(configController.configs[ChannelsConfig::class.java]!!.getConfig()!!))
        customChannelController =
            CustomChannelController(
                channelCustomDao.load(customChannelsConfig.getConfig()!!),
                channelCustomDao,
                customChannelsConfig
            )

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
        SpigotLoader.registerListeners(this)
    }

    private fun registerCommands() {
        SpigotLoader.registerCommands(this)
    }
}