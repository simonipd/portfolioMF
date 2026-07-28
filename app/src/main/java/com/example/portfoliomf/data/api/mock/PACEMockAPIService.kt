package com.example.portfoliomf.data.api.mock

import kotlinx.coroutines.delay
import java.time.Instant
import java.util.UUID

// Sata Models

// Response Models
data class PortafolioResponse(
    val valorPortafolio: String?,
    val patrimonio: String?,
    val variacionIntraday: String?,
    val variacionIntradayPorcentaje: String?,
    val ordenesEnTransito: Int,
    val evolucion: List<PuntoEvolucion>
)

data class PuntoEvolucion(
    val fecha: String,
    val valorPortafolio: String
)

data class SaldoResponse(
    val balanceUsd: String,
    val disponibleParaOperarUsd: String
)

data class PosicionesResponse(
    val limite: Int,
    val conteo: Int,
    val pagina: Int,
    val siguiente: String?,
    val anterior: String?,
    val resultados: List<PosicionItem>
)

data class PosicionItem(
    val nemo: String,
    val nombre: String,
    val categoria: String,
    val logo: String?,
    val valorActual: String,
    val variacionIntraday: String,
    val variacionIntradayPorcentaje: String
)

data class VitrinaSearchResponse(
    val limite: Int,
    val count: Int,
    val pagina: Int,
    val next: String?,
    val previous: String?,
    val results: List<VitrinaItem>
)

data class VitrinaItem(
    val nemo: String,
    val nombre: String,
    val precio: String,
    val logo: String?,
    val variacionDia: String,
    val categoria: String
)

data class VitrinaDetailResponse(
    val simbolo: String,
    val nombre: String,
    val categoria: String,
    val precio: String,
    val retornoPorcentaje: String,
    val retornoMonto: String,
    val descripcion: String,
    val apertura: String?,
    val volumen: String?,
    val marketCap: String?,
    val precioMasAlto: String?,
    val precioMasBajo: String?,
    val maximoAnual: String?,
    val sector: String?,
    val industria: String?,
    val montoMinimoDeOrden: String,
    val detalleCliente: ClienteDetalle?
)

data class ClienteDetalle(
    val disponibleParaOperarUsd: String,
    val cantidadAcciones: String?,
    val valorDeMercado: String?,
    val costoBase: String?,
    val gananciaOperdida: String?,
    val pesoPorcentaje: String?
)

// Request Models
data class OrdenSimulacionRequest(
    val nemo: String,
    val lado: String, // "COMPRAR" o "VENDER"
    val cantidad: String? = null,
    val monto: String? = null
)

data class OrdenSimulacionResponse(
    val nemo: String,
    val nombreInstrumento: String?,
    val categoria: String,
    val lado: String,
    val precio: String,
    val cantidad: String,
    val monto: String,
    val comision: String,
    val montoIva: String,
    val ivaPorcentaje: String,
    val subtotalCobros: String,
    val montoEstimado: String,
    val totalEstimado: String
)

data class OrdenCrearRequest(
    val nemo: String,
    val lado: String,
    val cantidad: String? = null,
    val monto: String? = null
)

data class OrdenCrearResponse(
    val id: String,
    val nemo: String,
    val categoria: String,
    val nombreInstrumento: String,
    val lado: String,
    val cantidad: String,
    val estado: String,
    val monto: String,
    val precioReferencia: String,
    val numeroDeCuenta: String,
    val fechaRecepcion: String
)

data class ErrorResponse(
    val codigoError: String,
    val mensajeError: String,
    val campos: Map<String, List<String>>? = null
)

// MARK: - Mock API Service

class PACEMockAPIService private constructor() {
    
    companion object {
        private var instance: PACEMockAPIService? = null
        
        fun getInstance(): PACEMockAPIService =
            instance ?: synchronized(this) {
                instance ?: PACEMockAPIService().also { instance = it }
            }
    }
    
    private val customerId = "1234567890"
    
    // MARK: - Portfolio Endpoints
    
