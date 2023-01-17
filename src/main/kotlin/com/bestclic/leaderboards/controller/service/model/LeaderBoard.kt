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
            node.leftChild = insert(node.leftChild, value)
        } else {
            node.rightChild = insert(node.rightChild, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = max(
            balancedNode.leftHeight,
            balancedNode.rightHeight
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
                if (node.leftChild == null && node.rightChild == null) {
                    return null }
                if (node.leftChild == null) {
                    return node.rightChild }
                if (node.rightChild == null) {
                    return node.leftChild }
                node.rightChild?.min?.value?.let {
                    node.value = it }
                node.rightChild = remove(node.rightChild, node.value)
            }
            value.points <= node.value.points -> node.leftChild = remove(node.leftChild, value)
            else -> node.rightChild = remove(node.rightChild, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = max(
            balancedNode.leftHeight,
            balancedNode.rightHeight
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
                    current.leftChild
                } else {
                    current.rightChild
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
                if (node.leftChild?.balanceFactor == -1) {
                    leftRightRotate(node)
                } else {
                    rightRotate(node)
                }
            }
            -2 -> {
                if (node.rightChild?.balanceFactor == 1) {
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
        val pivot = node.rightChild!!
        node.rightChild = pivot.leftChild
        pivot.leftChild = node
        node.height = max(node.leftHeight, node.rightHeight) + 1
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1

        return pivot
    }

    private fun rightRotate(node: PlayerNode): PlayerNode {
        val pivot = node.leftChild!!
        node.leftChild = pivot.rightChild
        pivot.rightChild = node
        node.height = max(node.leftHeight, node.rightHeight) + 1
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1
        return pivot
    }

    private fun rightLeftRotate(node: PlayerNode): PlayerNode {
        val rightChild = node.rightChild ?: return node
        node.rightChild = rightRotate(rightChild)
        return leftRotate(node)
    }

    private fun leftRightRotate(node: PlayerNode): PlayerNode {
        val leftChild = node.leftChild ?: return node
        node.leftChild = leftRotate(leftChild)
        return rightRotate(node)
    }

    override fun toString() = root?.toString() ?: "empty tree"

    fun getAllInOrderDesc(node: PlayerNode? = root): List<Player> {
        val list = mutableListOf<Player>()
        node?.traverseInOrderDesc { list.add(it!!) }
        return list.toList()

    }




}