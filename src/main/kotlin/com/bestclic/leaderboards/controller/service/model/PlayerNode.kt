package com.bestclic.leaderboards.controller.service.model

import com.bestclic.leaderboards.controller.service.Player

class PlayerNode(var value: Player) {

    var height = 0

    var leftChild: PlayerNode? = null

    var rightChild: PlayerNode? = null

    val leftHeight: Int
        get() = leftChild?.height() ?: -1

    val rightHeight: Int
        get() = rightChild?.height() ?: -1

    val min: PlayerNode
        get() = leftChild?.min ?: this

    val balanceFactor: Int
        get() = leftHeight - rightHeight


    private fun height(node: PlayerNode? = this): Int {
        return node?.let {
            1 + Integer.max(
                height(node.leftChild),
                height(node.rightChild)
            )
        } ?: -1
    }

    override fun toString() = diagram(this)

    private fun diagram(
        node: PlayerNode?,
        top: String = "",
        root: String = "",
        bottom: String = ""
    ): String {
        return node?.let {
            if (node.leftChild == null && node.rightChild == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightChild, "$top ", "$top┌──", "$top│ ") +
                        root + "${node.value}\n" + diagram(
                    node.leftChild,
                    "$bottom│ ", "$bottom└──", "$bottom "
                )
            }
        } ?: "${root}null\n"
    }

    private fun traverseInOrder(visit: Visitor<Player>) {
        rightChild?.traverseInOrder(visit)
        visit(value)
        leftChild?.traverseInOrder(visit)
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