    suspend fun getPortafolio(periodo: String = "YTD"): PortafolioResponse {
        // Simular delay de red
        delay(300)
        
        return PortafolioResponse(
            valorPortafolio = "15320.00",
            patrimonio = "16820.00",
            variacionIntraday = "120.50",
            variacionIntradayPorcentaje = "0.79",
            ordenesEnTransito = 3,
            evolucion = listOf(
                PuntoEvolucion(fecha = "2025-03-01", valorPortafolio = "15000.00"),
                PuntoEvolucion(fecha = "2025-03-05", valorPortafolio = "15100.00"),
                PuntoEvolucion(fecha = "2025-03-10", valorPortafolio = "15320.00"),
            )
        )
    }
    
    suspend fun getSaldo(): SaldoResponse {
        delay(200)
        
        return SaldoResponse(
            balanceUsd = "1500.00",
            disponibleParaOperarUsd = "1500.00"
        )
    }
    
    suspend fun getPosiciones(limite: Int = 100, pagina: Int = 1): PosicionesResponse {
        delay(400)
        
        return PosicionesResponse(
            limite = 100,
            conteo = 3,
            pagina = 1,
            siguiente = null,
            anterior = null,
            resultados = listOf(
                PosicionItem(
                    nemo = "AAPL",
                    nombre = "Apple Inc.",
                    categoria = "ACCIONES",
                    logo = "https://cdn.example.com/icons/aapl.png",
                    valorActual = "1523.40",
                    variacionIntraday = "12.50",
                    variacionIntradayPorcentaje = "0.83"
                ),
                PosicionItem(
                    nemo = "NVDA",
                    nombre = "NVIDIA Corporation",
                    categoria = "ACCIONES",
                    logo = "https://cdn.example.com/icons/nvda.png",
                    valorActual = "5432.10",
                    variacionIntraday = "-45.20",
                    variacionIntradayPorcentaje = "-0.82"
                ),
                PosicionItem(
                    nemo = "VOO",
                    nombre = "Vanguard S&P 500 ETF",
                    categoria = "ETF",
                    logo = "https://cdn.example.com/icons/voo.png",
                    valorActual = "8364.50",
                    variacionIntraday = "25.30",
                    variacionIntradayPorcentaje = "0.30"
                )
            )
        )
    }
    
    // MARK: - Search Endpoints
    
    suspend fun searchVitrina(
        busqueda: String,
        limite: Int = 100,
        pagina: Int = 1
    ): VitrinaSearchResponse {
        delay(350)
        
        // Filtrar resultados basados en búsqueda
        val allResults = listOf(
            VitrinaItem(
                nemo = "AAPL",
                nombre = "Apple Inc.",
                precio = "150.00",
                logo = "/logo.png",
                variacionDia = "0.01",
                categoria = "ACCIONES"
            ),
            VitrinaItem(
                nemo = "MSFT",
                nombre = "Microsoft Corporation",
                precio = "320.00",
                logo = "/logo.png",
                variacionDia = "-0.02",
                categoria = "ACCIONES"
            ),
            VitrinaItem(
                nemo = "NVDA",
                nombre = "NVIDIA Corporation",
                precio = "410.00",
                logo = "/logo.png",
                variacionDia = "0.05",
                categoria = "ACCIONES"
            ),
            VitrinaItem(
                nemo = "VOO",
                nombre = "Vanguard S&P 500 ETF",
                precio = "410.00",
                logo = "/logo.png",
                variacionDia = "0.02",
                categoria = "ETF"
            )
        )
        
        val filtered = allResults.filter { item ->
            item.nemo.lowercase().contains(busqueda.lowercase()) ||
            item.nombre.lowercase().contains(busqueda.lowercase())
        }
        
        return VitrinaSearchResponse(
            limite = 100,
            count = filtered.size,
            pagina = 1,
            next = null,
            previous = null,
            results = filtered
        )
    }
    
