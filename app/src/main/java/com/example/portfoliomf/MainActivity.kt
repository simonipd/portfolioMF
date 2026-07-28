package com.example.portfoliomf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.portfoliomf.data.api.RetrofitClient
import com.example.portfoliomf.data.repository.PortfolioRepository
import com.example.portfoliomf.ui.assets.AssetDetailScreen
import com.example.portfoliomf.ui.assets.AssetViewModel
import com.example.portfoliomf.ui.assets.SearchScreen
import com.example.portfoliomf.ui.portfolio.PortfolioScreen
import com.example.portfoliomf.ui.portfolio.PortfolioViewModel
import com.example.portfoliomf.ui.theme.PortfolioMFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // RetrofitClient is added, which has the switch for Mock or Real Network in the tests.
        val apiService = RetrofitClient.apiService
        val repository = PortfolioRepository(apiService)
        val portfolioViewModel = PortfolioViewModel(repository)
        val assetViewModel = AssetViewModel(repository)

        setContent {
            PortfolioMFTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(portfolioViewModel, assetViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(portfolioViewModel: PortfolioViewModel, assetViewModel: AssetViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "portfolio") {
        composable("portfolio") {
            PortfolioScreen(
                viewModel = portfolioViewModel,
                onSearchClick = { navController.navigate("search") }
            )
        }
        composable("search") {
            SearchScreen(
                viewModel = assetViewModel,
                onAssetClick = { ticker ->
                    navController.navigate("asset_detail/$ticker")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "asset_detail/{ticker}",
            arguments = listOf(navArgument("ticker") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
            AssetDetailScreen(
                ticker = ticker,
                viewModel = assetViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
