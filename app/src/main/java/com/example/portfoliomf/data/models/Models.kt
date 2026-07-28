package com.example.portfoliomf.data.models

// --- Portfolio and Balances ---

data class PortfolioSummary(
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
    val availableToTradeUsd: String,
    val availableToWithdrawUsd: String,
    val usdBalance: String,
    val clpBalance: String,
    val updatedAt: String
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

data class PaginatedPositionsResponse(
    val limit: Int,
    val count: Int,
    val page: Int,
    val results: List<PositionItem>
)

// --- Asset Gallery (Search and Detail) ---

data class AssetDto(
    val ticker: String,
    val name: String,
    val price: String,
    val logo: String,
    val dayVariation: String,
    val category: String
)

data class PaginatedAssetResponse(
    val limit: Int,
    val count: Int,
    val page: Int,
    val results: List<AssetDto>
)

data class AssetDetailDto(
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
    val profitOrLoss: String?
)

// --- Orders ---

data class OrderRequest(
    val ticker: String,
    val side: String, // "BUY" or "SELL"
    val amount: String? = null,
    val quantity: String? = null
)

data class OrderResponse(
    val id: String,
    val ticker: String,
    val instrumentName: String,
    val status: String,
    val amount: String?,
    val quantity: String,
    val receivedAt: String
)

// --- Errors ---

data class ErrorDto(
    val errorCode: String,
    val errorMessage: String,
    val fields: Map<String, List<String>>? = null
)
