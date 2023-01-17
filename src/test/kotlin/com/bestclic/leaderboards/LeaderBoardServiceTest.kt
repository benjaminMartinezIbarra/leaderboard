package com.bestclic.leaderboards

import com.bestclic.leaderboards.controller.service.LeaderboardService
import com.bestclic.leaderboards.controller.service.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class LeaderboardServiceTest {

    private val leaderboard: LeaderboardService = LeaderboardService()

    @BeforeEach
    fun setup() {
        leaderboard.removeAll()
    }

    @Test
    fun testAddPlayer() {
        leaderboard.addPlayer(Player("player1"))
        val players = leaderboard.getLeaderBoard()
        assertEquals(1, players.size)
        assertEquals("player1", players[0].name)
    }

    @Test
    fun testRemovePlayer() {
        val player = Player("player1")
        leaderboard.addPlayer(player)
        leaderboard.removePlayer(player)
        val players = leaderboard.getLeaderBoard()
        assertEquals(0, players.size)
    }

    @Test
    fun testUpdatePlayerPoints() {
        val player = Player("player1")
        leaderboard.addPlayer(player)
        player.points = 10
        leaderboard.updatePlayerPoints(player)
        val rankedPlayer = leaderboard.getPlayer(player)
        assertEquals(10, rankedPlayer?.points)
    }

    @Test
    fun testGetPlayersFromLeaderBoard() {

        val player1 = Player("player1")
        val player2 = Player("player2")
        val player3 = Player("player3")
        val player4 = Player("player4")

        leaderboard.addPlayer(player1)
        leaderboard.addPlayer(player2)
        leaderboard.addPlayer(player3)
        leaderboard.addPlayer(player4)

        player1.points = 50
        player2.points = 50
        player3.points = 70
        player4.points = 60

        leaderboard.updatePlayerPoints(player1)
        leaderboard.updatePlayerPoints(player2)
        leaderboard.updatePlayerPoints(player3)
        leaderboard.updatePlayerPoints(player4)

        assertEquals(4, leaderboard.getPlayer(player1)!!.rank)
        assertEquals(3, leaderboard.getPlayer(player2)!!.rank)
        assertEquals(1, leaderboard.getPlayer(player3)!!.rank)
        assertEquals(2, leaderboard.getPlayer(player4)!!.rank)

        leaderboard.removePlayer(player2)

        assertEquals(3, leaderboard.getPlayer(player1)!!.rank)
        assertEquals(1, leaderboard.getPlayer(player3)!!.rank)
        assertEquals(2, leaderboard.getPlayer(player4)!!.rank)
    }

    @Test
    fun testGetLeaderBoard() {
        val leaderboardService = LeaderboardService()
        val player1 = Player("Player1", points = 100)
        val player2 = Player("Player2", points = 200)
        val player3 = Player("Player3", points = 150)
        val player4 = Player("Player4", points = 50)

        leaderboardService.addPlayer(player1)
        leaderboardService.addPlayer(player2)
        leaderboardService.addPlayer(player3)
        leaderboardService.addPlayer(player4)

        val players = leaderboardService.getLeaderBoard()

        assertEquals(player2, players[0])
        assertEquals(player3, players[1])
        assertEquals(player1, players[2])
        assertEquals(player4, players[3])
    }
}
