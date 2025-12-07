package com.nathaniel.carryapp.presentation.ui.compose.orders.reorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity
import com.nathaniel.carryapp.data.repository.LocalRepository
import com.nathaniel.carryapp.domain.usecase.AddToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReorderViewModel @Inject constructor(
    private val repository: LocalRepository,
    private val cartUseCase: AddToCartUseCase
) : ViewModel() {

    val history = repository.getOrderHistory().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    /** ✅ Restore per-category (already working) */
    fun reorderAgain(items: List<ReorderEntity>) {
        viewModelScope.launch {
            items.forEach { item ->
                repeat(item.qty) {
                    cartUseCase(item.productId)
                }
            }
        }
    }

    /** ✅ NEW: RESTORE ALL WITH EXACT QUANTITIES */
    fun restoreAllWithQuantities() {
        viewModelScope.launch {
            history.value.forEach { item ->
                repeat(item.qty) {
                    cartUseCase(item.productId)
                }
            }
        }
    }
}
