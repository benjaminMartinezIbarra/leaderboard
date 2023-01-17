package com.bestclic.leaderboards

import com.bestclic.leaderboards.controller.service.GameService
import com.bestclic.leaderboards.controller.service.Player
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var gameService: GameService

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun restartGame(){
        gameService.endGame()
    }

    @Test
    fun testAddPlayer() {
        // given
        val playerName = "player1"

        // when
        mockMvc.perform(post("/game/addPlayer/${playerName}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(playerName))
            .andExpect(status().isOk)
            .andReturn()

        // then
        val players = gameService.getLeaderBoard()
        assertThat(players).isNotEmpty
        assertThat(players).contains(Player(playerName))
    }

    @Test
    fun testUpdatePoints() {
        // given
        val playerName ="player1"

        gameService.addPlayer(playerName)

        // when
        mockMvc.perform(put("/game/updatePoints/${playerName}")
            .contentType(MediaType.APPLICATION_JSON)
            .content("100"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        var updatedPlayer = gameService.getPlayer(playerName)
        assertThat(updatedPlayer?.points).isEqualTo(100)

        // when
        mockMvc.perform(put("/game/updatePoints/${playerName}")
            .contentType(MediaType.APPLICATION_JSON)
            .content("200"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        updatedPlayer = gameService.getPlayer(playerName)
        assertThat(updatedPlayer?.points).isEqualTo(200)
    }

    @Test
    fun testGetPlayer() {
        // given
        val playerName ="player1"
        gameService.addPlayer(playerName)

        // when
        val result = mockMvc.perform(get("/game/getPlayer/${playerName}"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        val responsePlayer = objectMapper.readValue(result.response.contentAsString, Player::class.java)
        assertThat(responsePlayer).isEqualTo(Player(playerName))
    }


    @Test
    fun testRemovePlayer() {
        // given
        val playerName = "player1"
        gameService.addPlayer(playerName)

        //when
        mockMvc.perform(delete("/game/removePlayer/${playerName}"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        val players = gameService.getLeaderBoard()
        assertThat(players).doesNotContain(Player(playerName))
    }


    @Test
    fun testCompleteGame(){

        val player1 = "player1"
        val player2 = "player2"
        val player3 = "player3"
        val player4 = "player4"
        val player5 = "player5"

        //given
        gameService.addPlayerAndPoints(player1, 10)
        gameService.addPlayerAndPoints(player2, 20)
        gameService.addPlayerAndPoints(player3, 30)
        gameService.addPlayerAndPoints(player4, 40)
        gameService.addPlayerAndPoints(player5, 50)

        // when


        var players: Array<Player> = getLeaderBoard()

        assertThat(players).hasSize(5)
        verifyPlayerRanking(players[0], player5, 50, 1 )
        verifyPlayerRanking(players[1], player4, 40, 2 )
        verifyPlayerRanking(players[2], player3, 30, 3 )
        verifyPlayerRanking(players[3], player2, 20, 4 )
        verifyPlayerRanking(players[4], player1, 10, 5 )

        //gameService.removePlayer("player3")
        gameService.updatePlayerPoints(player2, 45)

        players = getLeaderBoard()

        assertThat(players).hasSize(5)
        verifyPlayerRanking(players[0], player5, 50, 1 )
        verifyPlayerRanking(players[1], player2, 45, 2 )
        verifyPlayerRanking(players[2], player4, 40, 3 )
        verifyPlayerRanking(players[3], player3, 30, 4 )
        verifyPlayerRanking(players[4], player1, 10, 5 )

        gameService.removePlayer(player3)
        gameService.updatePlayerPoints(player4, 52)

        players = getLeaderBoard()
        assertThat(players).hasSize(4)
        verifyPlayerRanking(players[0], player4, 52, 1 )
        verifyPlayerRanking(players[1], player5, 50, 2 )
        verifyPlayerRanking(players[2], player2, 45, 3 )
        verifyPlayerRanking(players[3], player1, 10, 4 )

    }

    private fun getLeaderBoard(): Array<Player> {
        val result = mockMvc.perform(get("/game/leaderboard"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        return objectMapper.readValue(result.response.contentAsString, Array<Player>::class.java)
    }

    private fun verifyPlayerRanking(player: Player, name: String, points: Int, rank: Int) {
        assertThat(player.name).isEqualTo(name)
        assertThat(player.points).isEqualTo(points)
        assertThat(player.rank).isEqualTo(rank)
    }
}
