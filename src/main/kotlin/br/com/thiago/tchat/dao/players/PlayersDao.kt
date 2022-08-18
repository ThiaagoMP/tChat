package br.com.thiago.tchat.dao.players

import br.com.thiago.tchat.data.channels.ChannelType
import br.com.thiago.tchat.data.players.configuration.IChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.configuration.impl.ChannelSystemPlayerConfiguration
import br.com.thiago.tchat.data.players.entity.ChatPlayer
import org.bukkit.Bukkit
import java.sql.Connection
import java.util.*

private const val TABLE_NAME = "tchat_accounts"
private const val COLUMN_PLAYER_NAME = "player_name"
private const val COLUMN_CHANNEL_ACTIVE = "channel_active"
private const val COLUMN_GLOBAL = "global"
private const val COLUMN_LOCAL = "local"
private const val COLUMN_WHISPER = "whisper"
private const val COLUMN_SHOUT = "shout"

class PlayersDao(private val connection: Connection) {

    fun createTable() {
        val sql =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME($COLUMN_PLAYER_NAME varchar(20), $COLUMN_CHANNEL_ACTIVE varchar(20)," +
                    " $COLUMN_GLOBAL bit, $COLUMN_LOCAL bit, $COLUMN_WHISPER bit, $COLUMN_SHOUT bit);"
        val statement = connection.prepareStatement(sql)
        statement.execute()
        statement.close()
    }

    fun addAccount(chatPlayer: ChatPlayer) {
        val sql =
            "INSERT INTO $TABLE_NAME($COLUMN_PLAYER_NAME, $COLUMN_CHANNEL_ACTIVE, $COLUMN_GLOBAL, $COLUMN_LOCAL,$COLUMN_WHISPER,$COLUMN_SHOUT)" +
                    " VALUES (?,?,?,?,?,?);"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, chatPlayer.player.name)
        statement.setString(2, chatPlayer.channelActive.key)

        var counter = 3
        ChannelType.values().forEach {
            if (it == ChannelType.CUSTOM) return@forEach
            statement.setBoolean(counter, chatPlayer.systemChannelsConfigurations[it]!!.activated)
            counter++
        }

        statement.execute()
        statement.close()
    }

    fun remove(playerName: String) {
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMN_PLAYER_NAME = ?;"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, playerName)
        statement.execute()
        statement.close()
    }

    fun updateAccount(chatPlayer: ChatPlayer) {
        val sql =
            "UPDATE $TABLE_NAME SET $COLUMN_CHANNEL_ACTIVE = ?, $COLUMN_GLOBAL = ?,$COLUMN_LOCAL = ?,$COLUMN_WHISPER = ?,$COLUMN_SHOUT = ?" +
                    " WHERE $COLUMN_PLAYER_NAME = ?;"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, chatPlayer.channelActive.key)

        var counter = 1
        ChannelType.values().forEach {
            counter++
            if (it == ChannelType.CUSTOM) return@forEach
            statement.setBoolean(counter, chatPlayer.systemChannelsConfigurations[it]!!.activated)
        }

        statement.setString(6, chatPlayer.player.name)
        statement.execute()
        statement.close()
    }

    fun hasAccount(playerName: String): Boolean {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_PLAYER_NAME = ?;"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, playerName)
        val query = statement.executeQuery()
        val has = query.next()
        statement.close()
        query.close()
        return has
    }

    fun findChatPlayer(playerName: String): ChatPlayer {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_PLAYER_NAME = ?;"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, playerName)

        val map = emptyMap<ChannelType, IChannelSystemPlayerConfiguration>().toMutableMap()

        val query = statement.executeQuery()
        var chatPlayer: ChatPlayer? = null

        while (query.next()) {
            ChannelType.values().forEach {
                if (it != ChannelType.CUSTOM)
                    map[it] = ChannelSystemPlayerConfiguration(query.getBoolean(it.key))
            }
            chatPlayer = ChatPlayer(
                Bukkit.getPlayerExact(query.getString(COLUMN_PLAYER_NAME)),
                ChannelType.valueOf(query.getString(COLUMN_CHANNEL_ACTIVE).uppercase(Locale.getDefault())),
                map
            )
        }

        query.close()
        statement.close()
        return chatPlayer!!
    }

}