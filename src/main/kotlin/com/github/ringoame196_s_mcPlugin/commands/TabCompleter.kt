package com.github.ringoame196_s_mcPlugin.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter : TabCompleter {
    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        val size = args.size
        return when (size) {
            1 -> mutableListOf(CommandConst.HIDE_COMMAND, CommandConst.DISPLAY_COMMAND, CommandConst.DISPLAY_MODE_COMMAND)
            2 -> when (args[0]) {
                CommandConst.HIDE_COMMAND, CommandConst.DISPLAY_COMMAND -> mutableListOf("[半径]")
                else -> mutableListOf()
            }
            else -> mutableListOf()
        }
    }
}
