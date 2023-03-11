package net.mguler.myweather2.service

import com.google.gson.annotations.SerializedName

data class PlaceSuggestResponse(

	@field:SerializedName("features")
	val features: List<FeaturesItem?>? = null,

	@field:SerializedName("query")
	val query: Query? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Datasource(

	@field:SerializedName("license")
	val license: String? = null,

	@field:SerializedName("attribution")
	val attribution: String? = null,

	@field:SerializedName("sourcename")
	val sourcename: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Rank(

	@field:SerializedName("importance")
	val importance: Any? = null,

	@field:SerializedName("confidence")
	val confidence: Double? = null,

	@field:SerializedName("match_type")
	val matchType: String? = null,

	@field:SerializedName("confidence_city_level")
	val confidenceCityLevel: Double? = null
)

data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<Any?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Properties(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("result_type")
	val resultType: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("formatted")
	val formatted: String? = null,

	@field:SerializedName("timezone")
	val timezone: Timezone? = null,

	@field:SerializedName("county")
	val county: String? = null,

	@field:SerializedName("postcode")
	val postcode: String? = null,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("address_line2")
	val addressLine2: String? = null,

	@field:SerializedName("address_line1")
	val addressLine1: String? = null,

	@field:SerializedName("datasource")
	val datasource: Datasource? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("suburb")
	val suburb: String? = null,

	@field:SerializedName("rank")
	val rank: Rank? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null,

	@field:SerializedName("village")
	val village: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class FeaturesItem(

	@field:SerializedName("bbox")
	val bbox: List<Any?>? = null,

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("properties")
	val properties: Properties? = null
)

data class Timezone(

	@field:SerializedName("offset_DST")
	val offsetDST: String? = null,

	@field:SerializedName("offset_DST_seconds")
	val offsetDSTSeconds: Int? = null,

	@field:SerializedName("offset_STD_seconds")
	val offsetSTDSeconds: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("offset_STD")
	val offsetSTD: String? = null
)

data class Query(

	@field:SerializedName("text")
	val text: String? = null
)
