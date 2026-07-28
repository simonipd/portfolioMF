package com.example.portfoliomf.ui.portfolio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfoliomf.data.models.BalanceResponse
import com.example.portfoliomf.data.models.PortfolioSummary
import com.example.portfoliomf.data.models.PositionItem
import com.example.portfoliomf.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(private val repository: PortfolioRepository) : ViewModel() {

    var portfolioState by mutableStateOf<PortfolioSummary?>(null)
    var balanceState by mutableStateOf<BalanceResponse?>(null)
    var positionsState by mutableStateOf<List<PositionItem>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val portfolioDeferred = repository.getPortfolio()
                val balanceDeferred = repository.getBalance()
                val positionsDeferred = repository.getPositions()

                if (portfolioDeferred.isSuccessful) portfolioState = portfolioDeferred.body()
                if (balanceDeferred.isSuccessful) balanceState = balanceDeferred.body()
                if (positionsDeferred.isSuccessful) positionsState = positionsDeferred.body()?.results ?: emptyList()

                if (!portfolioDeferred.isSuccessful || !balanceDeferred.isSuccessful || !positionsDeferred.isSuccessful) {
                    errorMessage = "Error loading some data"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
