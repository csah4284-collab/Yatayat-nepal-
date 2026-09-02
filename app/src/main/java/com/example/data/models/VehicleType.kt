package com.example.data.models

enum class VehicleType(
    val titleEn: String,
    val titleNe: String,
    val titleRom: String,
    val subtitleEn: String,
    val subtitleNe: String,
    val subtitleRom: String,
    val baseFareNpr: Double,
    val perKmRateNpr: Double,
    val capacity: Int,
    val etaMins: Int,
    val iconKey: String
) {
    BIKE(
        titleEn = "Bike",
        titleNe = "बाइक / स्कुटर",
        titleRom = "Bike / Scooter",
        subtitleEn = "Fastest single ride",
        subtitleNe = "छिटो र सस्तो एकल यात्रा",
        subtitleRom = "Chhito ra sasto ride",
        baseFareNpr = 50.0,
        perKmRateNpr = 18.0,
        capacity = 1,
        etaMins = 3,
        iconKey = "bike"
    ),
    AUTO(
        titleEn = "Auto Rickshaw",
        titleNe = "अटो रिक्सा / टेम्पो",
        titleRom = "Auto / Tempo",
        subtitleEn = "Affordable for 3 people",
        subtitleNe = "३ जनासम्म सहज र सस्तो",
        subtitleRom = "3 jana samma sasto travel",
        baseFareNpr = 90.0,
        perKmRateNpr = 28.0,
        capacity = 3,
        etaMins = 5,
        iconKey = "auto"
    ),
    CAR(
        titleEn = "Cab / Taxi",
        titleNe = "ट्याक्सी / कार",
        titleRom = "Taxi / Car",
        subtitleEn = "AC Comfort, up to 4 seats",
        subtitleNe = "आरामदायी कार, ४ सिट",
        subtitleRom = "Comfortable AC cab 4 seat",
        baseFareNpr = 180.0,
        perKmRateNpr = 45.0,
        capacity = 4,
        etaMins = 7,
        iconKey = "car"
    ),
    BUS(
        titleEn = "Micro / Express Bus",
        titleNe = "बस / माइक्रो",
        titleRom = "Bus / Micro",
        subtitleEn = "Group or long distance",
        subtitleNe = "समूह वा लामो दुरी यात्रा",
        subtitleRom = "Group or long route bus",
        baseFareNpr = 30.0,
        perKmRateNpr = 10.0,
        capacity = 25,
        etaMins = 12,
        iconKey = "bus"
    );

    fun calculateFare(distanceKm: Double, surgeMultiplier: Double = 1.0): Double {
        val calculated = (baseFareNpr + (distanceKm * perKmRateNpr)) * surgeMultiplier
        return Math.round(calculated).toDouble()
    }
}
