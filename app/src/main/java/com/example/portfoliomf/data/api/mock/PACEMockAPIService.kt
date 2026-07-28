package com.example.portfoliomf.data.api.mock

import kotlinx.coroutines.delay
import java.time.Instant
import java.util.UUID

// Data Models

// Response Models
data class PortfolioResponse(
    val portfolioValue: String?,
    val equity: String?,
    val intradayVariation: String?,
    val intradayVariationPercentage: String?,
    val ordersInTransit: Int,
    val performance: List<PerformancePoint>
)

data class PerformancePoint(
    val date: String,
    val portfolioValue: String
)

data class BalanceResponse(
    val balanceUsd: String,
    val availableToTradeUsd: String
)

data class PositionsResponse(
    val limit: Int,
    val count: Int,
    val page: Int,
    val next: String?,
    val previous: String?,
    val results: List<PositionItem>
)

data class PositionItem(
    val ticker: String,
    val name: String,
    val category: String,
    val logo: String?,
    val currentValue: String,
    val intradayVariation: String,
    val intradayVariationPercentage: String
)

data class AssetSearchResponse(
    val limit: Int,
    val count: Int,
    val page: Int,
    val next: String?,
    val previous: String?,
    val results: List<AssetItem>
)

data class AssetItem(
    val ticker: String,
    val name: String,
    val price: String,
    val logo: String?,
    val dayVariation: String,
    val category: String
)

data class AssetDetailResponse(
    val symbol: String,
    val name: String,
    val category: String,
    val price: String,
    val returnPercentage: String,
    val returnAmount: String,
    val description: String,
    val open: String?,
    val volume: String?,
    val marketCap: String?,
    val highPrice: String?,
    val lowPrice: String?,
    val annualMax: String?,
    val sector: String?,
    val industry: String?,
    val minimumOrderAmount: String,
    val customerDetail: CustomerDetail?
)

data class CustomerDetail(
    val availableToTradeUsd: String,
    val shareCount: String?,
    val marketValue: String?,
    val costBasis: String?,
    val profitOrLoss: String?,
    val weightPercentage: String?
)

// Request Models
data class OrderSimulationRequest(
    val ticker: String,
    val side: String, // "BUY" or "SELL"
    val quantity: String? = null,
    val amount: String? = null
)

data class OrderSimulationResponse(
    val ticker: String,
    val instrumentName: String?,
    val category: String,
    val side: String,
    val price: String,
    val quantity: String,
    val amount: String,
    val commission: String,
    val vatAmount: String,
    val vatPercentage: String,
    val feesSubtotal: String,
    val estimatedAmount: String,
    val estimatedTotal: String
)

data class OrderCreateRequest(
    val ticker: String,
    val side: String,
    val quantity: String? = null,
    val amount: String? = null
)

data class OrderCreateResponse(
    val id: String,
    val ticker: String,
    val category: String,
    val instrumentName: String,
    val side: String,
    val quantity: String,
    val status: String,
    val amount: String,
    val referencePrice: String,
    val accountNumber: String,
    val receivedAt: String
)

data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String,
    val fields: Map<String, List<String>>? = null
)

// MARK: - Mock API Service

class PACEMockAPIService private constructor() {
    
    companion object {
        private var instance: PACEMockAPIService? = null
        
        fun getInstance(): PACEMockAPIService =
            instance ?: synchronized(this) {
                instance ?: PACEMockAPIService().also { instance = it }
            }
    }
    
    private val customerId = "1234567890"
    
    // MARK: - Portfolio Endpoints
    
    suspend fun getPortfolio(period: String = "YTD"): PortfolioResponse {
        // Simulate network delay
        delay(300)
        
        return PortfolioResponse(
            portfolioValue = "15320.00",
            equity = "16820.00",
            intradayVariation = "120.50",
            intradayVariationPercentage = "0.79",
            ordersInTransit = 3,
            performance = listOf(
                PerformancePoint(date = "2025-03-01", portfolioValue = "15000.00"),
                PerformancePoint(date = "2025-03-05", portfolioValue = "15100.00"),
                PerformancePoint(date = "2025-03-10", portfolioValue = "15320.00"),
            )
        )
    }
    
    suspend fun getBalance(): BalanceResponse {
        delay(200)
        
        return BalanceResponse(
            balanceUsd = "1500.00",
            availableToTradeUsd = "1500.00"
        )
    }
    
