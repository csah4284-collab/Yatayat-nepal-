package com.example.data.models

enum class RideStatus(val labelEn: String, val labelNe: String, val labelRom: String) {
    SEARCHING("Searching Driver", "चालक खोजिँदै...", "Driver khojidai..."),
    DRIVER_ASSIGNED("Driver Assigned", "चालक तोकियो", "Driver tokiyo"),
    DRIVER_ARRIVED("Driver Arrived", "चालक आइपुग्यो", "Driver aaipugyo"),
    ON_TRIP("On Trip", "यात्रा जारी छ", "Yatra jari chha"),
    COMPLETED("Completed", "यात्रा सम्पन्न", "Yatra complete"),
    CANCELLED("Cancelled", "रद्द गरियो", "Radda gariyo")
}

enum class PaymentMethod(
    val displayName: String,
    val brandName: String,
    val iconKey: String,
    val isDigital: Boolean
) {
    ESEWA("eSewa Digital Wallet", "eSewa", "esewa", true),
    KHALTI("Khalti Digital Wallet", "Khalti", "khalti", true),
    FONEPAY("Fonepay / QR Pay", "Fonepay", "fonepay", true),
    CASH("Cash on Drop", "Cash (नगद)", "cash", false),
    CARD("Debit / Credit Card", "Card", "card", true)
}

enum class PaymentStatus(val labelEn: String, val labelNe: String) {
    PENDING("Pending", "बाँकी"),
    PAID("Paid", "सम्पन्न"),
    FAILED("Failed", "असफल"),
    REFUNDED("Refunded", "फिर्ता")
}
