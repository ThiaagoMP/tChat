package br.com.thiago.tchat.dao.channel.custom

import br.com.thiago.tchat.configurations.CustomConfig
import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.custom.entity.CustomChannel
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class ChannelCustomDao {

    fun load(config: FileConfiguration): MutableList<CustomChannel> {
        val customChannels = emptyList<CustomChannel>().toMutableList()
        config.getKeys(false).forEach {
            val section = config.getConfigurationSection(it)
            val activated = section.getBoolean("activated")
            val isPublic = section.getBoolean("isPublic")
            val players = emptyList<Player>().toMutableList()
            section.getStringList("players").forEach { player ->
                players.add(Bukkit.getPlayerExact(player))
            }
            customChannels.add(CustomChannel(it, ChannelType.CUSTOM, activated, isPublic, players))
        }
        return customChannels
    }

    fun save(customChannel: CustomChannel, customConfig: CustomConfig) {
        val config = customConfig.getConfig()
        val section = config!!.createSection(customChannel.name)

        section.set("name", customChannel.name)
        section.set("activated", customChannel.activated)
        section.set("isPublic", customChannel.isPublic)

        val players = emptyList<String>().toMutableList()
        customChannel.players.forEach { players.add(it.name) }
        section.set("players", players)

        customConfig.save()
    }

    fun remove(customChannel: CustomChannel, customConfig: CustomConfig) {
        val config = customConfig.getConfig()
        config!!.set(customChannel.name, null)

        customConfig.save()
    }

}