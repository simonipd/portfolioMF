package com.example.portfoliomf.data.api

import com.example.portfoliomf.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("cuentas/portafolio")
    suspend fun getPortfolio(
        @Header("customer-id") customerId: String,
        @Query("periodo") periodo: String = "YTD"
    ): Response<ResumenPortafolio>

    @GET("cuentas/saldo")
    suspend fun getBalance(
        @Header("customer-id") customerId: String
    ): Response<BalanceResponse>

    @GET("cuentas/posiciones")
    suspend fun getPositions(
        @Header("customer-id") customerId: String,
        @Query("limite") limite: Int = 100,
        @Query("pagina") pagina: Int = 1
    ): Response<PaginatedPosicionesResponse>

    @GET("vitrina")
    suspend fun searchAssets(
        @Query("busqueda") query: String,
        @Query("limite") limite: Int = 100,
        @Query("pagina") pagina: Int = 1
    ): Response<PaginatedVitrinaResponse>

    @GET("vitrina/{ticker}")
    suspend fun getAssetDetails(
        @Path("ticker") ticker: String
    ): Response<VitrinaDetailDto>

    @POST("ordenes/crear")
    suspend fun createOrder(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body orderRequest: OrdenRequest
    ): Response<OrdenResponse>
}
