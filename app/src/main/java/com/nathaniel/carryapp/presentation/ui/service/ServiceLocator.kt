package com.nathaniel.carryapp.presentation.ui.service

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.data.repository.LocalRepository

object ServiceLocator {
    lateinit var apiRepository: ApiRepository
    lateinit var localRepository: LocalRepository
}