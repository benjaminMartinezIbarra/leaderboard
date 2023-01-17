package com.bestclic.leaderboards.controller.service

import com.bestclic.leaderboards.controller.service.model.LeaderBoard
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class LeaderboardService {

    private val leaderboard = LeaderBoard()

    @Cacheable("playersByRank")
    fun getLeaderBoard(): List<Player> {
        return leaderboard.getAllInOrderDesc()
    }

    @CacheEvict("playersByRank")
    fun updatePlayerPoints(player: Player) {
        removePlayer(player)
        addPlayer(player)
    }

    @CacheEvict("playersByRank")
    fun removePlayer(player: Player) {
        leaderboard.remove(player)
    }

    @CacheEvict("playersByRank")
    fun removeAll() {
        getLeaderBoard().forEach() {
            leaderboard.remove(it)
        }
    }

    @CacheEvict("playersByRank")
    fun addPlayer(player: Player){
        leaderboard.insert(player)
    }

    fun getPlayer(searchedPlayer: Player): Player? {
        return getLeaderBoard().find { player -> player == searchedPlayer }
    }

    override fun toString(): String {
        return leaderboard.toString()
    }
}
