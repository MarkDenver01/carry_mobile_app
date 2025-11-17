package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.CustomerDao
import com.nathaniel.carryapp.data.local.room.dao.DriverDao
import com.nathaniel.carryapp.data.local.room.dao.LoginDao
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.domain.datasource.LoginLocalDataSource
import javax.inject.Inject

class LoginLocalDataSourceImpl @Inject constructor(
    private val loginDao: LoginDao,
    private val customerDao: CustomerDao,
    private val driverDao: DriverDao
) : LoginLocalDataSource {

    override suspend fun saveLogin(
        login: LoginEntity,
        customer: CustomerEntity?,
        driver: DriverEntity?
    ) {
        loginDao.insertLogin(login)
        customer?.let { customerDao.insertCustomer(it) }
        driver?.let { driverDao.insertDriver(it) }
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
