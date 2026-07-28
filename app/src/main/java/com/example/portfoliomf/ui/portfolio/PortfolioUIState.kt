package com.example.portfoliomf.ui.portfolio

import com.example.portfoliomf.data.models.BalanceResponse
import com.example.portfoliomf.data.models.PortfolioSummary
import com.example.portfoliomf.data.models.PositionItem

data class PortfolioUIState(
    val portfolio: PortfolioSummary? = null,
    val balance: BalanceResponse? = null,
    val positions: List<PositionItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
