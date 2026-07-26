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
                valorPortafolio = "15450.50",
                patrimonio = "15450.50",
                variacionIntraday = "120.50",
                variacionIntradayPorcentaje = "0.78",
                ordenesEnTransito = 0,
                evolucion = listOf(
                    PuntoEvolucion("2023-01-01", "14000.0"),
                    PuntoEvolucion("2023-06-01", "14800.0"),
                    PuntoEvolucion("2023-10-01", "15450.50")
                )
            )
        )
    }

    override suspend fun getBalance(customerId: String): Response<BalanceResponse> {
        delay(300)
        return Response.success(
            BalanceResponse(
                disponibleParaOperarUsd = "2450.0",
                disponibleParaRetirarUsd = "2000.0",
                balanceUsd = "2500.0",
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
            PosicionItem("AAPL", "Apple Inc.", "Tecnología", null, "3500.0", "10.0", "0.28"),
            PosicionItem("TSLA", "Tesla, Inc.", "Automotriz", null, "2800.0", "-15.0", "-0.53"),
            PosicionItem("MSFT", "Microsoft Corp.", "Tecnología", null, "4200.0", "25.0", "0.60"),
            PosicionItem("GOOGL", "Alphabet Inc.", "Tecnología", null, "2450.50", "5.50", "0.22")
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
            VitrinaDto("AAPL", "Apple Inc.", "175.50", "", "1.2", "Acciones"),
            VitrinaDto("TSLA", "Tesla, Inc.", "240.20", "", "-0.5", "Acciones"),
            VitrinaDto("MSFT", "Microsoft Corp.", "330.10", "", "0.8", "Acciones"),
            VitrinaDto("GOOGL", "Alphabet Inc.", "135.00", "", "0.3", "Acciones"),
            VitrinaDto("AMZN", "Amazon.com Inc.", "145.20", "", "1.5", "Acciones"),
            VitrinaDto("NVDA", "NVIDIA Corp.", "450.50", "", "2.1", "Acciones")
        )
        val filtered = allAssets.filter {
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
                nombre = "Empresa $ticker",
                categoria = "Acciones",
                precio = "150.0",
                retornoPorcentaje = "12.0",
                retornoMonto = "18.0",
                descripcion = "Descripción detallada de $ticker. Una empresa líder en su sector.",
                apertura = "148.5",
                volumen = "1000000",
                marketCap = "1T",
                precioMasAlto = "155.0",
                precioMasBajo = "147.0",
                maximoAnual = "180.0",
                sector = "Tecnología",
                industria = "Software",
                montoMinimoDeOrden = "10.0",
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
