package com.harish.floatiq.ui.algebra

object ExpansionSolver {

    fun solve(
        expression: String
    ): ExpansionResult {

        try {

            val clean =
                expression
                    .replace(" ", "")
            val squarePlusRegex =
                Regex("""\(([a-zA-Z])\+([a-zA-Z])\)(\^2|²)""")

            val plusMatch =
                squarePlusRegex.find(clean)

            if (plusMatch != null) {

                val a = plusMatch.groupValues[1]
                val b = plusMatch.groupValues[2]

                return ExpansionResult(

                    answer =
                        "${a}²+2${a}${b}+${b}²",

                    steps = listOf(
                        "Using identity:",
                        "(a+b)² = a² + 2ab + b²",
                        "${a}²+2${a}${b}+${b}²"
                    )
                )
            }
            val squareMinusRegex =
                Regex("""\(([a-zA-Z])\-([a-zA-Z])\)(\^2|²)""")

            val minusMatch =
                squareMinusRegex.find(clean)

            if (minusMatch != null) {

                val a = minusMatch.groupValues[1]
                val b = minusMatch.groupValues[2]

                return ExpansionResult(

                    answer =
                        "${a}²-2${a}${b}+${b}²",

                    steps = listOf(
                        "Using identity:",
                        "(a-b)² = a² - 2ab + b²",
                        "${a}²-2${a}${b}+${b}²"
                    )
                )
            }
            val regex =
                Regex(
                    """\(x([+-]\d+)\)\(x([+-]\d+)\)"""
                )

            val match =
                regex.find(clean)

                    ?: return ExpansionResult(
                        answer = "Unsupported Format",
                        steps = listOf(
                            "Example: (x+2)(x+3)"
                        )
                    )

            val first =
                match.groupValues[1].toInt()

            val second =
                match.groupValues[2].toInt()

            val b =
                first + second

            val c =
                first * second

            val result = buildString {

                append("x²")

                if (b >= 0)
                    append("+${b}x")
                else
                    append("${b}x")

                if (c >= 0)
                    append("+$c")
                else
                    append("$c")
            }

            return ExpansionResult(

                answer = result,

                steps =
                    ExpansionStepsEngine
                        .generateSteps(
                            b,
                            c
                        )
            )

        } catch (e: Exception) {

            return ExpansionResult(

                answer = "Error",

                steps =
                    listOf(
                        e.toString()
                    )
            )
        }
    }
}