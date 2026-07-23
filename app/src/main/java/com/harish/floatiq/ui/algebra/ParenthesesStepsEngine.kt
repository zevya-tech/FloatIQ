package com.harish.floatiq.ui.algebra

object ParenthesesStepsEngine {

    fun generateSteps(

        multiplier: Double,

        innerCoefficient: Double,

        innerConstant: Double,

        rhs: Double

    ): List<String> {

        val steps =
            mutableListOf<String>()

        steps.add(
            "${multiplier}(${innerCoefficient}x + ${innerConstant}) = $rhs"
        )

        val expandedCoefficient =
            multiplier * innerCoefficient

        val expandedConstant =
            multiplier * innerConstant

        steps.add(
            "${expandedCoefficient}x + ${expandedConstant} = $rhs"
        )

        val newRhs =
            rhs - expandedConstant

        steps.add(
            "${expandedCoefficient}x = $newRhs"
        )

        val x =
            newRhs / expandedCoefficient

        steps.add(
            "x = $x"
        )

        return steps
    }
}