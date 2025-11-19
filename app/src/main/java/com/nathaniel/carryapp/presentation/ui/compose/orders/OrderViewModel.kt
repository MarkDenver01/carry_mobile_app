package com.nathaniel.carryapp.presentation.ui.compose.orders

import android.location.Location
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.data.repository.GeocodedAddress
import com.nathaniel.carryapp.domain.mapper.CustomerDetailsMapper
import com.nathaniel.carryapp.domain.model.Barangay
import com.nathaniel.carryapp.domain.model.City
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.model.Province
import com.nathaniel.carryapp.domain.request.CustomerRegistrationRequest
import com.nathaniel.carryapp.domain.request.DeliveryAddressMapper
import com.nathaniel.carryapp.domain.request.DeliveryAddressRequest
import com.nathaniel.carryapp.domain.usecase.BarangayResult
import com.nathaniel.carryapp.domain.usecase.CityResult
import com.nathaniel.carryapp.domain.usecase.ForwardGeocodeUseCase
import com.nathaniel.carryapp.domain.usecase.GeocodeResult
import com.nathaniel.carryapp.domain.usecase.GetAddressUseCase
import com.nathaniel.carryapp.domain.usecase.GetAllProductsUseCase
import com.nathaniel.carryapp.domain.usecase.GetBarangaysByCityUseCase
import com.nathaniel.carryapp.domain.usecase.GetCitiesByProvinceUseCase
import com.nathaniel.carryapp.domain.usecase.GetCurrentLocationUseCase
import com.nathaniel.carryapp.domain.usecase.GetCustomerDetailsUseCase
import com.nathaniel.carryapp.domain.usecase.GetMobileOrEmailUseCase
import com.nathaniel.carryapp.domain.usecase.GetProvincesByRegionUseCase
import com.nathaniel.carryapp.domain.usecase.ProductResult
import com.nathaniel.carryapp.domain.usecase.ProvinceResult
import com.nathaniel.carryapp.domain.usecase.ReverseGeocodeUseCase
import com.nathaniel.carryapp.domain.usecase.SaveAddressUseCase
import com.nathaniel.carryapp.domain.usecase.SaveCustomerDetailsUseCase
import com.nathaniel.carryapp.domain.usecase.SaveMobileOrEmailUseCase
import com.nathaniel.carryapp.domain.usecase.UpdateAddressUseCase
import com.nathaniel.carryapp.domain.usecase.UpdateCustomerUseCase
import com.nathaniel.carryapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val REGION_IV_A = "040000000"

data class CustomerUIState(
    val userName: String = "",
    val email: String = "",
    val isEmailDisabled: Boolean = false,

    val mobileNumber: String = "",
    val isMobileDisabled: Boolean = false,

    val address: String = "",
    val photoUri: Uri? = null
)

