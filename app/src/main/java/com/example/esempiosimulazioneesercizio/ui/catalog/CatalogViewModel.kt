package com.example.esempiosimulazioneesercizio.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esempiosimulazioneesercizio.data.CartRepository
import com.example.esempiosimulazioneesercizio.data.Product
import com.example.esempiosimulazioneesercizio.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CatalogUiState {
    object Loading : CatalogUiState()
    data class Success(val products: List<Product>) : CatalogUiState()
    data class Error(val message: String) : CatalogUiState()
}

class CatalogViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    val cartCount: StateFlow<Int> = CartRepository.items
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = CatalogUiState.Loading
            try {
                val products = apiService.getProducts()
                _uiState.value = CatalogUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = CatalogUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addToCart(product: Product) {
        CartRepository.addProduct(product)
    }
}
