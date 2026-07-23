package com.harish.floatiq.ui.algebra

object ExpansionStepsEngine {

    fun generateSteps(
        b: Int,
        c: Int
    ): List<String> {

        return listOf(

            "Apply distributive property",

            "x × x = x²",

            "Outer + Inner terms = ${b}x",

            "Last terms = $c",

            "Combine like terms",

            "x² + ${b}x + $c"
        )
    }
}