    suspend fun getVitrinaDetail(ticker: String): VitrinaDetailResponse {
        delay(300)
        
        // Retornar detalles según el ticker
        return when (ticker.uppercase()) {
            "AAPL" -> VitrinaDetailResponse(
                simbolo = "AAPL",
                nombre = "Apple Inc.",
                categoria = "ACCIONES",
                precio = "150.00",
                retornoPorcentaje = "0.01",
                retornoMonto = "1.00",
                descripcion = "Apple Inc. es una empresa tecnológica que fabrica y vende productos electrónicos y software.",
                apertura = "149.00",
                volumen = "12500000",
                marketCap = "2800000000000",
                precioMasAlto = "151.20",
                precioMasBajo = "148.75",
                maximoAnual = "198.23",
                sector = "Tecnología",
                industria = "Electrónica de consumo",
                montoMinimoDeOrden = "5.00",
                detalleCliente = ClienteDetalle(
                    disponibleParaOperarUsd = "100000.00",
                    cantidadAcciones = "100.02159248",
                    valorDeMercado = "100000.00",
                    costoBase = "100000.00",
                    gananciaOperdida = "0.00",
                    pesoPorcentaje = "25.50"
                )
            )
            "NVDA" -> VitrinaDetailResponse(
                simbolo = "NVDA",
                nombre = "NVIDIA Corporation",
                categoria = "ACCIONES",
                precio = "410.00",
                retornoPorcentaje = "0.05",
                retornoMonto = "20.00",
                descripcion = "NVIDIA Corporation es una empresa de semiconductores líder en inteligencia artificial.",
                apertura = "405.00",
                volumen = "45000000",
                marketCap = "3200000000000",
                precioMasAlto = "415.00",
                precioMasBajo = "408.00",
                maximoAnual = "520.00",
                sector = "Tecnología",
                industria = "Semiconductores",
                montoMinimoDeOrden = "5.00",
                detalleCliente = ClienteDetalle(
                    disponibleParaOperarUsd = "100000.00",
                    cantidadAcciones = "50.5",
                    valorDeMercado = "20705.00",
                    costoBase = "20000.00",
                    gananciaOperdida = "705.00",
                    pesoPorcentaje = "12.75"
                )
            )
            "ERROR" -> {
                throw Exception("MERCADO_CERRADO")
            }
            else -> VitrinaDetailResponse(
                simbolo = ticker.uppercase(),
                nombre = "Activo Desconocido",
                categoria = "ACCIONES",
                precio = "0.00",
                retornoPorcentaje = "0.00",
                retornoMonto = "0.00",
                descripcion = "Información no disponible",
                apertura = null,
                volumen = null,
                marketCap = null,
                precioMasAlto = null,
                precioMasBajo = null,
                maximoAnual = null,
                sector = null,
                industria = null,
                montoMinimoDeOrden = "5.00",
                detalleCliente = null
            )
        }
    }
    
    // MARK: - Order Endpoints
    
    suspend fun simulateOrder(request: OrdenSimulacionRequest): OrdenSimulacionResponse {
        delay(400)
        
        val precio = "410.01"
        val cantidad = request.cantidad ?: "1.0007"
        val monto = request.monto ?: "400.00"
        val comision = "1.05"
        val montoIva = "0.20"
        
        return OrdenSimulacionResponse(
            nemo = request.nemo,
            nombreInstrumento = "NVIDIA Corporation",
            categoria = "ACCIONES",
            lado = request.lado,
            precio = precio,
            cantidad = cantidad,
            monto = monto,
            comision = comision,
            montoIva = montoIva,
            ivaPorcentaje = "19.00",
            subtotalCobros = "1.25",
            montoEstimado = monto,
            totalEstimado = "401.25"
        )
    }
    
    suspend fun crearOrden(request: OrdenCrearRequest): OrdenCrearResponse {
        delay(500)
        
        if (request.nemo == "ERROR") {
             throw Exception("MERCADO_CERRADO")
        }

        // Generar UUID aleatorio para la orden
        val orderId = UUID.randomUUID().toString()
        val timestamp = Instant.now().toString()
        
        return OrdenCrearResponse(
            id = orderId,
            nemo = request.nemo,
            categoria = "ACCIONES",
            nombreInstrumento = if (request.nemo == "NVDA") "NVIDIA Corporation" else "Apple Inc.",
            lado = request.lado,
            cantidad = request.cantidad ?: "1.4",
            estado = "INGRESADA",
            monto = request.monto ?: "400.00",
            precioReferencia = "150.00",
            numeroDeCuenta = "VQBU000001",
            fechaRecepcion = timestamp
        )
    }
    
    // MARK: - Helper Methods
    
    fun getHeaders(): Map<String, String> {
        return mapOf(
            "customer-id" to customerId,
            "Channel" to "APP",
            "Request-Id" to UUID.randomUUID().toString()
        )
    }
    
    fun getOrderCreationHeaders(): Map<String, String> {
        val headers = getHeaders().toMutableMap()
        headers["Idempotency-Key"] = UUID.randomUUID().toString()
        return headers
    }
}
