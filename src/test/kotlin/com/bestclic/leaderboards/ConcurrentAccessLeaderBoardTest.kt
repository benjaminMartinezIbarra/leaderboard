package com.bestclic.leaderboards

import com.bestclic.leaderboards.controller.service.Player
import com.bestclic.leaderboards.controller.service.model.LeaderBoard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ConcurrentAccessLeaderBoardTest {

    @Test
    fun testConcurrentAccessWithCoroutines() = runBlocking {
        val leaderBoard = LeaderBoard()

        val insertJob = launch {
            repeat(1000) {
                leaderBoard.insert(Player("Player $it", it))
                delay(10)
            }
        }

        val removeJob = launch {
            repeat(500) {
                leaderBoard.remove(Player("Player $it", it))
                delay(20)
            }
        }

        val getJob = launch {
            repeat(1000) {
                leaderBoard.get(Player("Player $it", it))
                delay(5)
            }
        }

        insertJob.join()
        removeJob.join()
        getJob.join()

        assertEquals(500,
            leaderBoard.getAllInOrderDesc().size)
    }

}