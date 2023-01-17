package com.bestclic.leaderboards.controller

import com.bestclic.leaderboards.controller.service.GameService
import com.bestclic.leaderboards.controller.service.Player
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
class GameController(private val gameService: GameService) {

    @PostMapping("/addPlayer/{name}")
    fun addPlayer(@PathVariable name: String): ResponseEntity<Any> {
        gameService.addPlayer(name)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/getPlayer/{name}")
    fun getPlayer(@PathVariable name: String): ResponseEntity<Player> {
        val player = gameService.getPlayer(name)
        if (player != null) {
            return ResponseEntity.ok(player)
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/removePlayer/{name}")
    fun removePlayer(@PathVariable name: String): ResponseEntity<Any> {
        gameService.removePlayer(name)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/updatePoints/{name}")
    fun updatePlayerPoints(@PathVariable name: String, @RequestBody newPoints: Int): ResponseEntity<Any> {
        gameService.updatePlayerPoints(name, newPoints)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/leaderboard")
    fun getLeaderBoard(): ResponseEntity<List<Player>> {
        return ResponseEntity.ok(gameService.getLeaderBoard())
    }

    @DeleteMapping("/removeAll")
    fun removeAllPlayers(): ResponseEntity<Any> {
        gameService.endGame()
        return ResponseEntity.ok().build()
    }
}
