package com.bestclic.leaderboards.controller.service.model

import com.bestclic.leaderboards.controller.service.Player

class PlayerNode(var value: Player) {

    var height = 0

    var leftNode: PlayerNode? = null

    var rightNode: PlayerNode? = null

    val leftNodeHeight: Int
        get() = leftNode?.height() ?: -1

    val rightNodeHeight: Int
        get() = rightNode?.height() ?: -1

    val min: PlayerNode
        get() = leftNode?.min ?: this

    val balanceFactor: Int
        get() = leftNodeHeight - rightNodeHeight


    private fun height(node: PlayerNode? = this): Int {
        return node?.let {
            1 + Integer.max(
                height(node.leftNode),
                height(node.rightNode)
            )
        } ?: -1
    }

    private fun traverseInOrder(visit: Visitor<Player>) {
        rightNode?.traverseInOrder(visit)
        visit(value)
        leftNode?.traverseInOrder(visit)
    }

    fun traverseInOrderDesc(visit: Visitor<Player>) {
        var rank = 0
        val ranker: Visitor<Player> = { player ->
            rank ++
            player?.rank = rank }
        val rankAndVisit : Visitor<Player> = { player: Player? ->
            ranker.invoke(player)
            visit.invoke(player)
        }
        this.traverseInOrder(rankAndVisit)
    }

}