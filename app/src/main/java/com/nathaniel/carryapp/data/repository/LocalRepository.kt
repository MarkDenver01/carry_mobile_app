package com.nathaniel.carryapp.data.repository

import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.domain.datasource.AddressLocalDataSource
import com.nathaniel.carryapp.domain.datasource.CartLocalDataSource
import com.nathaniel.carryapp.domain.datasource.LoginLocalDataSource
import com.nathaniel.carryapp.domain.mapper.CustomerDetailsMapper
import com.nathaniel.carryapp.domain.model.CartSummary
import com.nathaniel.carryapp.domain.model.ShopProduct
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val addressLocalDataSource: AddressLocalDataSource,
    private val loginLocalDataSource: LoginLocalDataSource,
    private val cartLocalDataSource: CartLocalDataSource,
    private val tokenManager: TokenManager
) {

    private var products: List<ShopProduct> = emptyList()

    fun saveUserSession(session: Boolean) {
        tokenManager.userSession(session)
    }

    fun getUserSession(): Boolean = tokenManager.getUserSession()

    suspend fun saveCustomerDetails(customerDetailRequest: CustomerDetailRequest) {
        loginLocalDataSource.saveCustomerDetails(customerDetailRequest)
    }

    suspend fun getCustomerDetails(): CustomerDetailRequest? {
        val entity: CustomerDetailsEntity? = loginLocalDataSource.getCustomerDetails()
        return entity?.let { CustomerDetailsMapper.fromEntity(it) }
    }

    fun saveMobileOrEmail(mobileOrEmail: String) {
        tokenManager.saveMobileOrEmail(mobileOrEmail)
    }

    fun getMobileOrEmail(): String? = tokenManager.getMobileOrEmail()

    suspend fun saveAddress(address: DeliveryAddressEntity) {
        addressLocalDataSource.save(address)
    }

    suspend fun getAddress(): DeliveryAddressEntity? = addressLocalDataSource.get()

    suspend fun clearAddress() = addressLocalDataSource.clear()

    suspend fun updateAddress(
        provinceName: String,
        cityName: String,
        barangayName: String,
        addressDetail: String
    ) {
        addressLocalDataSource.updateAddressFields(
            provinceName,
            cityName,
            barangayName,
            addressDetail
        )
    }

    fun setProducts(list: List<ShopProduct>) {
        products = list
    }

    suspend fun add(productId: Long) {
        cartLocalDataSource.addItem(productId)
    }

    suspend fun remove(productId: Long) {
        cartLocalDataSource.removeLatest(productId)
    }

    suspend fun getSummary(): List<CartSummary> {
        val grouped = cartLocalDataSource.getGroupedItems()

        return grouped.mapNotNull { g ->
            val p = products.find { it.id.toLong() == g.productId } ?: return@mapNotNull null

            val price = p.price.replace("₱", "").toDouble()

            CartSummary(
                productId = g.productId,
                name = p.name,
                imageUrl = p.imageUrl,
                weight = p.weight,
                price = price,
                qty = g.qty,
                subtotal = price * g.qty
            )
        }
    }

    suspend fun getTotal(): Double = getSummary().sumOf { it.subtotal }

    suspend fun getCartCount(): Int = cartLocalDataSource.getTotalCount()
}