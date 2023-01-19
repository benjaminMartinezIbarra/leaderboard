package com.bestclic.leaderboards.controller.service.model

import com.bestclic.leaderboards.controller.service.Player
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.math.max


class LeaderBoard {

    private val rwLock = ReentrantReadWriteLock()

    var root: PlayerNode? = null

    fun insert(value: Player) {
        rwLock.writeLock().lock()
        try {
            root = insert(root, value)
        } finally {
            rwLock.writeLock().unlock()
        }
    }


    private fun insert(node: PlayerNode?, value: Player): PlayerNode {
       node?: return PlayerNode(value)

        if (value < node.value) {
            node.leftNode = insert(node.leftNode, value)
        } else {
            node.rightNode = insert(node.rightNode, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = max(
            balancedNode.leftNodeHeight,
            balancedNode.rightNodeHeight
        ) + 1
        return balancedNode
    }

    fun remove(value: Player) {
        rwLock.writeLock().lock()
        try {
            root = remove(root, value)
        } finally {
            rwLock.writeLock().unlock()
        }
    }

    private fun remove(node: PlayerNode?, value: Player): PlayerNode? {
        node ?: return null
        when {
            value == node.value -> {
                if (node.leftNode == null && node.rightNode == null) {
                    return null }
                if (node.leftNode == null) {
                    return node.rightNode }
                if (node.rightNode == null) {
                    return node.leftNode }
                node.rightNode?.min?.value?.let {
                    node.value = it }
                node.rightNode = remove(node.rightNode, node.value)
            }
            value.points <= node.value.points -> node.leftNode = remove(node.leftNode, value)
            else -> node.rightNode = remove(node.rightNode, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = max(
            balancedNode.leftNodeHeight,
            balancedNode.rightNodeHeight
        ) + 1
        return balancedNode
    }

    fun get(value: Player): PlayerNode? {
        rwLock.readLock().lock()
        try {
            var current = root
            while (current != null) {
                if (current.value == value) {
                    return current
                }
                current = if (value < current.value) {
                    current.leftNode
                } else {
                    current.rightNode
                }
            }
            return null
        } finally {
            rwLock.readLock().unlock()
        }
    }


    private fun balanced(node: PlayerNode): PlayerNode {
        return when (node.balanceFactor) {
            2 -> {
                if (node.leftNode?.balanceFactor == -1) {
                    leftRightRotate(node)
                } else {
                    rightRotate(node)
                }
            }
            -2 -> {
                if (node.rightNode?.balanceFactor == 1) {
                    rightLeftRotate(node)
                } else {
                    leftRotate(node)
                }
            }
            else -> node
        }
    }


    /**
     * rotation methods to keep the tree balanced after each update
     */
    private fun leftRotate(node: PlayerNode): PlayerNode {
        val pivot = node.rightNode!!
        node.rightNode = pivot.leftNode
        pivot.leftNode = node
        node.height = max(node.leftNodeHeight, node.rightNodeHeight) + 1
        pivot.height = max(pivot.leftNodeHeight, pivot.rightNodeHeight) + 1

        return pivot
    }

    private fun rightRotate(node: PlayerNode): PlayerNode {
        val pivot = node.leftNode!!
        node.leftNode = pivot.rightNode
        pivot.rightNode = node
        node.height = max(node.leftNodeHeight, node.rightNodeHeight) + 1
        pivot.height = max(pivot.leftNodeHeight, pivot.rightNodeHeight) + 1
        return pivot
    }

    private fun rightLeftRotate(node: PlayerNode): PlayerNode {
        val rightChild = node.rightNode ?: return node
        node.rightNode = rightRotate(rightChild)
        return leftRotate(node)
    }

    private fun leftRightRotate(node: PlayerNode): PlayerNode {
        val leftChild = node.leftNode ?: return node
        node.leftNode = leftRotate(leftChild)
        return rightRotate(node)
    }

    override fun toString() = root?.toString() ?: "empty tree"

    fun getAllInOrderDesc(node: PlayerNode? = root): List<Player> {
        val list = mutableListOf<Player>()
        node?.traverseInOrderDesc { list.add(it!!) }
        return list.toList()

    }




}