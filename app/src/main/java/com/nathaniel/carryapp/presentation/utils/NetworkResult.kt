package com.nathaniel.carryapp.presentation.utils

import com.nathaniel.carryapp.domain.enum.HttpStatus

/**
 * Network result
 *
 * @param T
 * @property status
 * @property data
 * @property message
 * @constructor Create empty Network result
 */
sealed class NetworkResult<T>(
    val status: HttpStatus? = null,
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Success
     *
     * @param T
     * @constructor
     *
     * @param status
     * @param data
     */
    class Success<T>(status: HttpStatus, data: T) : NetworkResult<T>(status, data)

    /**
     * Error
     *
     * @param T
     * @constructor
     *
     * @param status
     * @param message
     */
    class Error<T>(status: HttpStatus, message: String) : NetworkResult<T>(status, null, message)

    /**
     * Loading
     *
     * @param T
     * @constructor Create empty Loading
     */
    class Loading<T> : NetworkResult<T>()
}