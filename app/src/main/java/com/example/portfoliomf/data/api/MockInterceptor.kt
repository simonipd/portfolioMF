package com.example.portfoliomf.data.api

import com.example.portfoliomf.data.api.mock.*
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {
    private val gson = Gson()
    private val mockService = PACEMockAPIService.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        if (NetworkConfig.USE_REAL_NETWORK) {
            return chain.proceed(request)
        }

        val uri = request.url.toUri()
        val path = uri.path
        val method = request.method

        var responseString = ""
        var responseCode = 200

        try {
            val result = runBlocking {
                when {
                    path.endsWith("/cuentas/portafolio") -> {
                        val periodo = request.url.queryParameter("periodo") ?: "YTD"
                        gson.toJson(mockService.getPortafolio(periodo))
                    }
                    path.endsWith("/cuentas/saldo") -> {
                        gson.toJson(mockService.getSaldo())
                    }
                    path.endsWith("/cuentas/posiciones") -> {
                        gson.toJson(mockService.getPosiciones())
                    }
                    path.endsWith("/vitrina") && method == "GET" -> {
                        val busqueda = request.url.queryParameter("busqueda") ?: ""
                        gson.toJson(mockService.searchVitrina(busqueda))
                    }
                    path.contains("/vitrina/") -> {
                        val ticker = path.substringAfterLast("/")
                        gson.toJson(mockService.getVitrinaDetail(ticker))
                    }
                    path.endsWith("/ordenes/crear") && method == "POST" -> {
                        responseCode = 201
                        gson.toJson(mockService.crearOrden(OrdenCrearRequest("AAPL", "COMPRA")))
                    }
                    else -> {
                        null
                    }
                }
            }
            
            if (result == null) {
                return chain.proceed(request)
            } else {
                responseString = result
            }
            
        } catch (e: Exception) {
            val errorResponse = ErrorResponse(
                codigoError = e.message ?: "UNKNOWN_ERROR",
                mensajeError = "Error in mock service: ${e.message}"
            )
            return Response.Builder()
                .code(400)
                .message("Bad Request")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .addHeader("content-type", "application/json")
                .body(gson.toJson(errorResponse).toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        // Simular latencia
        Thread.sleep(300)

        return Response.Builder()
            .code(responseCode)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .addHeader("content-type", "application/json")
            .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}
