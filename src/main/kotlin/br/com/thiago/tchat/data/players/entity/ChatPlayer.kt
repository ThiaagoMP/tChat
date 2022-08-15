package br.com.thiago.tchat.data.players.entity

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.players.configuration.IChannelSystemPlayerConfiguration
import org.bukkit.entity.Player

data class ChatPlayer(
    val player: Player,
    var channelActive: ChannelType,
    val systemChannelsConfigurations: MutableMap<ChannelType, IChannelSystemPlayerConfiguration>
)