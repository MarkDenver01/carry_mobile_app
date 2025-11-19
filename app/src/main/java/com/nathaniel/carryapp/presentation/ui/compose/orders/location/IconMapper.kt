package com.nathaniel.carryapp.presentation.ui.compose.orders.location

import com.nathaniel.carryapp.R

object IconMapper {
    fun provinceIcon(name: String): Int {
        return when (name.lowercase()) {
            "metro manila" -> R.drawable.ic_batangas
            "batangas" -> R.drawable.ic_batangas
            "bulacan" -> R.drawable.ic_bulacan
            "cavite" -> R.drawable.ic_cavite
            "laguna" -> R.drawable.ic_laguna
            "rizal" -> R.drawable.ic_rizal
            else -> R.drawable.ic_batangas   // fallback
        }
    }
}