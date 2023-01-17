package com.bestclic.leaderboards.controller.service


class Player(val name: String, var points: Int = 0, var rank: Int = 0) : Comparable<Player> {
    override fun compareTo(other: Player) = this.points.compareTo(other.points)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Player
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "$name, Points: $points"
    }

}