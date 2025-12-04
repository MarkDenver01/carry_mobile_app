package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.CustomerDao
import com.nathaniel.carryapp.data.local.room.dao.DriverDao
import com.nathaniel.carryapp.data.local.room.dao.LoginDao
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginSessionEntity
import com.nathaniel.carryapp.domain.datasource.LoginDatasource
import com.nathaniel.carryapp.domain.mapper.CustomerDetailsMapper
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import javax.inject.Inject
import kotlin.math.log

class LoginDatasourceImpl @Inject constructor(
    private val loginDao: LoginDao,
    private val customerDao: CustomerDao,
    private val driverDao: DriverDao
) : LoginDatasource {
    override suspend fun saveLoginSession(entity: LoginSessionEntity) {
        loginDao.insertLoginSession(entity)
    }

    override suspend fun isLoggedIn(email: String): Boolean? {
        return loginDao.isLoggedIn(email)
    }

    override suspend fun deleteLoginSession() {
        loginDao.deleteLoginSession()
    }

    override suspend fun saveLogin(
        login: LoginEntity,
        customer: CustomerEntity?,
        driver: DriverEntity?
    ) {
        loginDao.insertLogin(login)
        customer?.let { customerDao.insertCustomer(it) }
        driver?.let { driverDao.insertDriver(it) }
    }

    override suspend fun saveCustomerDetails(customerDetailRequest: CustomerDetailRequest?) {
        customerDetailRequest?.let {
            val entity = CustomerDetailsMapper.toEntity(customerDetailRequest)
            customerDao.insertCustomerDetails(entity)
        }
    }

    override suspend fun getCustomerDetails(): CustomerDetailsEntity? {
        return customerDao.getCustomerDetails()
    }

    override suspend fun getCurrentLogin(): LoginEntity? {
        return loginDao.getCurrentLogin()
    }

    override suspend fun getCustomerSession() =
        loginDao.getLoginWithCustomer()

    override suspend fun getDriverSession() =
        loginDao.getLoginWithDriver()

    override suspend fun logout() {
        loginDao.clearLogin()
        customerDao.clearCustomers()
        driverDao.clearDrivers()
    }
}
