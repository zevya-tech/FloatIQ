package com.harish.floatiq.ui.algebra

object SimultaneousParser {

    fun parse(
        equation: String
    ): SimultaneousEquation {

        val clean =
            equation
                .replace(" ", "")

        val left =
            clean.substringBefore("=")

        val right =
            clean.substringAfter("=")

        val xRegex =
            Regex("""([+-]?\d*)x""")

        val yRegex =
            Regex("""([+-]?\d*)y""")

        val xMatch =
            xRegex.find(left)

        val yMatch =
            yRegex.find(left)

        val a =
            parseCoefficient(
                xMatch?.groupValues?.get(1)
            )

        val b =
            parseCoefficient(
                yMatch?.groupValues?.get(1)
            )

        val c =
            right.toDouble()

        return SimultaneousEquation(
            a = a,
            b = b,
            c = c
        )
    }

    private fun parseCoefficient(
        value: String?
    ): Double {

        return when (value) {

            null -> 0.0

            "" -> 1.0

            "+" -> 1.0

            "-" -> -1.0

            else -> value.toDouble()
        }
    }
}