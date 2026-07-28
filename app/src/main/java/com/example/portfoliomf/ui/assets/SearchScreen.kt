package com.example.portfoliomf.ui.assets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.data.models.VitrinaDto
import com.example.portfoliomf.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: AssetViewModel,
    onAssetClick: (String) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buscar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchAssets(it)
                },
                placeholder = { Text("Buscar...", color = GrayText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GrayText) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = ""; viewModel.searchAssets("") }) {
                            Icon(Icons.Default.Clear, contentDescription = null, tint = GrayText)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GrayText,
                    unfocusedBorderColor = DarkGray,
                    cursorColor = PrimaryGold,
                    focusedContainerColor = DarkGray,
                    unfocusedContainerColor = DarkGray
                )
            )

            if (viewModel.isSearching) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = PrimaryGold)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(viewModel.searchResults) { asset ->
                    SearchAssetItem(asset, onClick = { onAssetClick(asset.nemo) })
                    Divider(color = DarkGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun SearchAssetItem(asset: VitrinaDto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = asset.nemo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = asset.nombre,
                color = GrayText,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            val isPositive = !asset.variacionDia.startsWith("-")
            val color = if (isPositive) PositiveGreen else NegativeRed
            val sign = if (isPositive && asset.variacionDia != "0") "▲ " else if (!isPositive) "▼ " else ""
            
            Text(
                text = "${sign}${asset.variacionDia.replace("-", "")}%",
                color = color,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
