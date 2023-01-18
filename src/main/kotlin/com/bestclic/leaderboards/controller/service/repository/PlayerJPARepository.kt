package com.bestclic.leaderboards.controller.service.repository

import com.bestclic.leaderboards.controller.service.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.Id


@Repository
interface PlayerJPARepository : JpaRepository<PlayerEntity, String> {
}

@Entity
data class PlayerEntity(

    @Id
    val name: String,
    val points: Int,
    var rank: Int){
    constructor(): this("", 0, 0)
    companion object
    {
        fun toEntity(player: Player): PlayerEntity {
           return PlayerEntity(player.name, player.points, player.rank)
        }
    }
}



