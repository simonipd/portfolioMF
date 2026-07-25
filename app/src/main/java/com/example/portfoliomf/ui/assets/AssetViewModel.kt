package com.example.portfoliomf.ui.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfoliomf.data.models.*
import com.example.portfoliomf.data.repository.PortfolioRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AssetViewModel(private val repository: PortfolioRepository) : ViewModel() {

    var searchResults by mutableStateOf<List<VitrinaDto>>(emptyList())
    var selectedAssetDetails by mutableStateOf<VitrinaDetailDto?>(null)
    var isSearching by mutableStateOf(false)
    var orderStatus by mutableStateOf<String?>(null)
    var orderError by mutableStateOf<String?>(null)
    var isPlacingOrder by mutableStateOf(false)

    fun searchAssets(query: String) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }
        viewModelScope.launch {
            isSearching = true
            try {
                val response = repository.searchAssets(query)
                if (response.isSuccessful) {
                    searchResults = response.body()?.results ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isSearching = false
            }
        }
    }

    fun getAssetDetails(ticker: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAssetDetails(ticker)
                if (response.isSuccessful) {
                    selectedAssetDetails = response.body()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun buyAsset(nemo: String, monto: String) {
        viewModelScope.launch {
            isPlacingOrder = true
            orderStatus = null
            orderError = null
            try {
                val idempotencyKey = UUID.randomUUID().toString()
                val request = OrdenRequest(nemo = nemo, lado = "COMPRA", monto = monto)
                val response = repository.createOrder(idempotencyKey, request)
                
                if (response.isSuccessful) {
                    orderStatus = "¡Orden Exitosa! ID: ${response.body()?.id}"
                } else {
                    val errorBody = response.errorBody()?.string()
                    orderError = if (errorBody?.contains("MERCADO_CERRADO") == true) {
                        "Error: El mercado se encuentra cerrado actualmente."
                    } else {
                        "Error al procesar la orden: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                orderError = "Error de conexión: ${e.message}"
            } finally {
                isPlacingOrder = false
            }
        }
    }
    
    fun clearOrderState() {
        orderStatus = null
        orderError = null
    }
}
