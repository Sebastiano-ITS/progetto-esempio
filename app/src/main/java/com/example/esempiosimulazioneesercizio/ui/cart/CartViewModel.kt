package com.example.esempiosimulazioneesercizio.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esempiosimulazioneesercizio.data.CartItem
import com.example.esempiosimulazioneesercizio.data.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel : ViewModel() {
    val items: StateFlow<List<CartItem>> = CartRepository.items

    val totalPrice: StateFlow<Double> = CartRepository.items
        .map { it.sumOf { item -> item.subtotal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalItems: StateFlow<Int> = CartRepository.items
        .map { it.sumOf { item -> item.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun clearCart() {
        CartRepository.clearCart()
    }
}
