package com.harish.floatiq.ui.algebra

object FactorizationSolver {

    fun solve(
        expression: String
    ): FactorizationResult {

        try {

            val expr =
                expression
                    .replace(" ", "")
                    .replace("x²", "x^2")
                    .replace("x2", "x^2")
            val differenceSquaresRegex =
                Regex("""x\^2-([0-9]+)""")

            val diffMatch =
                differenceSquaresRegex.find(expr)

            if (diffMatch != null) {

                val value =
                    diffMatch.groupValues[1].toInt()

                val root =
                    kotlin.math.sqrt(
                        value.toDouble()
                    ).toInt()

                if (root * root == value) {

                    return FactorizationResult(

                        answer =
                            "(x-$root)(x+$root)",

                        steps = listOf(
                            "$value is a perfect square",
                            "x² - $value",
                            "= x² - ${root}²",
                            "= (x-$root)(x+$root)"
                        )
                    )
                }
            }
            val regex =
                Regex("""x\^2([+-]\d+)x([+-]\d+)""")

            val match =
                regex.find(expr)

                    ?: return FactorizationResult(
                        answer = "Cannot Factorize",
                        steps = listOf(
                            "Unsupported expression"
                        )
                    )

            val b =
                match.groupValues[1]
                    .toInt()

            val c =
                match.groupValues[2]
                    .toInt()

            for (i in -100..100) {

                for (j in -100..100) {

                    if (
                        i * j == c &&
                        i + j == b
                    ) {

                        val factorized =

                            "(x ${sign(i)} ${kotlin.math.abs(i)})" +
                                    "(x ${sign(j)} ${kotlin.math.abs(j)})"

                        return FactorizationResult(

                            answer = factorized,

                            steps =
                                FactorizationStepsEngine
                                    .generateSteps(
                                        b,
                                        c,
                                        i,
                                        j
                                    )
                        )
                    }
                }
            }

            return FactorizationResult(

                answer = "Cannot Factorize",

                steps =
                    listOf(
                        "No integer factors found"
                    )
            )

        } catch (e: Exception) {

            return FactorizationResult(

                answer = "Error",

                steps =
                    listOf(
                        e.toString()
                    )
            )
        }
    }

    private fun sign(
        value: Int
    ): String {

        return if (value >= 0)
            "+"
        else
            "-"
    }
}