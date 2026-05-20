package com.example.esempiosimulazioneesercizio.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CartRepository {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    fun addProduct(product: Product) {
        _items.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(product)
            }
        }
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    fun getTotalPrice(): Double {
        return _items.value.sumOf { it.subtotal }
    }

    fun getTotalItems(): Int {
        return _items.value.sumOf { it.quantity }
    }
}
