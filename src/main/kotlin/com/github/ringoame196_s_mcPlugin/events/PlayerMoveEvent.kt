package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.managers.BBDisplayManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin

class PlayerMoveEvent(plugin: Plugin) : Listener {
    private val bbDisplayManager = BBDisplayManager(plugin)
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val player = e.player
        val playerLocation = player.location
        val from = e.from
        val to = e.to ?: return

        if (!bbDisplayManager.isExecutionPlayer(player)) { // プレイヤーがexecutionに含まれていない場合 実行しない
            return
        }

        if (from.x != to.x || from.y != to.y || from.z != to.z) { // 目線だけの移動は実行しない
            val barrierBlockLocations = bbDisplayManager.acquisitionBarrierBlockLocations(playerLocation, 3)
            bbDisplayManager.setDisplayBlock(barrierBlockLocations, player, Material.GLASS)
        }
    }
}
