package com.bestclic.leaderboards.controller.service.repository

import com.bestclic.leaderboards.controller.service.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository {

    fun saveAll(playersByRank: List<Player>)
    fun findAll(): List<Player>
}