package br.com.thiago.tchat.data.players.controller

import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.entity.Player

data class ChatPlayerController(val players: MutableMap<Player, ChatPlayer> = emptyMap<Player, ChatPlayer>().toMutableMap())