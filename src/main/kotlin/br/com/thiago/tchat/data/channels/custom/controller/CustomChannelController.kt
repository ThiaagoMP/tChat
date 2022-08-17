package br.com.thiago.tchat.data.channels.custom.controller

import br.com.thiago.tchat.configurations.CustomConfig
import br.com.thiago.tchat.dao.channel.custom.ChannelCustomDao
import br.com.thiago.tchat.data.channels.custom.entity.CustomChannel

class CustomChannelController(
    val customChannels: MutableList<CustomChannel> = emptyList<CustomChannel>().toMutableList(),
    private val customChannelDao: ChannelCustomDao,
    private val customConfig: CustomConfig
) {

    fun filterByName(name: String): CustomChannel? {
        return customChannels.firstOrNull { it.name == name }
    }

    fun save(customChannel: CustomChannel) {
        customChannelDao.save(customChannel, customConfig)
    }

    fun remove(customChannel: CustomChannel) {
        customChannelDao.remove(customChannel, customConfig)
    }

}