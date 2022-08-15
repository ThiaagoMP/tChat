package br.com.thiago.tchat.configurations

import br.com.thiago.tchat.TChat
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

interface CustomConfig {

    fun setup(plugin: TChat): CustomConfig

    fun getConfigName(): String?

    fun getConfig(): FileConfiguration?

    fun getFile(): File?

    fun save()


}