package com.github.ringoame196_s_mcPlugin.managers

import com.github.ringoame196_s_mcPlugin.commands.CommandConst
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.BoundingBox
import java.io.File

class BBDisplayManager(plugin: Plugin) {
    private val parquetManager = ParquetManager()
    private val ymlFileManager = YmlFileManager()
    private val dataFile = File(plugin.dataFolder, "data.yml")
    private val executionPlayersKey = "execution_players"
    private val config = plugin.config

    fun acquisitionBarrierBlockLocations(location: Location, radius: Int): MutableList<Location> {
        val world = location.world
        val locations: MutableList<Location> = mutableListOf()
        // 半径の座標を取得
        val boundingBox = BoundingBox(
            location.x - radius,
            location.y - radius,
            location.z - radius,
            location.x + radius,
            location.y + radius,
            location.z + radius
        )

        // BoundingBox内のブロックを取得
        for (x in boundingBox.minX.toInt()..boundingBox.maxX.toInt()) {
            for (y in boundingBox.minY.toInt()..boundingBox.maxY.toInt()) {
                for (z in boundingBox.minZ.toInt()..boundingBox.maxZ.toInt()) {
                    val block = world?.getBlockAt(x, y, z) ?: continue
                    if (block.type == Material.BARRIER) {
                        locations.add(block.location)
                    }
                }
            }
        }
        return locations
    }

    fun setDisplayBlock(locations: MutableList<Location>, player: Player, material: Material) {
        for (location in locations) { // バリアブロックの場所にディスプレイブロックを設置する
            parquetManager.setBlock(material, location, player)
        }
    }

    fun isExecutionPlayer(player: Player): Boolean {
        val uuid = player.uniqueId.toString() // プレイヤーのUUID
        val executionPlayersList = acquisitionExecutionPlayers() // executionPlayerList
        return executionPlayersList.contains(uuid) // 含まれているか
    }

    fun changeExecutionPlayer(player: Player) {
        val message = "${ChatColor.GOLD}表示モードを切り替えました"
        player.sendMessage(message) // メッセージ
        if (isExecutionPlayer(player)) { // on -> off
            val radius = config.getInt(CommandConst.RADIUS_KEY)
            deletionExecutionPlayer(player) // 削除する
            hide(player, radius) // デフォルト値の半径を隠す
        } else { // off -> on
            additionExecutionPlayer(player) // 追加する
        }
    }

    private fun additionExecutionPlayer(player: Player) { // 追加
        val uuid = player.uniqueId.toString()
        val executionPlayersList = acquisitionExecutionPlayers() ?: return
        executionPlayersList.add(uuid)
        ymlFileManager.setValue(dataFile, executionPlayersKey, executionPlayersList)
    }

    private fun deletionExecutionPlayer(player: Player) { // 削除
        val uuid = player.uniqueId.toString()
        val executionPlayersList = acquisitionExecutionPlayers() ?: return
        executionPlayersList.remove(uuid)
        ymlFileManager.setValue(dataFile, executionPlayersKey, executionPlayersList)
    }

    private fun acquisitionExecutionPlayers(): MutableList<String> {
        return ymlFileManager.acquisitionListValue(dataFile, executionPlayersKey) ?: mutableListOf()
    }

    private fun resetDisplayBlock(locations: MutableList<Location>, player: Player) {
        for (location in locations) {
            val blockType = location.block.type
            parquetManager.setBlock(blockType, location, player)
        }
    }

    fun display(player: Player, radius: Int) {
        val displayBlockType = Material.GLASS
        val location = player.location // プレイヤー座標
        // 座標を取得
        val locations = acquisitionBarrierBlockLocations(location, radius)
        // 表示させる
        setDisplayBlock(locations, player, displayBlockType)
    }

    fun hide(player: Player, radius: Int) {
        val location = player.location // プレイヤー座標
        // 座標を取得
        val locations = acquisitionBarrierBlockLocations(location, radius)
        // 戻す
        resetDisplayBlock(locations, player)
    }
}
