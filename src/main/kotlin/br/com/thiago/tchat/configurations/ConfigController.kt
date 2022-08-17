package br.com.thiago.tchat.configurations

import br.com.thiago.tchat.TChat
import br.com.thiago.tchat.configurations.impl.ChannelsConfig
import br.com.thiago.tchat.configurations.impl.CustomChannelsConfig

class ConfigController(val configs: MutableMap<Class<*>, CustomConfig> = emptyMap<Class<*>, CustomConfig>().toMutableMap()) {

    fun load(plugin: TChat) {
        plugin.saveDefaultConfig()
        configs[ChannelsConfig::class.java] = ChannelsConfig().setup(plugin)
        configs[CustomChannelsConfig::class.java] = CustomChannelsConfig().setup(plugin)
    }

}