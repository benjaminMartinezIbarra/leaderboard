package com.bestclic.leaderboards.controller.service

import com.bestclic.leaderboards.controller.service.repository.PlayerEntity
import com.bestclic.leaderboards.controller.service.repository.PlayerJPARepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class GameService(

    private val leaderboardService: LeaderboardService,
    private val playerJPARepository: PlayerJPARepository
) {

    @Transactional
    fun addPlayer(name: String) {
        addPlayerAndPoints(name)
    }

    fun addPlayerAndPoints(name: String, points: Int = 0){
        leaderboardService.addPlayer(Player(name, points))
        saveLeaderBoard()
    }

    @Transactional
    fun removePlayer(name: String) {
        val playerToRemove = Player(name)
        leaderboardService.removePlayer(playerToRemove)
        saveLeaderBoard()
        playerJPARepository.deleteById(name)

    }

    @Transactional
    fun updatePlayerPoints(name: String, newPoints: Int) {
        leaderboardService.updatePlayerPoints(Player(name, points = newPoints))
        saveLeaderBoard()
    }

    fun getPlayer(playerName: String): Player? {
        return leaderboardService.getPlayer(Player(playerName))
    }

    fun getLeaderBoard(): List<Player> {
        return leaderboardService.getLeaderBoard()
    }

    private fun saveLeaderBoard() {
        val playerEntities = leaderboardService.getLeaderBoard().map { PlayerEntity.toEntity(it) }
        playerJPARepository.saveAll(playerEntities)
    }

    @Transactional
    fun endGame() {
        leaderboardService.removeAll()
        playerJPARepository.deleteAll()
    }

}
