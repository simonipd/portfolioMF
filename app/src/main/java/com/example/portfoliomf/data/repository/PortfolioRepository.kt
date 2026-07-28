package com.example.portfoliomf.data.repository

import com.example.portfoliomf.data.api.ApiService
import com.example.portfoliomf.data.models.BalanceResponse
import com.example.portfoliomf.data.models.OrderRequest
import com.example.portfoliomf.data.models.OrderResponse
import com.example.portfoliomf.data.models.PaginatedPositionsResponse
import com.example.portfoliomf.data.models.PaginatedAssetResponse
import com.example.portfoliomf.data.models.PortfolioSummary
import com.example.portfoliomf.data.models.AssetDetailDto
import retrofit2.Response

class PortfolioRepository(private val apiService: ApiService) {
    private val customerId = "user-123" // Hardcoded for this project

    suspend fun getPortfolio(periodo: String = "YTD"): Response<PortfolioSummary> =
        apiService.getPortfolio(customerId, periodo)
    
    suspend fun getBalance(): Response<BalanceResponse> =
        apiService.getBalance(customerId)
    
    suspend fun getPositions(): Response<PaginatedPositionsResponse> =
        apiService.getPositions(customerId)
    
    suspend fun searchAssets(query: String): Response<PaginatedAssetResponse> =
        apiService.searchAssets(query)
    
    suspend fun getAssetDetails(ticker: String): Response<AssetDetailDto> =
        apiService.getAssetDetails(ticker)
    
    suspend fun createOrder(idempotencyKey: String, orderRequest: OrderRequest): Response<OrderResponse> =
        apiService.createOrder(idempotencyKey, orderRequest)
}
