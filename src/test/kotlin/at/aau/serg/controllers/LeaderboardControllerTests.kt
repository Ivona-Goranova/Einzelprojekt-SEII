package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_shorterTimeFirstSorting()        {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_withRank_returnsPlayerAndThreeAround() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)
        val p6 = GameResult(6, "p6", 50, 10.0)
        val p7 = GameResult(7, "p7", 40, 10.0)
        val p8 = GameResult(8, "p8", 30, 10.0)
        val p9 = GameResult(9, "p9", 20, 10.0)
        val p10 = GameResult(10, "p10", 10, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p2, p6, p8, p1, p9, p3, p4, p10, p5, p7))

        val result: List<GameResult> = controller.getLeaderboard(5)

        verify(mockedService).getGameResults()
        assertEquals(7, result.size)
        assertEquals(p2, result[0])
        assertEquals(p3, result[1])
        assertEquals(p4, result[2])
        assertEquals(p5, result[3])
        assertEquals(p6, result[4])
        assertEquals(p7, result[5])
        assertEquals(p8, result[6])
    }

    @Test
    fun test_getLeaderboard_invalidRank_throwsBadRequest() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 15, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        val exception = assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(3)
        }

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }

    @Test
    fun test_getLeaderboard_rankZero_throwsBadRequest() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 15, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        val exception = assertFailsWith<ResponseStatusException> {
            controller.getLeaderboard(0)
        }

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }

    @Test
    fun test_getLeaderboard_rankAtStart_returnsUpperRange() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p3, p1, p5, p2, p4))

        val result: List<GameResult> = controller.getLeaderboard(1)

        verify(mockedService).getGameResults()
        assertEquals(4, result.size)
        assertEquals(p1, result[0])
        assertEquals(p2, result[1])
        assertEquals(p3, result[2])
        assertEquals(p4, result[3])
    }

    @Test
    fun test_getLeaderboard_rankAtEnd_returnsLowerRange() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p4, p2, p5, p1, p3))

        val result: List<GameResult> = controller.getLeaderboard(5)

        verify(mockedService).getGameResults()
        assertEquals(4, result.size)
        assertEquals(p2, result[0])
        assertEquals(p3, result[1])
        assertEquals(p4, result[2])
        assertEquals(p5, result[3])
    }
}