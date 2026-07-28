package com.example.portfoliomf.ui.assets

import app.cash.turbine.test
import com.example.portfoliomf.data.api.NetworkResult
import com.example.portfoliomf.data.models.AssetDetailDto
import com.example.portfoliomf.data.models.AssetDto
import com.example.portfoliomf.data.models.OrderResponse
import com.example.portfoliomf.data.models.PaginatedAssetResponse
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AssetViewModelTest {

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
    fun `searchAssets updates state with results on success`() = runTest {
        val query = "AAPL"
        val mockResults = listOf(AssetDto(query, "Apple", "150.0", "", "0.5", "Stocks"))
        val mockResponse =
            PaginatedAssetResponse(limit = 10, count = 1, page = 1, results = mockResults)
        
        coEvery { repository.searchAssets(query) } returns NetworkResult.Success(mockResponse)
        val viewModel = AssetViewModel(repository)

        viewModel.searchAssets(query)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchUIState.test {
            val state = awaitItem()
            assertEquals(mockResults, state.results)
            assertEquals(false, state.isSearching)
        }
    }

    @Test
    fun `getAssetDetails updates state to Success on success`() = runTest {
        val ticker = "AAPL"
        val mockDetail = AssetDetailDto(
            symbol = ticker,
            name = "Apple",
            category = "Stocks",
            price = "150.0",
            returnPercentage = "0.5",
            returnAmount = "1.0",
            description = "Desc",
            open = null,
            volume = null,
            marketCap = null,
            highPrice = null,
            lowPrice = null,
            annualMax = null,
            sector = null,
            industry = null,
            minimumOrderAmount = "1.0",
            customerDetail = null
        )
        
        coEvery { repository.getAssetDetails(ticker) } returns NetworkResult.Success(mockDetail)
        val viewModel = AssetViewModel(repository)

        viewModel.getAssetDetails(ticker)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.detailState.test {
            val state = awaitItem()
            assertTrue(state is AssetDetailState.Success)
            assertEquals(mockDetail, (state as AssetDetailState.Success).details)
        }
    }

    @Test
    fun `buyAsset updates state to Success on success`() = runTest {
        val ticker = "AAPL"
        val amount = "100.0"
        val mockOrderResponse = OrderResponse(
            id = "order-123",
            ticker = ticker,
            instrumentName = "Apple",
            status = "COMPLETED",
            amount = amount,
            quantity = "1",
            receivedAt = "now"
        )
        
        coEvery { repository.createOrder(any(), any()) } returns NetworkResult.Success(mockOrderResponse)
        val viewModel = AssetViewModel(repository)

        viewModel.buyAsset(ticker, amount)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.orderState.test {
            val state = awaitItem()
            assertTrue(state is OrderState.Success)
            assertEquals("order-123", (state as OrderState.Success).orderId)
        }
    }

    @Test
    fun `buyAsset updates state to Error with MARKET_CLOSED message when applicable`() = runTest {
        coEvery { repository.createOrder(any(), any()) } returns NetworkResult.Error(message = "MARKET_CLOSED")
        val viewModel = AssetViewModel(repository)

        viewModel.buyAsset("AAPL", "100.0")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.orderState.test {
            val state = awaitItem()
            assertTrue(state is OrderState.Error)
            assertEquals("MARKET_CLOSED", (state as OrderState.Error).message)
        }
    }
}
