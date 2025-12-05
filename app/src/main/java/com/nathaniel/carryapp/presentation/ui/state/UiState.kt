package com.nathaniel.carryapp.presentation.ui.state

import android.net.Uri

/**
 * Used for login ui click event.
 */
sealed class LoginUiEvent {
    object OnHomeClicked : LoginUiEvent()
    object OnCategoriesClicked : LoginUiEvent()
    object OnReorderClicked : LoginUiEvent()
    object OnAccountClicked : LoginUiEvent()
    object OnSearchClicked : LoginUiEvent()
    object OnViewMoreClicked : LoginUiEvent()
}

/**
 * Identify route navigation login/order.
 */
sealed class LoginUiAction {
    data class Navigate(val route: String) : LoginUiAction()
    data class ShowToast(val message: String) : LoginUiAction()
}

/**
 * Used for customer ui event.
 */
sealed class CustomerUiEvent {
    object OnCustomerRegisterClicked : CustomerUiEvent()
}

/**
 * Identify navigation for customer.
 */
sealed class CustomerUiAction {
    data class Navigate(val route: String) : CustomerUiAction()
}

/**
 * For customer registration ui event.
 */
sealed class CustomerRegistrationUIEvent {
    data class OnUserNameChanged(val value: String) : CustomerRegistrationUIEvent()
    data class OnEmailChanged(val value: String) : CustomerRegistrationUIEvent()
    data class OnMobileChanged(val value: String) : CustomerRegistrationUIEvent()
    data class OnPhotoSelected(val uri: Uri) : CustomerRegistrationUIEvent()
}
