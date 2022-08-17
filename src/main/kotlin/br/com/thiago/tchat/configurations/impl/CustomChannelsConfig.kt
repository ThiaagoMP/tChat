package br.com.thiago.tchat.configurations.impl

import br.com.thiago.tchat.TChat
import br.com.thiago.tchat.configurations.CustomConfig
import br.com.thiago.tchat.configurations.setup.ConfigSetup
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

class CustomChannelsConfig : CustomConfig {

    private var file: File? = null
    private var fileConfig: FileConfiguration? = null

    override fun setup(plugin: TChat): CustomConfig {
        file = ConfigSetup.setupFile(plugin, getConfigName())
        fileConfig = ConfigSetup.setupFileConfiguration(file!!, getConfigName())
        return this
    }

    override fun getConfigName(): String {
        return "customchannels.yml"
    }

    override fun getConfig(): FileConfiguration? {
        return fileConfig
    }

    override fun getFile(): File? {
        return file
    }

    override fun save() {
        try {
            fileConfig?.save(file!!)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Bukkit.getConsoleSender()
                .sendMessage("§7An error occurred while saving a configuration: " + getConfigName())
        }
    }

}