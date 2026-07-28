package com.example.portfoliomf.data.repository

import com.example.portfoliomf.data.api.ApiService
import com.example.portfoliomf.data.api.BaseRepository
import com.example.portfoliomf.data.api.NetworkResult
import com.example.portfoliomf.data.models.*

class PortfolioRepository(private val apiService: ApiService) : BaseRepository() {
    private val customerId = "user-123" // Hardcoded for this project

    suspend fun getPortfolio(periodo: String = "YTD"): NetworkResult<PortfolioSummary> =
        safeApiCall { apiService.getPortfolio(customerId, periodo) }
    
    suspend fun getBalance(): NetworkResult<BalanceResponse> =
        safeApiCall { apiService.getBalance(customerId) }
    
    suspend fun getPositions(): NetworkResult<PaginatedPositionsResponse> =
        safeApiCall { apiService.getPositions(customerId) }
    
    suspend fun searchAssets(query: String): NetworkResult<PaginatedAssetResponse> =
        safeApiCall { apiService.searchAssets(query) }
    
    suspend fun getAssetDetails(ticker: String): NetworkResult<AssetDetailDto> =
        safeApiCall { apiService.getAssetDetails(ticker) }
    
    suspend fun createOrder(idempotencyKey: String, orderRequest: OrderRequest): NetworkResult<OrderResponse> =
        safeApiCall { apiService.createOrder(idempotencyKey, orderRequest) }
}
