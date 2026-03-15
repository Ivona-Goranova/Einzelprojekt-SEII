package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_returnsCorrectGameResult() {
        val gameResult = GameResult(1, "Ivona", 100, 12.5)

        whenever(mockedService.getGameResult(1)).thenReturn(gameResult)

        val result = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(gameResult, result)
    }

    @Test
    fun test_getAllGameResults_returnsAllGameResults() {
        val first = GameResult(1, "first", 100, 10.0)
        val second = GameResult(2, "second", 80, 15.0)
        val results = listOf(first, second)

        whenever(mockedService.getGameResults()).thenReturn(results)

        val result = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(results, result)
    }

    @Test
    fun test_addGameResult_callsServiceWithCorrectArgument() {
        val gameResult = GameResult(0, "newPlayer", 50, 20.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun test_deleteGameResult_callsServiceWithCorrectId() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}