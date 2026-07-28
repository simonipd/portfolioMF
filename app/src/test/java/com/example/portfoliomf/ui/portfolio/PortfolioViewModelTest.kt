package com.example.portfoliomf.ui.portfolio

import app.cash.turbine.test
import com.example.portfoliomf.data.api.NetworkResult
import com.example.portfoliomf.data.models.BalanceResponse
import com.example.portfoliomf.data.models.PortfolioSummary
import com.example.portfoliomf.data.models.PaginatedPositionsResponse
import com.example.portfoliomf.data.repository.PortfolioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private val repository: PortfolioRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData updates uiState with success data`() = runTest {
        // Given
        val mockPortfolio = PortfolioSummary(
            portfolioValue = "100.00",
            equity = "100.00",
            intradayVariation = "1.00",
            intradayVariationPercentage = "1.0",
            ordersInTransit = 0,
            performance = emptyList()
        )
        val mockBalance = BalanceResponse(
            availableToTradeUsd = "50.00",
            availableToWithdrawUsd = "40.00",
            usdBalance = "50.00",
            clpBalance = "0",
            updatedAt = "now"
        )
        val mockPositions = PaginatedPositionsResponse(limit = 10, count = 0, page = 1, results = emptyList())

        coEvery { repository.getPortfolio() } returns NetworkResult.Success(mockPortfolio)
        coEvery { repository.getBalance() } returns NetworkResult.Success(mockBalance)
        coEvery { repository.getPositions() } returns NetworkResult.Success(mockPositions)

        // When
        val viewModel = PortfolioViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(mockPortfolio, state.portfolio)
            assertEquals(mockBalance, state.balance)
            assertEquals(0, state.positions.size)
        }
    }

    @Test
    fun `loadData updates uiState with error message when repository fails`() = runTest {
        // Given
        coEvery { repository.getPortfolio() } returns NetworkResult.Error(message = "Portfolio Error")
        coEvery { repository.getBalance() } returns NetworkResult.Error(message = "Balance Error")
        coEvery { repository.getPositions() } returns NetworkResult.Error(message = "Positions Error")

        // When
        val viewModel = PortfolioViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.errorMessage)
        }
    }
}
