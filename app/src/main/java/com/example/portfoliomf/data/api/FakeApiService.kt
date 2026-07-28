package com.example.portfoliomf.data.api

import com.example.portfoliomf.data.models.*
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeApiService : ApiService {
    override suspend fun getPortfolio(customerId: String, period: String): Response<PortfolioSummary> {
        delay(500)
        return Response.success(
            PortfolioSummary(
                portfolioValue = "40.433,00",
                equity = "40.433,00",
                intradayVariation = "7.06",
                intradayVariationPercentage = "0,04",
                ordersInTransit = 0,
                performance = listOf(
                    PerformancePoint("2023-01-01", "35000.0"),
                    PerformancePoint("2023-06-01", "38000.0"),
                    PerformancePoint("2023-10-01", "40433.0")
                )
            )
        )
    }

    override suspend fun getBalance(customerId: String): Response<BalanceResponse> {
        delay(300)
        return Response.success(
            BalanceResponse(
                availableToTradeUsd = "40.455,21",
                availableToWithdrawUsd = "35.000,00",
                usdBalance = "40.455,21",
                clpBalance = "$0",
                updatedAt = "2023-10-27T10:00:00Z"
            )
        )
    }

    override suspend fun getPositions(
        customerId: String,
        limit: Int,
        page: Int
    ): Response<PaginatedPositionsResponse> {
        delay(400)
        val results = listOf(
            PositionItem("AAL", "American Airlines Group Inc.", "Transport", null, "396.84", "0.25", "0.57"),
            PositionItem("AMZN", "Amazon", "E-commerce", null, "854.08", "0.21", "0.14"),
            PositionItem("TSLA", "Tesla", "Automotive", null, "576.28", "-1.33", "-0.27"),
            PositionItem("APPL", "Apple", "Technology", null, "782.01", "1.04", "0.06"),
            PositionItem("EXPI", "Exp World Holdings, Inc.", "Real Estate", null, "219.78", "0.15", "0.10")
        )
        return Response.success(
            PaginatedPositionsResponse(
                limit = limit,
                count = results.size,
                page = page,
                results = results
            )
        )
    }

    override suspend fun searchAssets(
        query: String,
        limit: Int,
        page: Int
    ): Response<PaginatedAssetResponse> {
        delay(300)
        val allAssets = listOf(
            AssetDto("AAL", "American Airlines Group Inc.", "396.84", "", "0.57", "Stocks"),
            AssetDto("AMRC", "Americanino Apparel", "150.00", "", "0.18", "Stocks"),
            AssetDto("AMZN", "Amazon", "854.08", "", "0.14", "Stocks"),
            AssetDto("ABNB", "Airbnb Inc.", "130.00", "", "-0.27", "Stocks"),
            AssetDto("ARA", "Aurora Group", "90.00", "", "-0.06", "Stocks")
        )
        val filtered = if (query.isEmpty()) allAssets else allAssets.filter {
            it.ticker.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
        }
        return Response.success(
            PaginatedAssetResponse(
                limit = limit,
                count = filtered.size,
                page = page,
                results = filtered
            )
        )
    }

    override suspend fun getAssetDetails(ticker: String): Response<AssetDetailDto> {
        delay(300)
        return Response.success(
            AssetDetailDto(
                symbol = ticker,
                name = if (ticker == "AAL") "American Airlines Group Inc." else "Company $ticker",
                category = "Stocks",
                price = "396.84",
                returnPercentage = "0.57",
                returnAmount = "0.25",
                description = "American Airlines Group Inc. is a major American airline holding company headquartered in Fort Worth, Texas.",
                open = "395.0",
                volume = "1000000",
                marketCap = "10B",
                highPrice = "400.0",
                lowPrice = "390.0",
                annualMax = "450.0",
                sector = "Transport",
                industry = "Airlines",
                minimumOrderAmount = "1.0",
                customerDetail = null
            )
        )
    }

    override suspend fun createOrder(
        idempotencyKey: String,
        orderRequest: OrderRequest
    ): Response<OrderResponse> {
        delay(1000)
        return if (orderRequest.ticker == "ERROR") {
            Response.error(400, "{\"errorCode\": \"MARKET_CLOSED\", \"errorMessage\": \"The market is currently closed\"}".toResponseBody("application/json".toMediaTypeOrNull()))
        } else {
            Response.success(
                OrderResponse(
                    id = "ord-123",
                    ticker = orderRequest.ticker,
                    instrumentName = "Instrument ${orderRequest.ticker}",
                    status = "COMPLETED",
                    amount = orderRequest.amount,
                    quantity = orderRequest.quantity ?: "1",
                    receivedAt = "2023-10-27T12:00:00Z"
                )
            )
        }
    }
}
