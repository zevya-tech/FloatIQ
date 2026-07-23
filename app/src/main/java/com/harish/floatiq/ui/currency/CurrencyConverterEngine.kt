
package com.harish.floatiq.ui.currency

object CurrencyConverterEngine {

    fun formatResult(
        value: Double
    ): String {

        return when {

            value >= 1 ->
                String.format("%.2f", value)

            value == 0.0 ->
                "0"

            else ->
                String.format("%.6f", value)
        }
    }
}
