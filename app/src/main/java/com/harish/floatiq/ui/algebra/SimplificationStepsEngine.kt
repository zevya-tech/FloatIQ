package com.harish.floatiq.ui.algebra

object SimplificationStepsEngine {

    fun generateSteps(
        original: String,
        xCoefficient: Int,
        constant: Int
    ): List<String> {

        val steps = mutableListOf<String>()

        steps.add(
            "Original Expression"
        )

        steps.add(
            original
        )

        steps.add(
            "Combine like terms"
        )

        steps.add(
            "${xCoefficient}x"
        )

        if (constant != 0) {

            steps.add(
                "Constant = $constant"
            )
        }

        return steps
    }
}