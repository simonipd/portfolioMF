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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfoliomf.R
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
                title = { Text(stringResource(R.string.portfoliomf_portfolio_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                actions = {
                    Text(
                        text = stringResource(R.string.portfoliomf_currency_pair),
                        color = PrimaryGold,
                        modifier = Modifier.padding(end = dimensionResource(R.dimen.portfoliomf_padding_medium)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                .padding(horizontal = dimensionResource(R.dimen.portfoliomf_screen_horizontal_padding))
        ) {
            item {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.portfoliomf_header_spacing)))
                PortfolioValueHeader(
                    value = viewModel.portfolioState?.portfolioValue ?: "40,433.00",
                    change = stringResource(R.string.portfoliomf_today_change_format, "7.06", "0.04%", stringResource(R.string.portfoliomf_today))
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.portfoliomf_padding_extra_large)))
                PerformanceChart()
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.portfoliomf_padding_large)))
                TimeRangeSelector()
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.portfoliomf_padding_huge)))
                BuyingPowerSection()
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.portfoliomf_padding_huge)))
                PositionsHeader()
            }

            items(viewModel.positionsState) { position ->
                PositionItemPremium(position)
                Divider(color = DarkGray, thickness = dimensionResource(R.dimen.portfoliomf_divider_thickness))
            }
        }
    }
}

@Composable
fun PortfolioValueHeader(value: String, change: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("USD $$value", fontSize = dimensionResource(R.dimen.portfoliomf_font_size_header).value.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("▲", color = PositiveGreen, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_small).value.sp)
            Spacer(Modifier.width(dimensionResource(R.dimen.portfoliomf_padding_small)))
            Text(change, color = PositiveGreen, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_medium).value.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PerformanceChart() {
    val chartHeight = dimensionResource(R.dimen.portfoliomf_chart_height)
    Canvas(modifier = Modifier.fillMaxWidth().height(chartHeight)) {
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
                fontSize = dimensionResource(R.dimen.portfoliomf_font_size_small).value.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = if (isSelected) Modifier.background(DarkGray, RoundedCornerShape(dimensionResource(R.dimen.portfoliomf_corner_radius_small))).padding(horizontal = dimensionResource(R.dimen.portfoliomf_padding_small), vertical = dimensionResource(R.dimen.portfoliomf_padding_small) / 2) else Modifier.padding(dimensionResource(R.dimen.portfoliomf_padding_small) / 2)
            )
        }
    }
}

@Composable
fun BuyingPowerSection() {
    Column {
        Text(stringResource(R.string.portfoliomf_buying_power), color = Color.White, fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_title).value.sp)
        Spacer(Modifier.height(dimensionResource(R.dimen.portfoliomf_header_spacing)))
        Row(Modifier.fillMaxWidth()) {
            BuyingPowerItem(stringResource(R.string.portfoliomf_us_dollars), "$40,455.21", Modifier.weight(1f))
            BuyingPowerItem(stringResource(R.string.portfoliomf_chilean_peso), "$42,788.54", Modifier.weight(1f))
        }
    }
}

@Composable
fun BuyingPowerItem(label: String, value: String, modifier: Modifier) {
    Column(modifier) {
        Text(label, color = GrayText, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_caption).value.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_large).value.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PositionsHeader() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.portfoliomf_positions), color = Color.White, fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_title).value.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.portfoliomf_day_value), color = GrayText, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_caption).value.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowDown, null, tint = GrayText, modifier = Modifier.size(dimensionResource(R.dimen.portfoliomf_icon_size_small)))
        }
    }
    Spacer(Modifier.height(dimensionResource(R.dimen.portfoliomf_padding_small)))
}

@Composable
fun PositionItemPremium(position: PositionItem) {
    Row(Modifier.fillMaxWidth().padding(vertical = dimensionResource(R.dimen.portfoliomf_padding_medium)), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(position.ticker, color = Color.White, fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_large).value.sp)
            Text(position.name, color = GrayText, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_small).value.sp)
        }
        Text("$$${position.currentValue}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.portfoliomf_font_size_large).value.sp)
    }
}

@Composable
fun PortfolioBottomBar(onSearchClick: () -> Unit) {
    NavigationBar(containerColor = DarkBackground, tonalElevation = dimensionResource(R.dimen.portfoliomf_nav_bar_elevation)) {
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.ShoppingCart, stringResource(R.string.portfoliomf_nav_trade)) }, label = { Text(stringResource(R.string.portfoliomf_nav_trade)) })
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Menu, stringResource(R.string.portfoliomf_nav_wallet)) }, label = { Text(stringResource(R.string.portfoliomf_nav_wallet)) })
        NavigationBarItem(selected = false, onClick = onSearchClick, icon = { Icon(Icons.Default.Search, stringResource(R.string.portfoliomf_nav_search)) }, label = { Text(stringResource(R.string.portfoliomf_nav_search)) })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.AccountCircle, stringResource(R.string.portfoliomf_nav_account)) }, label = { Text(stringResource(R.string.portfoliomf_nav_account)) })
    }
}