    suspend fun getPositions(limit: Int = 100, page: Int = 1): PositionsResponse {
        delay(400)
        
        return PositionsResponse(
            limit = 100,
            count = 3,
            page = 1,
            next = null,
            previous = null,
            results = listOf(
                PositionItem(
                    ticker = "AAPL",
                    name = "Apple Inc.",
                    category = "STOCKS",
                    logo = "https://cdn.example.com/icons/aapl.png",
                    currentValue = "1523.40",
                    intradayVariation = "12.50",
                    intradayVariationPercentage = "0.83"
                ),
                PositionItem(
                    ticker = "NVDA",
                    name = "NVIDIA Corporation",
                    category = "STOCKS",
                    logo = "https://cdn.example.com/icons/nvda.png",
                    currentValue = "5432.10",
                    intradayVariation = "-45.20",
                    intradayVariationPercentage = "-0.82"
                ),
                PositionItem(
                    ticker = "VOO",
                    name = "Vanguard S&P 500 ETF",
                    category = "ETF",
                    logo = "https://cdn.example.com/icons/voo.png",
                    currentValue = "8364.50",
                    intradayVariation = "25.30",
                    intradayVariationPercentage = "0.30"
                )
            )
        )
    }
    
    // MARK: - Search Endpoints
    
    suspend fun searchAssets(
        query: String,
        limit: Int = 100,
        page: Int = 1
    ): AssetSearchResponse {
        delay(350)
        
        // Filter results based on search
        val allResults = listOf(
            AssetItem(
                ticker = "AAPL",
                name = "Apple Inc.",
                price = "150.00",
                logo = "/logo.png",
                dayVariation = "0.01",
                category = "STOCKS"
            ),
            AssetItem(
                ticker = "MSFT",
                name = "Microsoft Corporation",
                price = "320.00",
                logo = "/logo.png",
                dayVariation = "-0.02",
                category = "STOCKS"
            ),
            AssetItem(
                ticker = "NVDA",
                name = "NVIDIA Corporation",
                price = "410.00",
                logo = "/logo.png",
                dayVariation = "0.05",
                category = "STOCKS"
            ),
            AssetItem(
                ticker = "VOO",
                name = "Vanguard S&P 500 ETF",
                price = "410.00",
                logo = "/logo.png",
                dayVariation = "0.02",
                category = "ETF"
            )
        )
        
        val filtered = allResults.filter { item ->
            item.ticker.lowercase().contains(query.lowercase()) ||
            item.name.lowercase().contains(query.lowercase())
        }
        
        return AssetSearchResponse(
            limit = 100,
            count = filtered.size,
            page = 1,
            next = null,
            previous = null,
            results = filtered
        )
    }
    
