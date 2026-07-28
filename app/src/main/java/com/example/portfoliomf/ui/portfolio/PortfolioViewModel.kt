package com.example.portfoliomf.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfoliomf.data.api.NetworkResult
import com.example.portfoliomf.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PortfolioViewModel(private val repository: PortfolioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioUIState())
    val uiState: StateFlow<PortfolioUIState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // In a real app, we might use combine or zip if we want to wait for all
                val portfolioResult = repository.getPortfolio()
                val balanceResult = repository.getBalance()
                val positionsResult = repository.getPositions()

                _uiState.update { currentState ->
                    var newState = currentState.copy(isLoading = false)
                    
                    when (portfolioResult) {
                        is NetworkResult.Success -> newState = newState.copy(portfolio = portfolioResult.data)
                        is NetworkResult.Error -> newState = newState.copy(errorMessage = portfolioResult.message)
                        else -> {}
                    }
                    
                    when (balanceResult) {
                        is NetworkResult.Success -> newState = newState.copy(balance = balanceResult.data)
                        is NetworkResult.Error -> newState = newState.copy(errorMessage = balanceResult.message)
                        else -> {}
                    }
                    
                    when (positionsResult) {
                        is NetworkResult.Success -> newState = newState.copy(positions = positionsResult.data.results)
                        is NetworkResult.Error -> newState = newState.copy(errorMessage = positionsResult.message)
                        else -> {}
                    }
                    
                    newState
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Unexpected error: ${e.message}") }
            }
        }
    }
}
