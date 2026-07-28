package com.example.portfoliomf.ui.assets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.ui.theme.DarkBackground
import com.example.portfoliomf.ui.theme.DarkGray
import com.example.portfoliomf.ui.theme.GrayText
import com.example.portfoliomf.ui.theme.NegativeRed
import com.example.portfoliomf.ui.theme.PositiveGreen
import com.example.portfoliomf.ui.theme.PrimaryGold

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
            CenterAlignedTopAppBar(
                title = { Text(ticker, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            if (details == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGold)
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = details.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = GrayText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "USD $${details.price}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isPositive = !details.returnPercentage.startsWith("-")
                    val color = if (isPositive) PositiveGreen else NegativeRed
                    Icon(
                        imageVector = if (isPositive) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${details.returnAmount} (${details.returnPercentage}%)",
                        color = color,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
                
                Text(
                    text = "RESUMEN",
                    style = MaterialTheme.typography.labelLarge,
                    color = GrayText,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = details.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkGray),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "COMPRAR",
                            style = MaterialTheme.typography.labelLarge,
                            color = GrayText,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            placeholder = { Text("Monto a invertir (USD)", color = GrayText) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGold,
                                unfocusedBorderColor = Color.DarkGray,
                                cursorColor = PrimaryGold,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = { viewModel.buyAsset(ticker, amount) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            enabled = amount.isNotBlank() && !viewModel.isPlacingOrder,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryGold,
                                contentColor = DarkBackground,
                                disabledContainerColor = PrimaryGold.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (viewModel.isPlacingOrder) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = DarkBackground)
                            } else {
                                Text("ORDENAR COMPRA", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                            }
                        }
                    }
                }

                if (viewModel.orderStatus != null) {
                    Text(
                        text = viewModel.orderStatus!!,
                        color = PositiveGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                
                if (viewModel.orderError != null) {
                    Text(
                        text = viewModel.orderError!!,
                        color = NegativeRed,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
