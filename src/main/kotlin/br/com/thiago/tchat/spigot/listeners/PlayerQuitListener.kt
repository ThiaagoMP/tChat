package br.com.thiago.tchat.spigot.listeners

import br.com.thiago.tchat.dao.players.PlayersDao
import br.com.thiago.tchat.data.players.controller.ChatPlayerController
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val playerController: ChatPlayerController, private val playersDao: PlayersDao) :
    Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        playersDao.updateAccount(playerController.players[event.player]!!)
    }

}