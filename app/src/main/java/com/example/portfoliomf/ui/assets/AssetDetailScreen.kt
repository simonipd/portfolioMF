package com.example.portfoliomf.ui.assets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.ui.theme.PositiveGreen
import com.example.portfoliomf.ui.theme.NegativeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    ticker: String,
    viewModel: AssetViewModel,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    val details = viewModel.selectedAssetDetails

    LaunchedEffect(ticker) {
        viewModel.getAssetDetails(ticker)
        viewModel.clearOrderState()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ticker) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            if (details == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Text(text = details.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(text = "$${details.precio}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = "Sobre la empresa", fontWeight = FontWeight.Bold)
                Text(text = details.descripcion, style = MaterialTheme.typography.bodyMedium)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Divider()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Comprar Activo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto a invertir (USD)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { viewModel.buyAsset(ticker, amount) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = amount.isNotBlank() && !viewModel.isPlacingOrder
                ) {
                    if (viewModel.isPlacingOrder) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Confirmar Compra")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (viewModel.orderStatus != null) {
                    Text(text = viewModel.orderStatus!!, color = PositiveGreen, fontWeight = FontWeight.Bold)
                }
                
                if (viewModel.orderError != null) {
                    Text(text = viewModel.orderError!!, color = NegativeRed, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.weight(1f))

                // Botón para simular error de mercado cerrado (usando ticker "ERROR" como trigger en FakeApi)
                TextButton(
                    onClick = { viewModel.buyAsset("ERROR", "100") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Simular Mercado Cerrado", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
