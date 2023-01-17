package com.bestclic.leaderboards

import com.bestclic.leaderboards.controller.service.repository.PlayerEntity
import com.bestclic.leaderboards.controller.service.repository.PlayerJPARepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class PlayerRepositoryTest {

    @Autowired
    private lateinit var playerRepository: PlayerJPARepository

    @Test
    fun testSaveAllPlayers() {
        val player1 = PlayerEntity("player1", 100, 1)
        val player2 = PlayerEntity("player2", 200, 2)
        playerRepository.saveAll(listOf(player1, player2))
        val players = playerRepository.findAll()
        assertEquals(2, players.size)
        assertTrue(players.contains(player1))
        assertTrue(players.contains(player2))
    }

    @Test
    fun testDeleteAllPlayers() {
        val player = PlayerEntity("player1", 100, 1)
        playerRepository.save(player)
        playerRepository.deleteAll()
        val deletedPlayer = playerRepository.findById("player1").orElse(null)
        assertNull(deletedPlayer)
    }
}
