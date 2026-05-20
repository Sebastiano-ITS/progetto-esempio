package com.example.esempiosimulazioneesercizio.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esempiosimulazioneesercizio.data.CartRepository
import com.example.esempiosimulazioneesercizio.data.Product
import com.example.esempiosimulazioneesercizio.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}

class ProductDetailViewModel : ViewModel() {
    private val apiService = ApiService.create()

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun fetchProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            try {
                val product = apiService.getProduct(productId)
                _uiState.value = ProductDetailUiState.Success(product)
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addToCart(product: Product) {
        CartRepository.addProduct(product)
    }
}
