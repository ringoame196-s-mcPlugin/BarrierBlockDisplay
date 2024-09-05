package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.commands.TabCompleter
import com.github.ringoame196_s_mcPlugin.events.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        val plugin = this
        saveDefaultConfig() // configファイル生成
        saveResource("data.yml", false) // dataファイルを生成
        server.pluginManager.registerEvents(PlayerMoveEvent(plugin), plugin)
        // コマンド
        val command = getCommand("bbdisplay")
        command!!.setExecutor(Command(plugin))
        command.tabCompleter = TabCompleter()
    }
}
