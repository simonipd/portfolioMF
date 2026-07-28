package com.example.portfoliomf.ui.portfolio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.data.models.PositionItem
import com.example.portfoliomf.ui.theme.DarkBackground
import com.example.portfoliomf.ui.theme.DarkGray
import com.example.portfoliomf.ui.theme.GrayText
import com.example.portfoliomf.ui.theme.PositiveGreen
import com.example.portfoliomf.ui.theme.PrimaryGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel,
    onSearchClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Portfolio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                actions = {
                    Text("USD / CLP", color = PrimaryGold, modifier = Modifier.padding(end = 16.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            PortfolioBottomBar(onSearchClick)
        },
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                PortfolioValueHeader(
                    value = viewModel.portfolioState?.portfolioValue ?: "40,433.00",
                    change = "7.06 (0.04%) Today"
                )
                Spacer(modifier = Modifier.height(32.dp))
                PerformanceChart()
                Spacer(modifier = Modifier.height(24.dp))
                TimeRangeSelector()
                Spacer(modifier = Modifier.height(40.dp))
                BuyingPowerSection()
                Spacer(modifier = Modifier.height(40.dp))
                PositionsHeader()
            }

            items(viewModel.positionsState) { position ->
                PositionItemPremium(position)
                Divider(color = DarkGray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun PortfolioValueHeader(value: String, change: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("USD $$value", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("▲", color = PositiveGreen, fontSize = 12.sp)
            Spacer(Modifier.width(4.dp))
            Text("$$change", color = PositiveGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PerformanceChart() {
    Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.7f)
            lineTo(size.width * 0.2f, size.height * 0.5f)
            lineTo(size.width * 0.4f, size.height * 0.8f)
            lineTo(size.width * 0.6f, size.height * 0.3f)
            lineTo(size.width * 0.8f, size.height * 0.4f)
            lineTo(size.width, size.height * 0.1f)
        }
        drawPath(path = path, color = PrimaryGold, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
fun TimeRangeSelector() {
    val ranges = listOf("1D", "1W", "1M", "3M", "1Y", "5Y", "YTD")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ranges.forEach { range ->
            val isSelected = range == "YTD"
            Text(
                text = range,
                color = if (isSelected) Color.White else GrayText,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = if (isSelected) Modifier.background(DarkGray, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp) else Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun BuyingPowerSection() {
    Column {
        Text("Buying Power", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            BuyingPowerItem("US DOLLARS", "$40,455.21", Modifier.weight(1f))
            BuyingPowerItem("CHILEAN PESO", "$42,788.54", Modifier.weight(1f))
        }
    }
}

@Composable
fun BuyingPowerItem(label: String, value: String, modifier: Modifier) {
    Column(modifier) {
        Text(label, color = GrayText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PositionsHeader() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Positions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("DAY VALUE", color = GrayText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowDown, null, tint = GrayText, modifier = Modifier.size(16.dp))
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun PositionItemPremium(position: PositionItem) {
    Row(Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(position.ticker, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(position.name, color = GrayText, fontSize = 12.sp)
        }
        Text("$$${position.currentValue}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun PortfolioBottomBar(onSearchClick: () -> Unit) {
    NavigationBar(containerColor = DarkBackground, tonalElevation = 0.dp) {
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.ShoppingCart, "Trade") }, label = { Text("Trade") })
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Menu, "Wallet") }, label = { Text("Wallet") })
        NavigationBarItem(selected = false, onClick = onSearchClick, icon = { Icon(Icons.Default.Search, "Search") }, label = { Text("Search") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.AccountCircle, "Account") }, label = { Text("Account") })
    }
}
