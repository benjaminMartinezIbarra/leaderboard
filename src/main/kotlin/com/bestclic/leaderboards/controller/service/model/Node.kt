package com.bestclic.leaderboards.controller.service.model

import java.lang.Integer.max
import java.util.function.Predicate

typealias Visitor<T> = (T?) -> Unit

class Node<T : Comparable<T>>(var value: T) {

    var height = 0

    var leftChild: Node<T>? = null

    var rightChild: Node<T>? = null

    val leftHeight: Int
        get() = leftChild?.height() ?: -1

    val rightHeight: Int
        get() = rightChild?.height() ?: -1

    val min: Node<T>?
        get() = leftChild?.min ?: this

    val balanceFactor: Int
        get() = leftHeight - rightHeight


    fun height(node: Node<T>? = this): Int {
        return node?.let {
            1 + max(
                height(node.leftChild),
                height(node.rightChild)
            )
        } ?: -1
    }

    override fun toString() = diagram(this)

    private fun diagram(
        node: Node<T>?,
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

    fun traverseInOrderAsc(visit: Visitor<T>) {
        leftChild?.traverseInOrderAsc(visit)
        visit(value)
        rightChild?.traverseInOrderAsc(visit)
    }

    fun traverseInOrderDesc(visit: Visitor<T>) {
        rightChild?.traverseInOrderDesc(visit)
        visit(value)
        leftChild?.traverseInOrderDesc(visit)
    }

    fun traverseNodeInOrderDesc(visit: Visitor<Node<T>>) {
        rightChild?.traverseNodeInOrderDesc(visit)
        visit(this)
        leftChild?.traverseNodeInOrderDesc(visit)
    }

    fun traversePreOrderWithNull(visit: Visitor<T>) {
        visit(value)
        leftChild?.traversePreOrderWithNull(visit) ?: visit(null)
        rightChild?.traversePreOrderWithNull(visit) ?: visit(null)
    }

    fun serialize(node: Node<T> = this): MutableList<T?> {
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


    private fun getInOrderWithPredicate(value: T, p: Predicate<T>) : Node<T>?{
        if (p.test(this.value)) return this
        else return this.leftChild?.getInOrderWithPredicate(value, p)
    }


}


