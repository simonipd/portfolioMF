package com.example.portfoliomf.ui.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfoliomf.data.api.NetworkResult
import com.example.portfoliomf.data.models.*
import com.example.portfoliomf.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class AssetViewModel(private val repository: PortfolioRepository) : ViewModel() {

    private val _searchUIState = MutableStateFlow(SearchUIState())
    val searchUIState: StateFlow<SearchUIState> = _searchUIState.asStateFlow()

    private val _detailState = MutableStateFlow<AssetDetailState>(AssetDetailState.Loading)
    val detailState: StateFlow<AssetDetailState> = _detailState.asStateFlow()

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    fun searchAssets(query: String) {
        if (query.isBlank()) {
            _searchUIState.update { it.copy(results = emptyList(), isSearching = false) }
            return
        }
        viewModelScope.launch {
            _searchUIState.update { it.copy(isSearching = true, errorMessage = null) }
            when (val result = repository.searchAssets(query)) {
                is NetworkResult.Success -> {
                    _searchUIState.update { it.copy(results = result.data.results, isSearching = false) }
                }
                is NetworkResult.Error -> {
                    _searchUIState.update { it.copy(isSearching = false, errorMessage = result.message) }
                }
                else -> {}
            }
        }
    }

    fun getAssetDetails(ticker: String) {
        viewModelScope.launch {
            _detailState.value = AssetDetailState.Loading
            when (val result = repository.getAssetDetails(ticker)) {
                is NetworkResult.Success -> {
                    _detailState.value = AssetDetailState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _detailState.value = AssetDetailState.Error(result.message ?: "Unknown error")
                }
                else -> {}
            }
        }
    }

    fun buyAsset(ticker: String, amount: String) {
        viewModelScope.launch {
            _orderState.value = OrderState.Processing
            val idempotencyKey = UUID.randomUUID().toString()
            val request = OrderRequest(ticker = ticker, side = "BUY", amount = amount)
            
            when (val result = repository.createOrder(idempotencyKey, request)) {
                is NetworkResult.Success -> {
                    _orderState.value = OrderState.Success(result.data.id)
                }
                is NetworkResult.Error -> {
                    val errorMessage = if (result.message?.contains("MARKET_CLOSED") == true) {
                        "MARKET_CLOSED"
                    } else {
                        result.message ?: "ERROR_${result.code}"
                    }
                    _orderState.value = OrderState.Error(errorMessage)
                }
                else -> {}
            }
        }
    }
    
    fun clearOrderState() {
        _orderState.value = OrderState.Idle
    }
}
