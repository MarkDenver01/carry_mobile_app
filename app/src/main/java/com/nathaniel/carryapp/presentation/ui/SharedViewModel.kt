package com.nathaniel.carryapp.presentation.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
inline fun <reified VM : ViewModel> sharedViewModel(): VM {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
        ?: throw IllegalStateException("sharedViewModel must be called within an Activity context")

    return ViewModelProvider(activity)[VM::class.java]
}
