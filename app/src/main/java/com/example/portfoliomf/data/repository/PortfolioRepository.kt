package com.example.portfoliomf.data.repository

import com.example.portfoliomf.data.api.ApiService
import com.example.portfoliomf.data.models.*
import retrofit2.Response

class PortfolioRepository(private val apiService: ApiService) {
    private val customerId = "user-123" // Hardcoded for this exercise

    suspend fun getPortfolio(periodo: String = "YTD"): Response<ResumenPortafolio> = 
        apiService.getPortfolio(customerId, periodo)
    
    suspend fun getBalance(): Response<BalanceResponse> = 
        apiService.getBalance(customerId)
    
    suspend fun getPositions(): Response<PaginatedPosicionesResponse> = 
        apiService.getPositions(customerId)
    
    suspend fun searchAssets(query: String): Response<PaginatedVitrinaResponse> = 
        apiService.searchAssets(query)
    
    suspend fun getAssetDetails(ticker: String): Response<VitrinaDetailDto> = 
        apiService.getAssetDetails(ticker)
    
    suspend fun createOrder(idempotencyKey: String, orderRequest: OrderRequest): Response<OrdenResponse> = 
        apiService.createOrder(idempotencyKey, orderRequest)
}
