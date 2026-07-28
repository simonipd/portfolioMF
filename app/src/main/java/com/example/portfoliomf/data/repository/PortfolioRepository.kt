package com.example.portfoliomf.data.repository

import com.example.portfoliomf.data.api.ApiService
import com.example.portfoliomf.data.models.BalanceResponse
import com.example.portfoliomf.data.models.OrdenRequest
import com.example.portfoliomf.data.models.OrdenResponse
import com.example.portfoliomf.data.models.PaginatedPosicionesResponse
import com.example.portfoliomf.data.models.PaginatedVitrinaResponse
import com.example.portfoliomf.data.models.ResumenPortafolio
import com.example.portfoliomf.data.models.VitrinaDetailDto
import retrofit2.Response

class PortfolioRepository(private val apiService: ApiService) {
    private val customerId = "user-123" // Hardcoded for this project

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
    
    suspend fun createOrder(idempotencyKey: String, orderRequest: OrdenRequest): Response<OrdenResponse> =
        apiService.createOrder(idempotencyKey, orderRequest)
}
