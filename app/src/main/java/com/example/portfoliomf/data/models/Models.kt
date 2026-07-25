package com.example.portfoliomf.data.models

import com.google.gson.annotations.SerializedName

// --- Portafolio y Saldos ---

data class ResumenPortafolio(
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

data class BalanceResponse(
    val disponibleParaOperarUsd: String,
    val disponibleParaRetirarUsd: String,
    val balanceUsd: String,
    val actualizadoEn: String
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

data class PaginatedPosicionesResponse(
    val limite: Int,
    val conteo: Int,
    val pagina: Int,
    val resultados: List<PosicionItem>
)

// --- Vitrina (Búsqueda y Detalle) ---

data class VitrinaDto(
    val nemo: String,
    val nombre: String,
    val precio: String,
    val logo: String,
    val variacionDia: String,
    val categoria: String
)

data class PaginatedVitrinaResponse(
    val limite: Int,
    val count: Int,
    val pagina: Int,
    val results: List<VitrinaDto>
)

data class VitrinaDetailDto(
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
    val detalleCliente: DetalleCliente?
)

data class DetalleCliente(
    val disponibleParaOperarUsd: String,
    val cantidadAcciones: String?,
    val valorDeMercado: String?,
    val costoBase: String?,
    val gananciaOperdida: String?
)

// --- Órdenes ---

data class OrdenRequest(
    val nemo: String,
    val lado: String, // "COMPRA" o "VENTA"
    val monto: String? = null,
    val cantidad: String? = null
)

data class OrdenResponse(
    val id: String,
    val nemo: String,
    val nombreInstrumento: String,
    val estado: String,
    val monto: String?,
    val cantidad: String,
    val fechaRecepcion: String
)

// --- Errores ---

data class ErrorDto(
    val codigoError: String,
    val mensajeError: String,
    val campos: Map<String, List<String>>? = null
)
