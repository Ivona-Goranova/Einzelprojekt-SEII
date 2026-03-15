package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): List<GameResult> {

        val sortedResults = gameResultService.getGameResults()
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds })) // sortiert nach Score (absteigend), dann nach kürzerer Spieldauer

        if (rank == null) {
            return sortedResults
        }

        if (rank <= 0 || rank > sortedResults.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Der rank-Wert ist ungültig")
        }

        val indexRank = rank - 1 // Rank beginnt bei 1, Liste beginnt bei 0
        val from = maxOf(0, indexRank - 3) // Der Startindex darf nicht kleiner als 0 sein
        val to = minOf(sortedResults.size - 1, indexRank + 3) // Der Endindex darf nicht größer als der letzte Listenindex sein

        val result = mutableListOf<GameResult>()
        for (i in from..to) {
            result.add(sortedResults[i])
        }

        return result
    }
}

