package com.example.data.models

data class NepalLocation(
    val id: String,
    val nameEn: String,
    val nameNe: String,
    val nameRom: String,
    val address: String,
    val city: String,
    val lat: Double,
    val lng: Double,
    val isPopular: Boolean = true
) {
    fun getDisplayName(language: String): String {
        return when (language) {
            "ne" -> nameNe
            "ne-rom" -> nameRom
            else -> nameEn
        }
    }
}

object NepalLocationsPreset {
    val presets = listOf(
        NepalLocation(
            id = "thamel",
            nameEn = "Thamel",
            nameNe = "ठमेल",
            nameRom = "Thamel",
            address = "Thamel Marg, Ward 26, Kathmandu",
            city = "Kathmandu",
            lat = 27.7154,
            lng = 85.3123
        ),
        NepalLocation(
            id = "airport",
            nameEn = "Tribhuvan Int'l Airport (TIA)",
            nameNe = "त्रिभुवन अन्तर्राष्ट्रिय विमानस्थल",
            nameRom = "Airport (TIA)",
            address = "Ring Road, Airport Gate, Kathmandu",
            city = "Kathmandu",
            lat = 27.6966,
            lng = 85.3591
        ),
        NepalLocation(
            id = "durbarmarg",
            nameEn = "Durbar Marg / Kingsway",
            nameNe = "दरबारमार्ग",
            nameRom = "Durbar Marg",
            address = "Durbar Marg, Kathmandu",
            city = "Kathmandu",
            lat = 27.7107,
            lng = 85.3175
        ),
        NepalLocation(
            id = "patan",
            nameEn = "Patan Durbar Square",
            nameNe = "पाटन दरबार क्षेत्र",
            nameRom = "Patan Durbar Square",
            address = "Mangal Bazar, Lalitpur",
            city = "Lalitpur",
            lat = 27.6744,
            lng = 85.3260
        ),
        NepalLocation(
            id = "bhaktapur",
            nameEn = "Bhaktapur Durbar Square",
            nameNe = "भक्तपुर दरबार क्षेत्र",
            nameRom = "Bhaktapur Durbar",
            address = "Durbar Square, Bhaktapur",
            city = "Bhaktapur",
            lat = 27.6722,
            lng = 85.4281
        ),
        NepalLocation(
            id = "kalanki",
            nameEn = "Kalanki Chowk",
            nameNe = "कलंकी चोक",
            nameRom = "Kalanki Chowk",
            address = "Prithvi Highway Junction, Kathmandu",
            city = "Kathmandu",
            lat = 27.6934,
            lng = 85.2818
        ),
        NepalLocation(
            id = "koteshwor",
            nameEn = "Koteshwor Chowk",
            nameNe = "कोटेश्वर चोक",
            nameRom = "Koteshwor",
            address = "Araniko Highway, Kathmandu",
            city = "Kathmandu",
            lat = 27.6780,
            lng = 85.3473
        ),
        NepalLocation(
            id = "swayambhu",
            nameEn = "Swayambhunath (Monkey Temple)",
            nameNe = "स्वयम्भूनाथ",
            nameRom = "Swayambhunath",
            address = "Swayambhu Hill, Kathmandu",
            city = "Kathmandu",
            lat = 27.7149,
            lng = 85.2904
        ),
        NepalLocation(
            id = "bouddha",
            nameEn = "Bouddhanath Stupa",
            nameNe = "बौद्धनाथ स्तूप",
            nameRom = "Bouddha Stupa",
            address = "Bouddha, Kathmandu",
            city = "Kathmandu",
            lat = 27.7215,
            lng = 85.3620
        ),
        NepalLocation(
            id = "pokhara_lakeside",
            nameEn = "Lakeside, Pokhara",
            nameNe = "लेकसाइड, पोखरा",
            nameRom = "Lakeside Pokhara",
            address = "Phewa Lake Marg, Pokhara",
            city = "Pokhara",
            lat = 28.2096,
            lng = 83.9592
        ),
        NepalLocation(
            id = "chitwan_sauraha",
            nameEn = "Sauraha, Chitwan",
            nameNe = "सौराहा, चितवन",
            nameRom = "Sauraha Chitwan",
            address = "National Park Gate, Sauraha",
            city = "Chitwan",
            lat = 27.5833,
            lng = 84.4967
        )
    )

    fun calculateDistanceKm(loc1: NepalLocation, loc2: NepalLocation): Double {
        val latDiff = (loc1.lat - loc2.lat) * 111.0
        val lngDiff = (loc1.lng - loc2.lng) * 98.0
        val dist = Math.sqrt(latDiff * latDiff + lngDiff * lngDiff)
        return if (dist < 1.2) 2.5 else Math.round(dist * 10.0) / 10.0
    }
}
