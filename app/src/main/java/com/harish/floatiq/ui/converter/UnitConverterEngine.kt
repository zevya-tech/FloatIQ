package com.harish.floatiq.ui.converter

object UnitConverterEngine {

    fun convertLength(
        value: Double,
        from: String,
        to: String
    ): Double {

        val meters = when (from) {

            "km" -> value * 1000
            "m" -> value
            "cm" -> value / 100
            "mm" -> value / 1000

            else -> value
        }

        return when (to) {

            "km" -> meters / 1000
            "m" -> meters
            "cm" -> meters * 100
            "mm" -> meters * 1000

            else -> meters
        }

    }
    fun convertWeight(
        value: Double,
        from: String,
        to: String
    ): Double {

        val grams = when (from) {

            "kg" -> value * 1000
            "g" -> value
            "lb" -> value * 453.592

            else -> value
        }

        return when (to) {

            "kg" -> grams / 1000
            "g" -> grams
            "lb" -> grams / 453.592

            else -> grams
        }
    }
    fun convertTemperature(
        value: Double,
        from: String,
        to: String
    ): Double {

        val celsius = when (from) {

            "°C" -> value

            "°F" ->
                (value - 32) * 5 / 9

            "K" ->
                value - 273.15

            else -> value
        }

        return when (to) {

            "°C" -> celsius

            "°F" ->
                celsius * 9 / 5 + 32

            "K" ->
                celsius + 273.15

            else -> celsius
        }
    }
    fun convertTime(
        value: Double,
        from: String,
        to: String
    ): Double {

        val seconds = when (from) {

            "hour" -> value * 3600
            "min" -> value * 60
            "sec" -> value

            else -> value
        }

        return when (to) {

            "hour" -> seconds / 3600
            "min" -> seconds / 60
            "sec" -> seconds

            else -> seconds
        }
    }
    fun convertDataStorage(
        value: Double,
        from: String,
        to: String
    ): Double {

        val kb = when (from) {

            "KB" -> value
            "MB" -> value * 1024
            "GB" -> value * 1024 * 1024
            "TB" -> value * 1024 * 1024 * 1024

            else -> value
        }

        return when (to) {

            "KB" -> kb
            "MB" -> kb / 1024
            "GB" -> kb / (1024 * 1024)
            "TB" -> kb / (1024 * 1024 * 1024)

            else -> kb
        }
    }
    fun convertArea(
        value: Double,
        from: String,
        to: String
    ): Double {

        val squareMeters = when (from) {

            "km²" -> value * 1_000_000
            "m²" -> value
            "ft²" -> value * 0.092903

            else -> value
        }

        return when (to) {

            "km²" -> squareMeters / 1_000_000
            "m²" -> squareMeters
            "ft²" -> squareMeters / 0.092903

            else -> squareMeters
        }
    }
    fun convertVolume(
        value: Double,
        from: String,
        to: String
    ): Double {

        val liters = when (from) {

            "L" -> value
            "mL" -> value / 1000
            "m³" -> value * 1000

            else -> value
        }

        return when (to) {

            "L" -> liters
            "mL" -> liters * 1000
            "m³" -> liters / 1000

            else -> liters
        }
    }
    fun convertSpeed(
        value: Double,
        from: String,
        to: String
    ): Double {

        val metersPerSecond = when (from) {

            "km/h" -> value / 3.6
            "m/s" -> value
            "mph" -> value * 0.44704

            else -> value
        }

        return when (to) {

            "km/h" -> metersPerSecond * 3.6
            "m/s" -> metersPerSecond
            "mph" -> metersPerSecond / 0.44704

            else -> metersPerSecond
        }
    }
    fun convert(
        category: String,
        value: Double,
        from: String,
        to: String
    ): Double {

        return when (category) {

            "Length" ->
                convertLength(value, from, to)

            "Weight" ->
                convertWeight(value, from, to)

            "Temperature" ->
                convertTemperature(value, from, to)

            "Area" ->
                convertArea(value, from, to)

            "Volume" ->
                convertVolume(value, from, to)

            "Speed" ->
                convertSpeed(value, from, to)
            "Time" ->
                convertTime(value, from, to)

            "Data Storage" ->
                convertDataStorage(value, from, to)

            else -> value
        }
    }
    fun getUnits(
        category: String
    ): List<String> {

        return when (category) {

            "Length" ->
                listOf(
                    "km",
                    "m",
                    "cm",
                    "mm"
                )

            "Weight" ->
                listOf(
                    "kg",
                    "g",
                    "lb"
                )

            "Temperature" ->
                listOf(
                    "°C",
                    "°F",
                    "K"
                )

            "Area" ->
                listOf(
                    "km²",
                    "m²",
                    "ft²"
                )

            "Volume" ->
                listOf(
                    "L",
                    "mL",
                    "m³"
                )

            "Speed" ->
                listOf(
                    "km/h",
                    "m/s",
                    "mph"
                )

            "Time" ->
                listOf(
                    "sec",
                    "min",
                    "hour"
                )

            "Data Storage" ->
                listOf(
                    "KB",
                    "MB",
                    "GB",
                    "TB"
                )

            else -> emptyList()
        }
    }
    fun formatResult(value: Double): String {

        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            "%.6f".format(value)
                .trimEnd('0')
                .trimEnd('.')
        }
    }
}