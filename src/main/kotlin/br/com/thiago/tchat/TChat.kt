package br.com.thiago.tchat

import br.com.thiago.tchat.configurations.ConfigController
import br.com.thiago.tchat.configurations.impl.ChannelsConfig
import br.com.thiago.tchat.configurations.impl.CustomChannelsConfig
import br.com.thiago.tchat.dao.channel.custom.ChannelCustomDao
import br.com.thiago.tchat.dao.channel.system.ChannelDao
import br.com.thiago.tchat.dao.players.PlayersDao
import br.com.thiago.tchat.dao.players.factory.ConnectionFactory
import br.com.thiago.tchat.data.channels.custom.controller.CustomChannelController
import br.com.thiago.tchat.data.channels.system.controller.ChannelController
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
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
    var playersDao: PlayersDao? = null

    override fun onEnable() {

        instance = this
        configController.load(this)
        loadSQL()

        val customChannelsConfig = configController.configs[CustomChannelsConfig::class.java]!!

        channelController =
            ChannelController(ChannelDao().load(configController.configs[ChannelsConfig::class.java]!!.getConfig()!!))
        customChannelController = CustomChannelController(
            channelCustomDao.load(customChannelsConfig.getConfig()!!), channelCustomDao, customChannelsConfig
        )

        playersDao!!.createTable()
        registerCommands()
        registerListeners()
        loadPlayersInReload()
    }

    override fun onDisable() {
        Bukkit.getOnlinePlayers().forEach {
            playersDao?.updateAccount(chatPlayerController.players[it]!!) ?: return
        }
    }

    private fun loadSQL() {
        val section = this.config.getConfigurationSection("Database")
        playersDao = PlayersDao(
            ConnectionFactory.getConnection(
                section.getString("password"),
                section.getString("user"),
                section.getString("host"),
                section.getString("port"),
                section.getString("database")
            )!!
        )
    }

    private fun loadPlayersInReload() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val chatPlayer = playersDao!!.findChatPlayer(player.name)
            chatPlayerController.players[player] = chatPlayer
        }
    }

    private fun registerListeners() {
        SpigotLoader.registerListeners(this)
    }

    private fun registerCommands() {
        SpigotLoader.registerCommands(this)
    }
}