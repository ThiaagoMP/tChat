package br.com.thiago.tchat.data.channels.system.controller

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.system.entity.ChannelDefault

class ChannelController(val channels: MutableMap<ChannelType, ChannelDefault>)