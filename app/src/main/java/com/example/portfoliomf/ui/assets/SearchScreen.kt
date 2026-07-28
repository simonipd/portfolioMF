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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.R
import com.example.portfoliomf.data.models.AssetDto
import com.example.portfoliomf.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: AssetViewModel,
    onAssetClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val searchUIState by viewModel.searchUIState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.portfoliomf_search_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.portfoliomf_back_content_description))
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
                .padding(horizontal = dimensionResource(R.dimen.portfoliomf_padding_medium))
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchAssets(it)
                },
                placeholder = { Text(stringResource(R.string.portfoliomf_search_placeholder), color = GrayText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.portfoliomf_padding_small)),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GrayText) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = ""; viewModel.searchAssets("") }) {
                            Icon(Icons.Default.Clear, contentDescription = null, tint = GrayText)
                        }
                    }
                },
                shape = RoundedCornerShape(dimensionResource(R.dimen.portfoliomf_corner_radius_medium)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GrayText,
                    unfocusedBorderColor = DarkGray,
                    cursorColor = PrimaryGold,
                    focusedContainerColor = DarkGray,
                    unfocusedContainerColor = DarkGray
                )
            )

            if (searchUIState.isSearching) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = PrimaryGold)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.portfoliomf_padding_small))
            ) {
                items(searchUIState.results) { asset ->
                    SearchAssetItem(asset, onClick = { onAssetClick(asset.ticker) })
                    Divider(color = DarkGray, thickness = dimensionResource(R.dimen.portfoliomf_divider_thickness))
                }
            }
        }
    }
}

@Composable
fun SearchAssetItem(asset: AssetDto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = dimensionResource(R.dimen.portfoliomf_padding_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = asset.ticker, fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_large).value.sp)
            Text(
                text = asset.name,
                color = GrayText,
                fontSize = dimensionResource(R.dimen.portfoliomf_font_size_small).value.sp,
                maxLines = 1
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            val isPositive = !asset.dayVariation.startsWith("-")
            val color = if (isPositive) PositiveGreen else NegativeRed
            val sign = if (isPositive && asset.dayVariation != "0") "▲ " else if (!isPositive) "▼ " else ""
            
            Text(
                text = "${sign}${asset.dayVariation.replace("-", "")}%",
                color = color,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(R.dimen.portfoliomf_font_size_medium).value.sp,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.portfoliomf_padding_medium))
            )
            
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(dimensionResource(R.dimen.portfoliomf_icon_size_medium))
            )
        }
    }
}
