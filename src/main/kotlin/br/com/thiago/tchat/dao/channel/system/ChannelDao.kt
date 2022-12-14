package br.com.thiago.tchat.dao.channel.system

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.system.entity.ChannelDefault
import org.bukkit.configuration.file.FileConfiguration

class ChannelDao {

    fun load(config: FileConfiguration): MutableMap<ChannelType, ChannelDefault> {
        val map = emptyMap<ChannelType, ChannelDefault>().toMutableMap()
        config.getKeys(false).forEach {
            val section = config.getConfigurationSection(it)
            val channelType = ChannelType.valueOf(it)
            map[channelType] =
                ChannelDefault(channelType, section.getBoolean("activated"))
        }
        return map
    }

}