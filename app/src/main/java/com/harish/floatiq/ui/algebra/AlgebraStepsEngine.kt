//
//package com.harish.floatiq.ui.algebra
//
//object AlgebraStepsEngine {
//
//    fun generateSteps(
//        a: Double,
//        b: Double,
//        c: Double
//    ): List<String> {
//
//        val steps =
//            mutableListOf<String>()
//
//        if (b != 0.0) {
//
//            steps.add(
//                "${a}x + ${b} = $c"
//            )
//
//            val rhs =
//                c - b
//
//            steps.add(
//                "${a}x = $rhs"
//            )
//
//            val x =
//                rhs / a
//
//            steps.add(
//                "x = $x"
//            )
//
//        } else {
//
//            steps.add(
//                "${a}x = $c"
//            )
//
//            val x =
//                c / a
//
//            steps.add(
//                "x = $x"
//            )
//        }
//
//        return steps
//    }
//}

package com.harish.floatiq.ui.algebra

object AlgebraStepsEngine {

    fun generateSteps(
        equation: AlgebraEquation
    ): List<String> {

        val steps =
            mutableListOf<String>()

        steps.add(

            "${equation.leftCoefficient}x + ${equation.leftConstant} = " +
                    "${equation.rightCoefficient}x + ${equation.rightConstant}"
        )

        val newCoefficient =

            equation.leftCoefficient -
                    equation.rightCoefficient

        steps.add(

            "${newCoefficient}x + ${equation.leftConstant} = " +
                    equation.rightConstant
        )

        val rhs =

            equation.rightConstant -
                    equation.leftConstant

        steps.add(
            "${newCoefficient}x = $rhs"
        )

        val x =
            rhs / newCoefficient

        steps.add(
            "x = $x"
        )

        return steps
    }
}