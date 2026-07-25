package com.example.portfoliomf.ui.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.data.models.PosicionItem
import com.example.portfoliomf.ui.theme.PositiveGreen
import com.example.portfoliomf.ui.theme.NegativeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel,
    onSearchClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Portafolio") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (viewModel.errorMessage != null) {
                Text(text = viewModel.errorMessage!!, color = Color.Red)
                Button(onClick = { viewModel.loadData() }) {
                    Text("Reintentar")
                }
            } else {
                PortfolioHeader(
                    totalValue = viewModel.portfolioState?.valorPortafolio ?: "0.00",
                    performance = viewModel.portfolioState?.variacionIntradayPorcentaje ?: "0.00"
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Tus Activos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.positionsState) { position ->
                        PositionItem(position)
                    }
                }
            }
        }
    }
}

@Composable
fun PortfolioHeader(totalValue: String, performance: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Valor Total", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "$$totalValue",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            val perfValue = performance.toDoubleOrNull() ?: 0.0
            val perfColor = if (perfValue >= 0) PositiveGreen else NegativeRed
            val sign = if (perfValue >= 0) "+" else ""
            Text(
                text = "$sign$performance%",
                color = perfColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun PositionItem(position: PosicionItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = position.nemo, fontWeight = FontWeight.Bold)
                Text(text = position.nombre, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "$${position.valorActual}",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
