package com.harish.floatiq.ui.algebra

object SimplificationSolver {
    fun solve(
        expression: String
    ): SimplificationResult {

        try {

            val clean =
                expression
                    .replace(" ", "")
                    .lowercase()
            val variableMap =
                mutableMapOf<String, Int>()

            var constant = 0

            val termRegex =
                Regex("""([+-]?\d*)([a-zA-Z])|([+-]?\d+)""")

            termRegex.findAll(clean).forEach {

                val coefficientPart =
                    it.groupValues[1]

                val variable =
                    it.groupValues[2]

                val constantPart =
                    it.groupValues[3]

                if (variable.isNotEmpty()) {

                    val coefficient =
                        when (coefficientPart) {

                            "" -> 1
                            "+" -> 1
                            "-" -> -1

                            else ->
                                coefficientPart.toInt()
                        }

                    variableMap[variable] =
                        variableMap.getOrDefault(
                            variable,
                            0
                        ) + coefficient

                } else if (
                    constantPart.isNotEmpty()
                ) {

                    constant +=
                        constantPart.toInt()
                }
            }

            val result =
                buildString {

                    variableMap.forEach {

                        val coeff =
                            it.value

                        val variable =
                            it.key

                        if (coeff == 0)
                            return@forEach

                        if (
                            isNotEmpty() &&
                            coeff > 0
                        ) {
                            append("+")
                        }

                        if (coeff == 1)
                            append(variable)

                        else if (coeff == -1)
                            append("-$variable")

                        else
                            append("${coeff}$variable")
                    }

                    if (constant > 0) {

                        append("+$constant")
                    }

                    if (constant < 0) {

                        append("$constant")
                    }
                }

            return SimplificationResult(

                answer = result,

                steps =
                    listOf(
                        "Original Expression",
                        clean,
                        "Combine Like Terms",
                        result
                    )
            )

        } catch (e: Exception) {

            return SimplificationResult(

                answer = "Error",

                steps =
                    listOf(
                        e.toString()
                    )
            )
        }
    }
//    fun solve(
//        expression: String
//    ): SimplificationResult {
//
//        try {
//
//            val clean =
//                expression
//                    .replace(" ", "")
//
//            var xCoefficient = 0
//            var constant = 0
//
//            val termRegex =
//                Regex("""([+-]?\d*)x|([+-]?\d+)""")
//
//            val matches =
//                termRegex.findAll(clean)
//
//            matches.forEach {
//
//                if (
//                    it.value.contains("x")
//                ) {
//
//                    val coefficient =
//                        it.value
//                            .replace("x", "")
//
//                    xCoefficient +=
//                        when (coefficient) {
//
//                            "" -> 1
//                            "+" -> 1
//                            "-" -> -1
//
//                            else ->
//                                coefficient.toInt()
//                        }
//
//                } else {
//
//                    constant +=
//                        it.value.toInt()
//                }
//            }
//
//            val result =
//                buildString {
//
//                    if (xCoefficient != 0) {
//
//                        append("${xCoefficient}x")
//                    }
//
//                    if (constant > 0) {
//
//                        append("+$constant")
//                    }
//
//                    if (constant < 0) {
//
//                        append("$constant")
//                    }
//                }
//
//            return SimplificationResult(
//
//                answer = result,
//
//                steps =
//                    SimplificationStepsEngine
//                        .generateSteps(
//                            clean,
//                            xCoefficient,
//                            constant
//                        )
//            )
//
//        } catch (e: Exception) {
//
//            return SimplificationResult(
//
//                answer = "Error",
//
//                steps =
//                    listOf(
//                        e.toString()
//                    )
//            )
//        }
//    }
}