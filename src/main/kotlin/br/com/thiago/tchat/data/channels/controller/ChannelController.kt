package br.com.thiago.tchat.data.channels.controller

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.channels.system.ChannelDefault

class ChannelController(val channels: MutableMap<ChannelType, ChannelDefault>)