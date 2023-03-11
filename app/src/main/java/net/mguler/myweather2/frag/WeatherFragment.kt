package net.mguler.myweather2.frag

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mguler.myweather2.model.NextHours
import net.mguler.myweather2.model.SelectedCity
import net.mguler.myweather2.R
import net.mguler.myweather2.databinding.FragmentWeatherBinding
import net.mguler.myweather2.model.NextHoursAdapter
import net.mguler.myweather2.service.MyWeatherAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherFragment : Fragment() {

    private lateinit var sp: SharedPreferences
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<WeatherFragmentArgs>()
    private val nextHoursList = ArrayList<NextHours>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = requireActivity().getSharedPreferences("selected", Context.MODE_PRIVATE)
        //sp.edit().putString("location", "0:0").apply()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cLSpinner.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_editFragment) }

        var selectedCity = args.selectedCity
        if (selectedCity == null) {

            val spSelectedCity = sp.getString("location", "Globe:0:0")!!
            val listSelectedCity = spSelectedCity.split(":")

            val cityName = listSelectedCity[0]
            val cityLat = listSelectedCity[1].toDouble()
            val cityLng = listSelectedCity[2].toDouble()
            selectedCity = SelectedCity(cityName, cityLat, cityLng)
        }
        else {
            val prefStr = "${selectedCity.cityName}:${selectedCity.lat}:${selectedCity.lng}"
            sp.edit().putString("location", prefStr).apply()
        }

        getDataWithLatLng(selectedCity)

    }


    // TODO: getIdentifier coil integration
    private fun getDataWithLatLng(selectedCity: SelectedCity) {
        val api = ""
        val baseurl = "https://api.openweathermap.org/data/2.5/"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyWeatherAPI::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getWeatherData(api, selectedCity.lat, selectedCity.lng, "metric")

            print(selectedCity.lat)
            withContext(Dispatchers.Main){
                val res = response.body()

                if (selectedCity.cityName == "Custom") {
                    binding.textCityName.text = res?.city?.name
                }
                else {
                    binding.textCityName.text = selectedCity.cityName
                }
                binding.textCurrentDegree.text = String.format("%.0f", res?.list?.get(0)?.main?.temp)
                binding.textCurrentHumidity.text = res?.list?.get(0)?.main?.humidity.toString()
                binding.textCurrentWind.text = res?.list?.get(0)?.wind?.speed.toString()
                binding.textCurrentWeather.text = res?.list?.get(0)?.weather?.get(0)?.main

                val icon = res?.list?.get(0)?.weather?.get(0)?.icon
                val mainIconRes = resources.getIdentifier("img" + icon, "drawable", requireContext().packageName)
                binding.imageMainIcon.setImageResource(mainIconRes)


                for (i in 1..15) {
                    val nextTemp = res?.list?.get(i)?.main?.temp
                    val nextHour = res?.list?.get(i)?.dtTxt
                    val nextIcon = res?.list?.get(i)?.weather?.get(0)?.icon

                    val nextHours = NextHours(nextTemp, nextHour, nextIcon)
                    nextHoursList.add(nextHours)

                }

                binding.recylerHome.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recylerHome.adapter =
                    NextHoursAdapter(requireContext(), nextHoursList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
