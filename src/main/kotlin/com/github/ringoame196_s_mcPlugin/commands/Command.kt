package com.github.ringoame196_s_mcPlugin.commands

import com.github.ringoame196_s_mcPlugin.managers.BBDisplayManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Command(plugin: Plugin) : CommandExecutor {
    private val bbDisplayManager = BBDisplayManager(plugin)
    private val config = plugin.config
    private val defaultRadius = config.getInt(CommandConst.RADIUS_KEY)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            return false
        }

        val subCommand = args[0]
        when (subCommand) {
            CommandConst.HIDE_COMMAND -> hideCommand(sender, args)
            CommandConst.DISPLAY_COMMAND -> displayCommand(sender, args)
            CommandConst.DISPLAY_MODE_COMMAND -> alwaysDisplayCommand(sender)
            else -> {
                val message = "${ChatColor.RED}構文が間違っています"
                sender.sendMessage(message)
            }
        }

        return true
    }

    private fun displayCommand(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) { // プレイヤーのみ実行可能
            sendAllowedOnlyPlayer(sender)
            return
        }

        val radius = acquisitionRadius(args) // 半径取得

        if (!inRadiusRange(radius, sender)) { // max越えないように
            return
        }

        bbDisplayManager.display(sender, radius) // 表示させる

        // 通知
        val message = "${ChatColor.AQUA}半径${radius}ブロックのバリアブロックを表示しました"
        sender.sendMessage(message)
    }

    private fun hideCommand(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) { // プレイヤーのみ実行可能
            sendAllowedOnlyPlayer(sender)
            return
        }

        val radius = acquisitionRadius(args) // 半径取得

        if (!inRadiusRange(radius, sender)) { // max越えないように
            return
        }

        bbDisplayManager.hide(sender, radius) // 隠す

        // 通知
        val message = "${ChatColor.GOLD}半径${radius}ブロックのバリアブロックを通常表示に戻しました"
        sender.sendMessage(message)
    }

    private fun alwaysDisplayCommand(sender: CommandSender) {
        if (sender !is Player) { // プレイヤーのみ実行可能
            sendAllowedOnlyPlayer(sender)
            return
        }
        bbDisplayManager.changeExecutionPlayer(sender)
    }

    private fun acquisitionRadius(args: Array<out String>): Int {
        return if (args.size > 1) { // 範囲設定
            try {
                args[1].toInt()
            } catch (e: NumberFormatException) {
                defaultRadius // int型に変換失敗した場合はデフォルト値に
            }
        } else {
            defaultRadius // 指定がなければ デフォルト値に
        }
    }

    private fun sendAllowedOnlyPlayer(sender: CommandSender) {
        val message = "${ChatColor.RED}このコマンドはプレイヤー専用です"
        sender.sendMessage(message)
    }

    private fun inRadiusRange(radius: Int, player: Player): Boolean {
        // 半径の制限の処理
        val max = config.getInt(CommandConst.MAX_RADIUS_KEY)

        return if (max < radius) {
            val message = "${ChatColor.RED}${max}以上の半径を設定することは不可能です"
            player.sendMessage(message)
            false
        } else {
            true
        }
    }
}