sealed class CustomerRegistrationUIEvent {
    data class OnUserNameChanged(val value: String) : CustomerRegistrationUIEvent()
    data class OnEmailChanged(val value: String) : CustomerRegistrationUIEvent()
    data class OnMobileChanged(val value: String) : CustomerRegistrationUIEvent()
    data class OnPhotoSelected(val uri: Uri) : CustomerRegistrationUIEvent()
}

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val productsUseCase: GetAllProductsUseCase,
    private val getProvincesByRegionUseCase: GetProvincesByRegionUseCase,
    private val getCitiesByProvinceUseCase: GetCitiesByProvinceUseCase,
    private val getBarangaysByCityUseCase: GetBarangaysByCityUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val forwardGeocodeUseCase: ForwardGeocodeUseCase,
    private val reverseGeocodeUseCase: ReverseGeocodeUseCase,
    private val getAddressUseCase: GetAddressUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val getMobileOrEmailUseCase: GetMobileOrEmailUseCase,
    private val saveMobileOrEmailUseCase: SaveMobileOrEmailUseCase,
    private val saveCustomerDetailsUseCase: SaveCustomerDetailsUseCase,
    private val getCustomerDetailsUseCase: GetCustomerDetailsUseCase,
    private val updateCustomerUseCase: UpdateCustomerUseCase,
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

    private val _state = MutableStateFlow("")
    val state = _state

    private val _selectedLatLng = MutableStateFlow<LatLng?>(null)
    val selectedLatLng: StateFlow<LatLng?> = _selectedLatLng

    private val _loadAddressLocal = MutableStateFlow<String?>(null)
    val loadAddressLocal: StateFlow<String?> = _loadAddressLocal

    private val _reverseAddress = MutableStateFlow(
        GeocodedAddress(
            fullAddressLine = "",
            province = "",
            city = "",
            barangay = ""
        )
    )
    val reverseAddress: StateFlow<GeocodedAddress> = _reverseAddress

    private val _pinMoveMode = MutableStateFlow(false)
    val pinMoveMode: StateFlow<Boolean> = _pinMoveMode

    private val _uiState = MutableStateFlow(CustomerUIState())
    val uiState: StateFlow<CustomerUIState> = _uiState

    init {
        checkLoginStatus()
        loadProducts()
        loadRegions()
        loadProvinces()
        loadSavedAddress()
        loadSavedMobileOrEmail()
    }

    private fun loadSavedMobileOrEmail() {
        val saved = getMobileOrEmailUseCase()

        if (!saved.isNullOrBlank()) {

            if (saved.contains("@")) {
                // It's an email
                _uiState.update {
                    it.copy(
                        email = saved,
                        isEmailDisabled = true,
                        mobileNumber = "",
                        isMobileDisabled = false
                    )
                }
            } else {
                // It's a mobile number
                _uiState.update {
                    it.copy(
                        mobileNumber = saved,
                        isMobileDisabled = true,
                        email = "",
                        isEmailDisabled = false
                    )
                }
            }
        }
    }


    private fun loadSavedAddress() {
        viewModelScope.launch {

            val saved = getAddressUseCase()

            if (saved != null) {

                // 1. USE THE SAVED ADDRESS TEXT AS GEOCODING INPUT
                val fullAddress = saved.addressDetail ?: ""

                // 2. Convert text → LatLng
                val geocodeResult = forwardGeocodeUseCase(fullAddress)

                if (geocodeResult is GeocodeResult.Success) {

                    // 3. THIS is the TRUE starting point of the map
                    _selectedLatLng.value = geocodeResult.data

                } else {

                    // OPTIONAL fallback if geocode fails
                    _selectedLatLng.value = LatLng(14.085, 121.146)
                }

                // 4. Load structured address FROM ROOM (no API call)
                _reverseAddress.value = GeocodedAddress(
                    fullAddressLine = saved.addressDetail,
                    province = saved.provinceName,
                    city = saved.cityName,
                    barangay = saved.barangayName
                )

                _uiState.update {
                    it.copy(
                        address = listOf(
                            saved.addressDetail,
                            saved.barangayName,
                            saved.cityName,
                            saved.provinceName
                        ).filter { s -> s.isNotBlank() }.joinToString(", ")
                    )
                }

                // 5. UI flag (optional)
                _loadAddressLocal.value = saved.addressDetail
            }
        }
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
        val a = _isLoggedIn.value
        Timber.e("xxxxxxx: $a" )
        if (_isLoggedIn.value == true) {
            _navigateTo.value = Routes.CATEGORIES
        } else {
            _navigateTo.value = Routes.SIGN_IN
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

    fun searchCoordinates(address: String) {
        viewModelScope.launch {
            when (val result = forwardGeocodeUseCase(address)) {
                is GeocodeResult.Success -> _state.value = "LatLng: ${result.data}"
                is GeocodeResult.Error -> _state.value = result.message
            }
        }
    }

    fun searchAddress(lat: Double, lng: Double) {
        viewModelScope.launch {
            when (val result = reverseGeocodeUseCase(lat, lng)) {
                is GeocodeResult.Success -> _state.value = "Address: ${result.data.fullAddressLine}"
                is GeocodeResult.Error -> _state.value = result.message
            }
        }
    }


    fun initLocation(lat: Double, lng: Double) {
        val pos = LatLng(lat, lng)
        _selectedLatLng.value = pos
        reverseLookup(pos)
    }

    fun updatePin(newPos: LatLng) {
        _selectedLatLng.value = newPos
        reverseLookup(newPos)
    }

    private fun reverseLookup(pos: LatLng) {
        viewModelScope.launch {
            when (val result = reverseGeocodeUseCase(pos.latitude, pos.longitude)) {
                is GeocodeResult.Success -> {
                    _reverseAddress.value = result.data
                }

                is GeocodeResult.Error -> {
                    _reverseAddress.value = _reverseAddress.value.copy(
                        fullAddressLine = "Unable to fetch address"
                    )
                }
            }
        }
    }

    fun confirmAddress(
        deliveryAddressRequest: DeliveryAddressRequest,
        navController: NavController
    ) {
        viewModelScope.launch {
            updateAddressUseCase.invoke(
                deliveryAddressRequest.provinceName,
                deliveryAddressRequest.cityName,
                deliveryAddressRequest.barangayName,
                deliveryAddressRequest.addressDetails
            )
            navController.navigate(Routes.CUSTOMER_DETAIL) {
                popUpTo(Routes.DELIVERY_ADDRESS) { inclusive = true }
            }
        }
    }

    fun triggerPinMoveMode() {
        _pinMoveMode.value = true
    }

    fun loadCurrentLocation() {
        viewModelScope.launch {

            // Step 1: Get device location
            val loc = getCurrentLocationUseCase()

            if (loc != null) {

                // Step 2: Convert to LatLng
                val latLng = LatLng(loc.latitude, loc.longitude)

                // Step 3: Move the pin on the map
                _selectedLatLng.value = latLng

                // Step 4: Reverse geocode → update address fields
                when (val result = reverseGeocodeUseCase(latLng.latitude, latLng.longitude)) {
                    is GeocodeResult.Success -> {
                        _reverseAddress.value = result.data
                    }

                    is GeocodeResult.Error -> {
                        _reverseAddress.value = GeocodedAddress(
                            fullAddressLine = "Unable to fetch address",
                            province = "",
                            city = "",
                            barangay = ""
                        )
                    }
                }

            } else {
                // OPTIONAL: fallback if no GPS
                _reverseAddress.value = GeocodedAddress(
                    fullAddressLine = "GPS unavailable",
                    province = "",
                    city = "",
                    barangay = ""
                )
            }
        }
    }

    fun onEvent(event: CustomerRegistrationUIEvent) {
        when (event) {

            is CustomerRegistrationUIEvent.OnUserNameChanged ->
                _uiState.update { it.copy(userName = event.value) }

            is CustomerRegistrationUIEvent.OnEmailChanged ->
                _uiState.update { ui ->
                    if (ui.isEmailDisabled) ui // ignore changes
                    else ui.copy(email = event.value)
                }

            is CustomerRegistrationUIEvent.OnMobileChanged ->
                _uiState.update { ui ->
                    if (ui.isMobileDisabled) ui // ignore changes
                    else ui.copy(mobileNumber = event.value)
                }

            is CustomerRegistrationUIEvent.OnPhotoSelected ->
                _uiState.update { it.copy(photoUri = event.uri) }
        }
    }


    fun submitCustomer(customerRegistrationRequest: CustomerRegistrationRequest) = viewModelScope.launch {
        Timber.d("submit customer...")
//        val details = CustomerDetailsMapper.toCustomerRequest(customerRegistrationRequest)
//
//        val identifier = details.mobileNumber.ifBlank { details.email }
//        val response = updateCustomerUseCase(identifier, details)
//        if (response.isSuccessful) {
//            saveCustomerDetailsUseCase.invoke(details) // saved to local
//            _navigateTo.value = Routes.ORDERS
//        }
        _navigateTo.value = Routes.ORDERS
    }


}
