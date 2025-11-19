package com.nathaniel.carryapp.presentation.ui.compose.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Barangay
import com.nathaniel.carryapp.domain.model.City
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.model.Province
import com.nathaniel.carryapp.domain.request.DeliveryAddressMapper
import com.nathaniel.carryapp.domain.request.DeliveryAddressRequest
import com.nathaniel.carryapp.domain.usecase.BarangayResult
import com.nathaniel.carryapp.domain.usecase.CityResult
import com.nathaniel.carryapp.domain.usecase.GetAllProductsUseCase
import com.nathaniel.carryapp.domain.usecase.GetBarangaysByCityUseCase
import com.nathaniel.carryapp.domain.usecase.GetCitiesByProvinceUseCase
import com.nathaniel.carryapp.domain.usecase.GetProvincesByRegionUseCase
import com.nathaniel.carryapp.domain.usecase.ProductResult
import com.nathaniel.carryapp.domain.usecase.ProvinceResult
import com.nathaniel.carryapp.domain.usecase.SaveAddressUseCase
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REGION_IV_A = "040000000"

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val productsUseCase: GetAllProductsUseCase,
    private val getProvincesByRegionUseCase: GetProvincesByRegionUseCase,
    private val getCitiesByProvinceUseCase: GetCitiesByProvinceUseCase,
    private val getBarangaysByCityUseCase: GetBarangaysByCityUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selected = MutableStateFlow("Select Area")
    val selected: StateFlow<String> = _selected

    private val _regions = MutableStateFlow<List<String>>(emptyList())
    val regions: StateFlow<List<String>> = _regions

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _barangays = MutableStateFlow<List<Barangay>>(emptyList())
    val barangays: StateFlow<List<Barangay>> = _barangays


    var selectedProvince by mutableStateOf<Province?>(null)
        private set

    var selectedCity by mutableStateOf<City?>(null)
        private set

    var selectedBarangay by mutableStateOf<Barangay?>(null)
        private set

    var addressDetail by mutableStateOf("")
    var landmark by mutableStateOf("")

    init {
        checkLoginStatus()
        loadProducts()
        loadRegions()
        loadProvinces()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            when (val result = productsUseCase()) {
                is ProductResult.Success -> {
                    _products.value = result.products
                }

                is ProductResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    private fun loadRegions() {
        viewModelScope.launch {

            val regionCode = "040000000" // CALABARZON

            when (val result = getProvincesByRegionUseCase(regionCode)) {

                is ProvinceResult.Success -> {
                    _regions.value = result.provinces.map { it.name }
                }

                is ProvinceResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            when (val result = getProvincesByRegionUseCase(REGION_IV_A)) {
                is ProvinceResult.Success -> {
                    _provinces.value = result.provinces
                    _error.value = null
                }

                is ProvinceResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    fun onNext(deliveryAddressRequest: DeliveryAddressRequest, nav: NavController) {
        viewModelScope.launch {
            saveAddressUseCase.invoke(
                DeliveryAddressMapper.toEntity(deliveryAddressRequest)
            )
            nav.navigate(Routes.LOCATION_CONFIRMATION_SCREEN) {
                popUpTo(Routes.DELIVERY_ADDRESS) { inclusive = true }
            }
        }

    }

    fun onDeliveryAreaClick(nav: NavController) {
        nav.navigate(Routes.DELIVERY_ADDRESS)
    }

    fun onProvinceSelected(province: Province) {
        selectedProvince = province
        selectedCity = null
        selectedBarangay = null
        _cities.value = emptyList()
        _barangays.value = emptyList()

        loadCities(province.code)
    }

    private fun loadCities(provinceCode: String) {
        viewModelScope.launch {
            when (val result = getCitiesByProvinceUseCase(provinceCode)) {
                is CityResult.Success -> {
                    _cities.value = result.cities
                    _error.value = null
                }

                is CityResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    fun onCitySelected(city: City) {
        selectedCity = city
        selectedBarangay = null
        _barangays.value = emptyList()

        loadBarangays(city.code)
    }

    private fun loadBarangays(cityCode: String) {
        viewModelScope.launch {
            when (val result = getBarangaysByCityUseCase(cityCode)) {
                is BarangayResult.Success -> {
                    _barangays.value = result.barangays
                    _error.value = null
                }

                is BarangayResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }

    fun onBarangaySelected(barangay: Barangay) {
        selectedBarangay = barangay
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val login = apiRepository.getCurrentSession()
            _isLoggedIn.value = login != null
        }
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    // ---------- BUTTON ACTIONS ----------

    fun onHomeClick() {
        if (_isLoggedIn.value == true) {
            _navigateTo.value = Routes.ORDERS
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onCategoriesClick() {
        if (_isLoggedIn.value == true) {
            _navigateTo.value = Routes.DELIVERY_AREA // TODO CHANGE TO CATEGORIES LATER
        } else {
            _navigateTo.value = Routes.DELIVERY_AREA  // TODO CHANGE TO SIGNIN LATER
        }
    }

    fun onReorderClick() {
        if (_isLoggedIn.value == true) {
            //_navigateTo.value = Routes.REORDER
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onAccountClick() {
        if (_isLoggedIn.value == true) {
            _navigateTo.value = Routes.ACCOUNT
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }

    }

    fun onSearchClick() {
        if (_isLoggedIn.value == true) {
            // do nothing for now
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }

    fun onViewMoreClick() {
        if (_isLoggedIn.value == true) {
            // do nothing for now
        } else {
            _navigateTo.value = Routes.SIGN_IN
        }
    }


    fun select(area: String) {
        _selected.value = area
    }

}
