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
                    path.endsWith("/accounts/portfolio") -> {
                        val period = request.url.queryParameter("period") ?: "YTD"
                        gson.toJson(mockService.getPortfolio(period))
                    }
                    path.endsWith("/accounts/balance") -> {
                        gson.toJson(mockService.getBalance())
                    }
                    path.endsWith("/accounts/positions") -> {
                        gson.toJson(mockService.getPositions())
                    }
                    path.endsWith("/showcase") && method == "GET" -> {
                        val query = request.url.queryParameter("search") ?: ""
                        gson.toJson(mockService.searchAssets(query))
                    }
                    path.contains("/showcase/") -> {
                        val ticker = path.substringAfterLast("/")
                        gson.toJson(mockService.getAssetDetail(ticker))
                    }
                    path.endsWith("/orders/create") && method == "POST" -> {
                        responseCode = 201
                        // Simplified for mock
                        gson.toJson(mockService.createOrder(OrderCreateRequest("AAPL", "BUY")))
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
                errorCode = "MOCK_ERROR",
                errorMessage = "Error in mock service: ${e.message}"
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

        // Simulate latency
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
