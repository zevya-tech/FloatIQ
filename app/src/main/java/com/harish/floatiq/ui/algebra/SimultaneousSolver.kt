package com.harish.floatiq.ui.algebra

object SimultaneousSolver {

    fun solve(
        equations: String
    ): SimultaneousResult {

        val steps =
            mutableListOf<String>()

        try {

            val lines =
                equations
                    .split("\n")
                    .filter {
                        it.isNotBlank()
                    }

            if (lines.size != 2) {

                return SimultaneousResult(
                    answer =
                        "Need exactly 2 equations",
                    steps =
                        listOf(
                            "Example:\n2x+y=10\nx-y=2"
                        )
                )
            }

            val eq1 =
                SimultaneousParser.parse(
                    lines[0]
                )

            val eq2 =
                SimultaneousParser.parse(
                    lines[1]
                )

            steps.add(
                "${eq1.a}x + ${eq1.b}y = ${eq1.c}"
            )

            steps.add(
                "${eq2.a}x + ${eq2.b}y = ${eq2.c}"
            )

            val determinant =
                (eq1.a * eq2.b) -
                        (eq2.a * eq1.b)

            steps.add(
                "Determinant = $determinant"
            )

            if (determinant == 0.0) {

                return SimultaneousResult(
                    answer =
                        "No Unique Solution",
                    steps = steps
                )
            }

            val x =
                (
                        (eq1.c * eq2.b) -
                                (eq2.c * eq1.b)
                        ) / determinant

            val y =
                (
                        (eq1.a * eq2.c) -
                                (eq2.a * eq1.c)
                        ) / determinant

            steps.add(
                "x = $x"
            )

            steps.add(
                "y = $y"
            )

            return SimultaneousResult(

                answer =
                    "x = $x , y = $y",

                steps = steps
            )

        } catch (e: Exception) {

            return SimultaneousResult(

                answer = "Error",

                steps =
                    listOf(
                        e.toString()
                    )
            )
        }
    }
}