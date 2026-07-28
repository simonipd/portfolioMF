package com.example.portfoliomf.ui.assets

import com.example.portfoliomf.data.models.AssetDetailDto
import com.example.portfoliomf.data.models.AssetDto

sealed class AssetDetailState {
    object Loading : AssetDetailState()
    data class Success(val details: AssetDetailDto) : AssetDetailState()
    data class Error(val message: String) : AssetDetailState()
}

sealed class OrderState {
    object Idle : OrderState()
    object Processing : OrderState()
    data class Success(val orderId: String) : OrderState()
    data class Error(val message: String) : OrderState()
}

data class SearchUIState(
    val results: List<AssetDto> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)
