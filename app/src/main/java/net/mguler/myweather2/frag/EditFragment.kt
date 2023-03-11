package net.mguler.myweather2.frag

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mguler.myweather2.R
import net.mguler.myweather2.databinding.FragmentEditBinding
import net.mguler.myweather2.model.SelectedCity
import net.mguler.myweather2.service.PlaceSuggestAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private var selectedCity: SelectedCity? = null

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val permCoarse = Manifest.permission.ACCESS_COARSE_LOCATION
    private val permFine = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerUserLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.autoTextCities.addTextChangedListener {
            if ( it.toString().length > 2) { getCitySuggestion(it.toString()) }
        }

        binding.fabAdd.setOnClickListener {
            val gotoWeatherFragment = EditFragmentDirections.actionEditFragmentToWeatherFragment(selectedCity)
            findNavController().navigate(gotoWeatherFragment)
        }

        binding.floatingActionButton.setOnClickListener {
            requestPermission(it)
        }

    }

    private fun requestPermission(v: View) {
        val permOK = PackageManager.PERMISSION_GRANTED
        when {
            ContextCompat.checkSelfPermission(requireContext(), permCoarse) == permOK -> {
                getUserLocation()
            }
            shouldShowRequestPermissionRationale(permCoarse) -> {
                Snackbar.make(v, "Permission Needed!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give It!") { permissionLauncher.launch(permCoarse) }.show()
            }
            else -> {
                permissionLauncher.launch(permCoarse)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { loc ->
                selectedCity = SelectedCity("Custom", loc.latitude, loc.longitude)
                val gotoWeatherFragment = EditFragmentDirections.actionEditFragmentToWeatherFragment(selectedCity)
                findNavController().navigate(gotoWeatherFragment)
            }
            .addOnFailureListener { e -> println(e.localizedMessage) }
    }

    @SuppressLint("MissingPermission")
    private fun registerUserLocation() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                when (it) {
                    true -> { getUserLocation() }
                    else -> {
                        Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    /*
    @SuppressLint("MissingPermission")
    private fun getLocationWithManager() {
        // TODO: one time update

        val locationManager = requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, object :
            LocationListener {

            override fun onLocationChanged(l: Location) {
                //val userLocationE = SelectedCity("Custom", l.latitude, l.longitude)
                //myIntent.putExtra("latLng", userLocationE)
                //myIntent.putExtra("city", "custom")
                //startActivity(myIntent)
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {
                Toast.makeText(requireContext(), "GPS Disabled!", Toast.LENGTH_SHORT).show()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        })
    }
     */


    private fun getCitySuggestion(text: String) {
        val baseurl = "https://api.geoapify.com/v1/geocode/"
        val api = ""
        val type = "city"
        val bias = "countrycode:tr"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceSuggestAPI::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            val citiesList = ArrayList<SelectedCity>()
            val cityNamesList = ArrayList<String>()


            val response = retrofit.getPlaceSuggestion(text, type, bias, api)

            withContext(Dispatchers.Main) {
                val res = response.body()

                res?.features?.forEach { item ->
                    val suggestionItem = item?.properties?.formatted.toString()
                    val suggestionLat = item?.properties?.lat as Double
                    val suggestionLng = item.properties.lon as Double

                    val city = SelectedCity(suggestionItem, suggestionLat, suggestionLng)
                    citiesList.add(city)
                    cityNamesList.add(suggestionItem)
                }

                if (citiesList.isNotEmpty()) {
                    val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cityNamesList)
                    binding.autoTextCities.setAdapter(citiesAdapter)
                    binding.autoTextCities.showDropDown()
                }

                binding.autoTextCities.setOnItemClickListener { _, _, i, _ ->
                    selectedCity = citiesList[i]
                    // TODO: tıklamaz tamamını yazarsa
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