    suspend fun getAssetDetail(ticker: String): AssetDetailResponse {
        delay(300)
        
        // Return detail based on ticker
        return when (ticker.uppercase()) {
            "AAPL" -> AssetDetailResponse(
                symbol = "AAPL",
                name = "Apple Inc.",
                category = "STOCKS",
                price = "150.00",
                returnPercentage = "0.01",
                returnAmount = "1.00",
                description = "Apple Inc. es una empresa tecnológica que diseña, fabrica y comercializa productos electrónicos y software.",
                open = "149.00",
                volume = "12500000",
                marketCap = "2800000000000",
                highPrice = "151.20",
                lowPrice = "148.75",
                annualMax = "198.23",
                sector = "Technology",
                industry = "Consumer Electronics",
                minimumOrderAmount = "5.00",
                customerDetail = CustomerDetail(
                    availableToTradeUsd = "100000.00",
                    shareCount = "100.02159248",
                    marketValue = "100000.00",
                    costBasis = "100000.00",
                    profitOrLoss = "0.00",
                    weightPercentage = "25.50"
                )
            )
            "MSFT" -> AssetDetailResponse(
                symbol = "MSFT",
                name = "Microsoft Corporation",
                category = "STOCKS",
                price = "320.00",
                returnPercentage = "-0.02",
                returnAmount = "-6.40",
                description = "Microsoft Corporation es una empresa tecnológica multinacional que produce software informático y productos electrónicos de consumo.",
                open = "325.00",
                volume = "28000000",
                marketCap = "2400000000000",
                highPrice = "328.00",
                lowPrice = "318.00",
                annualMax = "366.78",
                sector = "Technology",
                industry = "Software",
                minimumOrderAmount = "10.00",
                customerDetail = null
            )
            "NVDA" -> AssetDetailResponse(
                symbol = "NVDA",
                name = "NVIDIA Corporation",
                category = "STOCKS",
                price = "410.00",
                returnPercentage = "0.05",
                returnAmount = "20.00",
                description = "NVIDIA Corporation es una empresa de semiconductores y líder en inteligencia artificial.",
                open = "405.00",
                volume = "45000000",
                marketCap = "3200000000000",
                highPrice = "415.00",
                lowPrice = "408.00",
                annualMax = "520.00",
                sector = "Technology",
                industry = "Semiconductors",
                minimumOrderAmount = "5.00",
                customerDetail = CustomerDetail(
                    availableToTradeUsd = "100000.00",
                    shareCount = "50.5",
                    marketValue = "20705.00",
                    costBasis = "20000.00",
                    profitOrLoss = "705.00",
                    weightPercentage = "12.75"
                )
            )
            "VOO" -> AssetDetailResponse(
                symbol = "VOO",
                name = "Vanguard S&P 500 ETF",
                category = "ETF",
                price = "410.00",
                returnPercentage = "0.02",
                returnAmount = "8.20",
                description = "El Vanguard S&P 500 ETF es un fondo cotizado en bolsa que replica el índice S&P 500.",
                open = "408.00",
                volume = "5000000",
                marketCap = "800000000000",
                highPrice = "412.00",
                lowPrice = "407.00",
                annualMax = "440.00",
                sector = "Diversified",
                industry = "ETFs",
                minimumOrderAmount = "1.00",
                customerDetail = null
            )
            "ERROR" -> {
                throw Exception("MARKET_CLOSED")
            }
            else -> AssetDetailResponse(
                symbol = ticker.uppercase(),
                name = "Unknown Asset",
                category = "STOCKS",
                price = "0.00",
                returnPercentage = "0.00",
                returnAmount = "0.00",
                description = "Information not available",
                open = null,
                volume = null,
                marketCap = null,
                highPrice = null,
                lowPrice = null,
                annualMax = null,
                sector = null,
                industry = null,
                minimumOrderAmount = "5.00",
                customerDetail = null
            )
        }
    }
    
    // MARK: - Order Endpoints - pending confirmation
    
    suspend fun simulateOrder(request: OrderSimulationRequest): OrderSimulationResponse {
        delay(400)
        
        val price = "410.01"
        val quantity = request.quantity ?: "1.0007"
        val amount = request.amount ?: "400.00"
        val commission = "1.05"
        val vatAmount = "0.20"
        
        return OrderSimulationResponse(
            ticker = request.ticker,
            instrumentName = "NVIDIA Corporation",
            category = "STOCKS",
            side = request.side,
            price = price,
            quantity = quantity,
            amount = amount,
            commission = commission,
            vatAmount = vatAmount,
            vatPercentage = "19.00",
            feesSubtotal = "1.25",
            estimatedAmount = amount,
            estimatedTotal = "401.25"
        )
    }
    
    suspend fun createOrder(request: OrderCreateRequest): OrderCreateResponse {
        delay(500)
        
        if (request.ticker == "ERROR") {
             throw Exception("MARKET_CLOSED")
        }

        // Generate random UUID for the order
        val orderId = UUID.randomUUID().toString()
        val timestamp = Instant.now().toString()
        
        return OrderCreateResponse(
            id = orderId,
            ticker = request.ticker,
            category = "STOCKS",
            instrumentName = if (request.ticker == "NVDA") "NVIDIA Corporation" else "Apple Inc.",
            side = request.side,
            quantity = request.quantity ?: "1.4",
            status = "SUBMITTED",
            amount = request.amount ?: "400.00",
            referencePrice = "150.00",
            accountNumber = "VQBU000001",
            receivedAt = timestamp
        )
    }
    
    // MARK: - Helper Methods
    
    fun getHeaders(): Map<String, String> {
        return mapOf(
            "customer-id" to customerId,
            "Channel" to "APP",
            "Request-Id" to UUID.randomUUID().toString()
        )
    }
    
    fun getOrderCreationHeaders(): Map<String, String> {
        val headers = getHeaders().toMutableMap()
        headers["Idempotency-Key"] = UUID.randomUUID().toString()
        return headers
    }
}
