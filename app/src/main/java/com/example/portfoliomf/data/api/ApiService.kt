package com.example.portfoliomf.data.api

import com.example.portfoliomf.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("accounts/portfolio")
    suspend fun getPortfolio(
        @Header("customer-id") customerId: String,
        @Query("periodo") period: String = "YTD"
    ): Response<PortfolioSummary>

    @GET("accounts/balance")
    suspend fun getBalance(
        @Header("customer-id") customerId: String
    ): Response<BalanceResponse>

    @GET("accounts/positions")
    suspend fun getPositions(
        @Header("customer-id") customerId: String,
        @Query("limite") limit: Int = 100,
        @Query("pagina") page: Int = 1
    ): Response<PaginatedPositionsResponse>

    @GET("showcase")
    suspend fun searchAssets(
        @Query("search") query: String,
        @Query("limite") limit: Int = 100,
        @Query("pagina") page: Int = 1
    ): Response<PaginatedAssetResponse>

    @GET("showcase/{ticker}")
    suspend fun getAssetDetails(
        @Path("ticker") ticker: String
    ): Response<AssetDetailDto>

    @POST("orders/create")
    suspend fun createOrder(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>
}
