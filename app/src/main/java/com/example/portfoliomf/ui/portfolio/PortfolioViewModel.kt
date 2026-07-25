package com.example.portfoliomf.ui.portfolio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfoliomf.data.models.BalanceResponse
import com.example.portfoliomf.data.models.ResumenPortafolio
import com.example.portfoliomf.data.models.PosicionItem
import com.example.portfoliomf.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(private val repository: PortfolioRepository) : ViewModel() {

    var portfolioState by mutableStateOf<ResumenPortafolio?>(null)
    var balanceState by mutableStateOf<BalanceResponse?>(null)
    var positionsState by mutableStateOf<List<PosicionItem>>(emptyList())
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
                if (positionsDeferred.isSuccessful) positionsState = positionsDeferred.body()?.resultados ?: emptyList()

                if (!portfolioDeferred.isSuccessful || !balanceDeferred.isSuccessful || !positionsDeferred.isSuccessful) {
                    errorMessage = "Error al cargar algunos datos"
                }
            } catch (e: Exception) {
                errorMessage = "Error de red: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
