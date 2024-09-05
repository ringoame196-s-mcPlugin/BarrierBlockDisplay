package com.github.ringoame196_s_mcPlugin.managers

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class YmlFileManager {
    private fun acquisitionYml(file: File): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(file)
    }

    fun acquisitionListValue(file: File, key: String): MutableList<String>? {
        val ymlFile = acquisitionYml(file)
        val list = ymlFile.getList(key) ?: return null
        return list.filterIsInstance<String>().toMutableList()
    }

    fun setValue(file: File, key: String, value: Any?) {
        val ymlFile = acquisitionYml(file)
        ymlFile.set(key, value)
        ymlFile.save(file)
    }
}
