package com.nathaniel.carryapp.presentation.ui.service

import com.nathaniel.carryapp.data.repository.ApiRepository

object ServiceLocator {
    lateinit var apiRepository: ApiRepository
}