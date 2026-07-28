package com.example.portfoliomf.data.api

import com.example.portfoliomf.data.models.*
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeApiService : ApiService {
    override suspend fun getPortfolio(customerId: String, periodo: String): Response<ResumenPortafolio> {
        delay(500)
        return Response.success(
            ResumenPortafolio(
                valorPortafolio = "40.433,00",
                patrimonio = "40.433,00",
                variacionIntraday = "7.06",
                variacionIntradayPorcentaje = "0,04",
                ordenesEnTransito = 0,
                evolucion = listOf(
                    PuntoEvolucion("2023-01-01", "35000.0"),
                    PuntoEvolucion("2023-06-01", "38000.0"),
                    PuntoEvolucion("2023-10-01", "40433.0")
                )
            )
        )
    }

    override suspend fun getBalance(customerId: String): Response<BalanceResponse> {
        delay(300)
        return Response.success(
            BalanceResponse(
                disponibleParaOperarUsd = "40.455,21",
                disponibleParaRetirarUsd = "35.000,00",
                balanceUsd = "40.455,21",
                actualizadoEn = "2023-10-27T10:00:00Z"
            )
        )
    }

    override suspend fun getPositions(
        customerId: String,
        limite: Int,
        pagina: Int
    ): Response<PaginatedPosicionesResponse> {
        delay(400)
        val positions = listOf(
            PosicionItem("AAL", "American Airlines Group Inc.", "Transporte", null, "396.84", "0.25", "0.57"),
            PosicionItem("AMZN", "Amazon", "E-commerce", null, "854.08", "0.21", "0.14"),
            PosicionItem("TSLA", "Tesla", "Automotriz", null, "576.28", "-1.33", "-0.27"),
            PosicionItem("APPL", "Apple", "Tecnología", null, "782.01", "1.04", "0.06"),
            PosicionItem("EXPI", "Exp World Holdings, Inc.", "Bienes Raíces", null, "219.78", "0.15", "0.10")
        )
        return Response.success(
            PaginatedPosicionesResponse(
                limite = limite,
                conteo = positions.size,
                pagina = pagina,
                resultados = positions
            )
        )
    }

    override suspend fun searchAssets(
        query: String,
        limite: Int,
        pagina: Int
    ): Response<PaginatedVitrinaResponse> {
        delay(300)
        val allAssets = listOf(
            VitrinaDto("AAL", "American Airlines Group Inc.", "396.84", "", "0.57", "Acciones"),
            VitrinaDto("AMRC", "Americanino Apparel", "150.00", "", "0.18", "Acciones"),
            VitrinaDto("AMZN", "Amazon", "854.08", "", "0.14", "Acciones"),
            VitrinaDto("ABNB", "Airbnb Inc.", "130.00", "", "-0.27", "Acciones"),
            VitrinaDto("ARA", "Aurora Group", "90.00", "", "-0.06", "Acciones")
        )
        val filtered = if (query.isEmpty()) allAssets else allAssets.filter {
            it.nemo.contains(query, ignoreCase = true) || it.nombre.contains(query, ignoreCase = true)
        }
        return Response.success(
            PaginatedVitrinaResponse(
                limite = limite,
                count = filtered.size,
                pagina = pagina,
                results = filtered
            )
        )
    }

    override suspend fun getAssetDetails(ticker: String): Response<VitrinaDetailDto> {
        delay(300)
        return Response.success(
            VitrinaDetailDto(
                simbolo = ticker,
                nombre = if (ticker == "AAL") "American Airlines Group Inc." else "Empresa $ticker",
                categoria = "Acciones",
                precio = "396.84",
                retornoPorcentaje = "0.57",
                retornoMonto = "0.25",
                descripcion = "American Airlines Group Inc. is a major American airline holding company headquartered in Fort Worth, Texas.",
                apertura = "395.0",
                volumen = "1000000",
                marketCap = "10B",
                precioMasAlto = "400.0",
                precioMasBajo = "390.0",
                maximoAnual = "450.0",
                sector = "Transporte",
                industria = "Aerolíneas",
                montoMinimoDeOrden = "1.0",
                detalleCliente = null
            )
        )
    }

    override suspend fun createOrder(
        idempotencyKey: String,
        orderRequest: OrdenRequest
    ): Response<OrdenResponse> {
        delay(1000)
        return if (orderRequest.nemo == "ERROR") {
            Response.error(400, "{\"codigoError\": \"MERCADO_CERRADO\", \"mensajeError\": \"El mercado está cerrado\"}".toResponseBody("application/json".toMediaTypeOrNull()))
        } else {
            Response.success(
                OrdenResponse(
                    id = "ord-123",
                    nemo = orderRequest.nemo,
                    nombreInstrumento = "Instrumento ${orderRequest.nemo}",
                    estado = "COMPLETADA",
                    monto = orderRequest.monto,
                    cantidad = orderRequest.cantidad ?: "1",
                    fechaRecepcion = "2023-10-27T12:00:00Z"
                )
            )
        }
    }
}
