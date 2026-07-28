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

    var searchResults by mutableStateOf<List<AssetDto>>(emptyList())
    var selectedAssetDetails by mutableStateOf<AssetDetailDto?>(null)
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

    fun buyAsset(ticker: String, amount: String) {
        viewModelScope.launch {
            isPlacingOrder = true
            orderStatus = null
            orderError = null
            try {
                val idempotencyKey = UUID.randomUUID().toString()
                val request = OrderRequest(ticker = ticker, side = "BUY", amount = amount)
                val response = repository.createOrder(idempotencyKey, request)
                
                if (response.isSuccessful) {
                    orderStatus = "Order Successful! ID: ${response.body()?.id}"
                } else {
                    val errorBody = response.errorBody()?.string()
                    orderError = if (errorBody?.contains("MARKET_CLOSED") == true) {
                        "Error: The market is currently closed."
                    } else {
                        "Error processing order: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                orderError = "Connection error: ${e.message}"
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
