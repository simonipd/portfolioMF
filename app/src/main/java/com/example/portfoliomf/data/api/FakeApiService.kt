package com.example.portfoliomf.data.api

import com.example.portfoliomf.data.models.*
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeApiService : ApiService {
    override suspend fun getPortfolio(customerId: String, periodo: String): Response<PortfolioResponse> {
        delay(500)
        return Response.success(
            PortfolioResponse(
                valorPortafolio = 15450.50,
                patrimonio = 15450.50,
                performance = 5.2,
                evolucion = listOf(
                    EvolucionPoint("2023-01-01", 14000.0),
                    EvolucionPoint("2023-06-01", 14800.0),
                    EvolucionPoint("2023-10-01", 15450.50)
                )
            )
        )
    }

    override suspend fun getBalance(customerId: String): Response<BalanceResponse> {
        delay(300)
        return Response.success(
            BalanceResponse(
                balanceUsd = 2500.0,
                disponibleParaOperarUsd = 2450.0
            )
        )
    }

    override suspend fun getPositions(customerId: String): Response<List<Position>> {
        delay(400)
        return Response.success(
            listOf(
                Position("AAPL", "Apple Inc.", 3500.0),
                Position("TSLA", "Tesla, Inc.", 2800.0),
                Position("MSFT", "Microsoft Corp.", 4200.0),
                Position("GOOGL", "Alphabet Inc.", 2450.50)
            )
        )
    }

    override suspend fun searchAssets(query: String): Response<List<AssetSearchResult>> {
        delay(300)
        val allAssets = listOf(
            AssetSearchResult("AAPL", "Apple Inc.", 175.50),
            AssetSearchResult("TSLA", "Tesla, Inc.", 240.20),
            AssetSearchResult("MSFT", "Microsoft Corp.", 330.10),
            AssetSearchResult("GOOGL", "Alphabet Inc.", 135.00),
            AssetSearchResult("AMZN", "Amazon.com Inc.", 145.20),
            AssetSearchResult("NVDA", "NVIDIA Corp.", 450.50)
        )
        val filtered = allAssets.filter { 
            it.nemo.contains(query, ignoreCase = true) || it.nombre.contains(query, ignoreCase = true) 
        }
        return Response.success(filtered)
    }

    override suspend fun getAssetDetails(ticker: String): Response<AssetDetails> {
        delay(300)
        return Response.success(
            AssetDetails(
                ticker,
                "Empresa $ticker",
                150.0,
                "Descripción detallada de $ticker. Una empresa líder en su sector.",
                mapOf("1D" to 1.2, "1W" to -0.5, "1M" to 5.4, "YTD" to 12.0)
            )
        )
    }

    override suspend fun createOrder(
        customerId: String,
        idempotencyKey: String,
        orderRequest: OrderRequest
    ): Response<OrderResponse> {
        delay(1000)
        return if (orderRequest.nemo == "ERROR") {
            Response.error(400, "{\"codigoError\": \"MERCADO_CERRADO\"}".toResponseBody("application/json".toMediaTypeOrNull()))
        } else {
            Response.success(OrderResponse("ord-123", "COMPLETADA", "Orden creada exitosamente"))
        }
    }
}
