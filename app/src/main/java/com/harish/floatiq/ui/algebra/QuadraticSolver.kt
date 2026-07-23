package com.harish.floatiq.ui.algebra

import kotlin.math.sqrt

object QuadraticSolver {

    fun solve(
        equation: String
    ): QuadraticResult {

        val steps =
            mutableListOf<String>()

        try {

            var expr =
                equation
                    .replace(" ", "")
                    .replace("x²", "x^2")
                    .replace("x2", "x^2")
                    .replace("X²", "x^2")
                    .replace("X2", "x^2")

            val leftSide =
                expr.substringBefore("=")

//            val quadraticRegex =
//                Regex(
//                    """([+-]?\d*)x\^2([+-]\d*)x([+-]\d+)"""
//                )
            val quadraticRegex =
                Regex(
                    """([+-]?\d*)x\^2(?:([+-]\d*)x)?([+-]\d+)"""
                )
            val match =
                quadraticRegex.find(
                    leftSide
                )
                    ?: return QuadraticResult(
                        answer = "Invalid Quadratic Equation",
                        steps = listOf(
                            "Unable to parse equation"
                        )
                    )

            val aString =
                match.groupValues[1]

//            val bString =
//                match.groupValues[2]
            val bString =
                match.groupValues[2].ifEmpty {
                    "0"
                }

            val cString =
                match.groupValues[3]

            val a =
                when (aString) {
                    "" -> 1.0
                    "+" -> 1.0
                    "-" -> -1.0
                    else -> aString.toDouble()
                }

            val b =
                when (bString) {
                    "" -> 1.0
                    "+" -> 1.0
                    "-" -> -1.0
                    else -> bString.toDouble()
                }

            val c =
                cString.toDouble()

            steps.add(
                "a = $a"
            )

            steps.add(
                "b = $b"
            )

            steps.add(
                "c = $c"
            )

            val discriminant =
                (b * b) -
                        (4 * a * c)

            steps.add(
                "D = b² - 4ac"
            )

            steps.add(
                "D = (${b}²) - 4(${a})(${c})"
            )

            steps.add(
                "D = $discriminant"
            )

            if (discriminant < 0) {

                return QuadraticResult(
                    answer =
                        "No Real Roots",
                    steps = steps
                )
            }

            val root1 =
                (
                        -b +
                                sqrt(
                                    discriminant
                                )
                        ) / (2 * a)

            val root2 =
                (
                        -b -
                                sqrt(
                                    discriminant
                                )
                        ) / (2 * a)

            steps.add(
                "x₁ = $root1"
            )

            steps.add(
                "x₂ = $root2"
            )

            return QuadraticResult(
                answer =
                    "x₁ = $root1 , x₂ = $root2",
                steps = steps
            )

        } catch (e: Exception) {

            return QuadraticResult(
                answer = "Error",
                steps = listOf(
                    e.toString()
                )
            )
        }
    }
}