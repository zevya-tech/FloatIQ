package com.harish.floatiq.ui.algebra

object FactorizationStepsEngine {

    fun generateSteps(
        b: Int,
        c: Int,
        factor1: Int,
        factor2: Int
    ): List<String> {

        return listOf(

            "Find two numbers whose product is $c",

            "Find two numbers whose sum is $b",

            "$factor1 × $factor2 = $c",

            "$factor1 + $factor2 = $b",

            "Factorized Form",

            "(x + $factor1)(x + $factor2)"
        )
    }
}