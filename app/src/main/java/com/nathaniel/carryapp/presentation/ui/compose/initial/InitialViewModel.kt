package com.nathaniel.carryapp.presentation.ui.compose.initial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor() : ViewModel() {

    private val _navigateToDashboard = MutableSharedFlow<Unit>()
    val navigateToDashboard = _navigateToDashboard.asSharedFlow()

    fun onGetStartedClicked() {
        viewModelScope.launch {
            Log.d("TST", "Get started clicked.")
            _navigateToDashboard.emit(Unit)
        }
    }
}