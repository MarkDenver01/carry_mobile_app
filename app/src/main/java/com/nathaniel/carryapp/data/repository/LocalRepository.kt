package com.nathaniel.carryapp.data.repository

import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginSessionEntity
import com.nathaniel.carryapp.domain.datasource.AddressDatasource
import com.nathaniel.carryapp.domain.datasource.AgreementDatasource
import com.nathaniel.carryapp.domain.datasource.CartDatasource
import com.nathaniel.carryapp.domain.datasource.LoginDatasource
import com.nathaniel.carryapp.domain.mapper.CustomerDetailsMapper
import com.nathaniel.carryapp.domain.model.ShopProduct
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.presentation.ui.compose.orders.CartSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.math.log

class LocalRepository @Inject constructor(
    private val addressDataSource: AddressDatasource,
    private val loginDataSource: LoginDatasource,
    private val cartDataSource: CartDatasource,
    private val agreementDatasource: AgreementDatasource,
    private val tokenManager: TokenManager
) {
    private val _products = MutableStateFlow<List<ShopProduct>>(emptyList())
    fun getProductsFlow(): StateFlow<List<ShopProduct>> = _products

    fun saveUserSession(session: Boolean) {
        tokenManager.userSession(session)
    }

    fun getUserSession(): Boolean = tokenManager.getUserSession()

    suspend fun saveCustomerDetails(customerDetailRequest: CustomerDetailRequest) {
        loginDataSource.saveCustomerDetails(customerDetailRequest)
    }

    suspend fun getCustomerDetails(): CustomerDetailRequest? {
        val entity: CustomerDetailsEntity? = loginDataSource.getCustomerDetails()
        return entity?.let { CustomerDetailsMapper.fromEntity(it) }
    }

    fun saveMobileOrEmail(mobileOrEmail: String) {
        tokenManager.saveMobileOrEmail(mobileOrEmail)
    }

    fun getMobileOrEmail(): String? = tokenManager.getMobileOrEmail()

    suspend fun saveAddress(address: DeliveryAddressEntity) {
        addressDataSource.save(address)
    }

    suspend fun getAddress(): DeliveryAddressEntity? = addressDataSource.get()

    suspend fun clearAddress() = addressDataSource.clear()

    suspend fun saveAgreement(agreement: AgreementTermsEntity) {
        agreementDatasource.insertAgreement(agreement)
    }

    suspend fun isVerifiedAgreement(email: String): Boolean? =
        agreementDatasource.isVerified(email)

    suspend fun clearAgreementStatus() = agreementDatasource.clearAgreementStatus()

    suspend fun saveLoginSession(session: LoginSessionEntity) {
        loginDataSource.saveLoginSession(session)
    }

    suspend fun isLoggedIn(email: String): Boolean? = loginDataSource.isLoggedIn(email)

    suspend fun deleteLoginSession() = loginDataSource.deleteLoginSession()

    suspend fun updateAddress(
        provinceName: String,
        cityName: String,
        barangayName: String,
        addressDetail: String
    ) {
        addressDataSource.updateAddressFields(
            provinceName,
            cityName,
            barangayName,
            addressDetail
        )
    }

    fun setProducts(list: List<ShopProduct>) {
        _products.value = list
    }

    suspend fun add(productId: Long) {
        cartDataSource.addItem(productId)
    }

    suspend fun remove(productId: Long) {
        cartDataSource.removeLatest(productId)
    }

    suspend fun getCartCount(): Int = cartDataSource.getTotalCount()

    suspend fun getCartGroups(): List<CartSummary> {
        return cartDataSource.getGroupedItems().map { g ->
            CartSummary(productId = g.productId, qty = g.qty)
        }
    }

    suspend fun clearAll() = cartDataSource.clearAll()
}