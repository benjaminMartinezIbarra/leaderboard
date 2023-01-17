package com.bestclic.leaderboards.controller.service.repository

import com.bestclic.leaderboards.controller.service.Player
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PlayerJPARepository : JpaRepository<PlayerEntity, String> {
}

@Entity
data class PlayerEntity(

    @Id
    val name: String,
    val points: Int,
    var rank: Int){
    companion object
    {
        fun toEntity(player: Player): PlayerEntity {
           return PlayerEntity(player.name, player.points, player.rank)
        }
    }
}



