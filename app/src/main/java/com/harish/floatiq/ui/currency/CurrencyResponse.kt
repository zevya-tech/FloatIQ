//package com.harish.floatiq.ui.currency
//
package com.harish.floatiq.ui.currency

data class CurrencyResponse(

    val amount: Double,

    val base: String,

    val date: String,

    val rates: Map<String, Double>
)