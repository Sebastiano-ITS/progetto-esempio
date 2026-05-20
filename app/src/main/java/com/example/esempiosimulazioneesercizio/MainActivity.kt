package com.example.esempiosimulazioneesercizio

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.esempiosimulazioneesercizio.ui.cart.CartScreen
import com.example.esempiosimulazioneesercizio.ui.catalog.CatalogScreen
import com.example.esempiosimulazioneesercizio.ui.detail.ProductDetailScreen
import com.example.esempiosimulazioneesercizio.ui.theme.EsempioSimulazioneEsercizioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EsempioSimulazioneEsercizioTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "catalog") {
        composable("catalog") {
            CatalogScreen(
                onProductClick = { product ->
                    navController.navigate("detail/${product.id}")
                },
                onGoToCart = {
                    navController.navigate("cart")
                }
            )
        }
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            ProductDetailScreen(
                productId = productId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("cart") {
            CartScreen(
                onBack = { navController.popBackStack() },
                onConfirmOrder = {
                    Toast.makeText(context, "Ordine confermato!", Toast.LENGTH_LONG).show()
                    navController.popBackStack("catalog", inclusive = false)
                }
            )
        }
    }
}
