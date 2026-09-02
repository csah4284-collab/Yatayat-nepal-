package com.example.util

object LanguageManager {
    const val LANG_EN = "en"
    const val LANG_NE = "ne"
    const val LANG_ROM = "ne-rom"

    fun formatCurrency(amount: Double, language: String = LANG_EN): String {
        val rounded = Math.round(amount)
        return when (language) {
            LANG_NE -> "रु $rounded"
            LANG_ROM -> "Rs $rounded"
            else -> "NPR $rounded"
        }
    }

    fun getString(key: String, language: String): String {
        return translations[key]?.get(language) ?: translations[key]?.get(LANG_EN) ?: key
    }

    private val translations = mapOf(
        "app_title" to mapOf(
            LANG_EN to "Yatri Nepal",
            LANG_NE to "यात्री नेपाल",
            LANG_ROM to "Yatri Nepal"
        ),
        "tagline" to mapOf(
            LANG_EN to "Fast, Safe Rides Across Nepal",
            LANG_NE to "नेपालभर छिटो र सुरक्षित यात्रा",
            LANG_ROM to "Nepal bhar chhito ra safe ride"
        ),
        "book_ride" to mapOf(
            LANG_EN to "Book a Ride",
            LANG_NE to "यात्रा बुक गर्नुहोस्",
            LANG_ROM to "Ride book garnuhos"
        ),
        "pickup_location" to mapOf(
            LANG_EN to "Pickup Location",
            LANG_NE to "पिकअप स्थान",
            LANG_ROM to "Pickup sthan"
        ),
        "drop_location" to mapOf(
            LANG_EN to "Destination / Drop-off",
            LANG_NE to "गन्तव्य स्थान",
            LANG_ROM to "Gantavya (Drop) sthan"
        ),
        "select_vehicle" to mapOf(
            LANG_EN to "Choose Vehicle",
            LANG_NE to "सवारी साधन रोज्नुहोस्",
            LANG_ROM to "Vehicle choose garnuhos"
        ),
        "payment_method" to mapOf(
            LANG_EN to "Payment Method",
            LANG_NE to "भुक्तानी विधि",
            LANG_ROM to "Payment method"
        ),
        "promo_code" to mapOf(
            LANG_EN to "Promo / Coupon Code",
            LANG_NE to "प्रोमो कोड",
            LANG_ROM to "Promo code"
        ),
        "apply" to mapOf(
            LANG_EN to "Apply",
            LANG_NE to "लागू गर्नुहोस्",
            LANG_ROM to "Apply garnuhos"
        ),
        "discount_applied" to mapOf(
            LANG_EN to "Discount Applied!",
            LANG_NE to "छुट लागू भयो!",
            LANG_ROM to "Discount apply bhayo!"
        ),
        "confirm_booking" to mapOf(
            LANG_EN to "Confirm & Book Ride",
            LANG_NE to "यात्रा पुष्टि र बुक गर्नुहोस्",
            LANG_ROM to "Ride confirm & book garnuhos"
        ),
        "tracking_title" to mapOf(
            LANG_EN to "Live Trip Tracking",
            LANG_NE to "प्रत्यक्ष यात्रा ट्र्याकिङ",
            LANG_ROM to "Live trip tracking"
        ),
        "share_location" to mapOf(
            LANG_EN to "Share Live Location",
            LANG_NE to "प्रत्यक्ष स्थान सेयर गर्नुहोस्",
            LANG_ROM to "Live location share garnuhos"
        ),
        "sos_emergency" to mapOf(
            LANG_EN to "SOS Emergency",
            LANG_NE to "आपतकालीन (SOS)",
            LANG_ROM to "SOS Emergency"
        ),
        "driver_otp" to mapOf(
            LANG_EN to "Start Ride PIN / OTP",
            LANG_NE to "यात्रा सुरु गर्ने ओटिपी",
            LANG_ROM to "Ride start OTP"
        ),
        "nav_home" to mapOf(
            LANG_EN to "Book Ride",
            LANG_NE to "सवारी बुक",
            LANG_ROM to "Book Ride"
        ),
        "nav_tracking" to mapOf(
            LANG_EN to "Live Track",
            LANG_NE to "ट्र्याकिङ",
            LANG_ROM to "Tracking"
        ),
        "nav_history" to mapOf(
            LANG_EN to "History",
            LANG_NE to "इतिहास",
            LANG_ROM to "History"
        ),
        "nav_admin" to mapOf(
            LANG_EN to "Admin Panel",
            LANG_NE to "प्रशासक प्यानल",
            LANG_ROM to "Admin Panel"
        ),
        "nav_profile" to mapOf(
            LANG_EN to "Profile",
            LANG_NE to "प्रोफाइल",
            LANG_ROM to "Profile"
        ),
        "rate_driver" to mapOf(
            LANG_EN to "Rate & Review Driver",
            LANG_NE to "चालकको मूल्याङ्कन र प्रतिक्रिया",
            LANG_ROM to "Driver lai rating dinuhos"
        ),
        "trip_completed" to mapOf(
            LANG_EN to "Trip Completed!",
            LANG_NE to "यात्रा सम्पन्न भयो!",
            LANG_ROM to "Yatra complete bhayo!"
        ),
        "pay_with_esewa" to mapOf(
            LANG_EN to "Pay with eSewa",
            LANG_NE to "ई-सेवाबाट भुक्तानी गर्नुहोस्",
            LANG_ROM to "eSewa bata payment garnuhos"
        ),
        "pay_with_khalti" to mapOf(
            LANG_EN to "Pay with Khalti",
            LANG_NE to "खल्तीबाट भुक्तानी गर्नुहोस्",
            LANG_ROM to "Khalti bata payment garnuhos"
        ),
        "esewa_success" to mapOf(
            LANG_EN to "eSewa Payment Successful!",
            LANG_NE to "ई-सेवा भुक्तानी सफलतापूर्वक सम्पन्न भयो!",
            LANG_ROM to "eSewa payment success bhayo!"
        ),
        "khalti_success" to mapOf(
            LANG_EN to "Khalti Payment Successful!",
            LANG_NE to "खल्ती भुक्तानी सफलतापूर्वक सम्पन्न भयो!",
            LANG_ROM to "Khalti payment success bhayo!"
        ),
        "admin_analytics" to mapOf(
            LANG_EN to "Monthly Performance Analytics",
            LANG_NE to "मासिक कार्यसम्पादन विश्लेषण",
            LANG_ROM to "Monthly Performance Analytics"
        ),
        "automated_report" to mapOf(
            LANG_EN to "Automated Monthly Performance Report",
            LANG_NE to "स्वचालित मासिक प्रतिवेदन",
            LANG_ROM to "Automated Monthly Report"
        ),
        "popular_routes" to mapOf(
            LANG_EN to "Top Nepal Routes",
            LANG_NE to "प्रमुख नेपाली रुटहरू",
            LANG_ROM to "Top Nepal Routes"
        ),
        "vehicle_breakdown" to mapOf(
            LANG_EN to "Vehicle Usage Share",
            LANG_NE to "सवारी साधन प्रयोग हिस्सा",
            LANG_ROM to "Vehicle usage share"
        ),
        "emergency_alert_sent" to mapOf(
            LANG_EN to "Emergency SMS & Live GPS shared with your contact!",
            LANG_NE to "आपतकालीन सन्देश र प्रत्यक्ष जीपीएस सम्पर्कमा पठाइयो!",
            LANG_ROM to "Emergency SMS ra live GPS contact ma pathaiyo!"
        )
    )
}
