package com.bestclic.leaderboards.controller.service.model

import com.bestclic.leaderboards.controller.service.Player
import kotlin.math.max


class BalancedTree<T : Comparable<T>> {

    val nodes: MutableList<Node<T>> = mutableListOf()

    var root: Node<T>? = null

    fun insert(value: T) {
        root = insert(root, value)
    }

    private fun insert(node: Node<T>?, value: T): Node<T> {
        if (node == null) {
            var newNode = Node(value)
            nodes.add(newNode)
            return newNode
        }

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

    fun remove(value: T) {
        root = remove(root, value)
    }

    private fun remove(node: Node<T>?, value: T): Node<T>? {
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
            value < node.value -> node.leftChild = remove(node.leftChild, value)
            else -> node.rightChild = remove(node.rightChild, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = max(
            balancedNode.leftHeight,
            balancedNode.rightHeight
        ) + 1
        return balancedNode
    }

    fun contains(value: T): Boolean {
         get(value)?:
         return true
        return false
    }

    fun get(value: T) : Node<T>? {
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
    }


    private fun balanced(node: Node<T>): Node<T> {
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
    private fun leftRotate(node: Node<T>): Node<T> {
        val pivot = node.rightChild!!
        node.rightChild = pivot.leftChild
        pivot.leftChild = node
        node.height = max(node.leftHeight, node.rightHeight) + 1
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1

        return pivot
    }

    private fun rightRotate(node: Node<T>): Node<T> {
        val pivot = node.leftChild!!
        node.leftChild = pivot.rightChild
        pivot.rightChild = node
        node.height = max(node.leftHeight, node.rightHeight) + 1
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1
        return pivot
    }

    private fun rightLeftRotate(node: Node<T>): Node<T> {
        val rightChild = node.rightChild ?: return node
        node.rightChild = rightRotate(rightChild)
        return leftRotate(node)
    }

    private fun leftRightRotate(node: Node<T>): Node<T> {
        val leftChild = node.leftChild ?: return node
        node.leftChild = leftRotate(leftChild)
        return rightRotate(node)
    }

    override fun toString() = root?.toString() ?: "empty tree"

    fun getAllInOrderAsc(node: Node<T> = root!!): List<T> {
        val list = mutableListOf<T>()
        node.traverseInOrderAsc { list.add(it!!) }
        return list.toList()
    }



    fun getAllInOrderDesc(node: Node<T> = root!!): List<T> {
        val list = mutableListOf<T>()
        node.traverseInOrderDesc { list.add(it!!) }
        return list.toList()

    }

    fun serialize(node: Node<T> = root!!): MutableList<T?> {
        val list = mutableListOf<T?>()
        node.traversePreOrderWithNull { list.add(it) }
        return list
    }

    fun deserialize(list: MutableList<T?>): Node<T>? {
        val rootValue = list.removeAt(0) ?: return null
        val root = Node<T>(rootValue)
        root.leftChild = deserialize(list)
        root.rightChild = deserialize(list)
        return root
    }


}