package br.com.thiago.tchat.data.channels.custom.entity

import br.com.thiago.tchat.data.channels.ChannelType
import org.bukkit.OfflinePlayer

data class CustomChannel(
    val name: String,
    val channelType: ChannelType,
    var activated: Boolean,
    val isPublic: Boolean,
    val players: MutableList<OfflinePlayer>
)
