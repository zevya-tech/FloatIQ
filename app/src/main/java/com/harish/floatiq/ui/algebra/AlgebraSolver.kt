package com.harish.floatiq.ui.algebra
import com.harish.floatiq.ui.algebra.QuadraticSolver
object AlgebraSolver {

    fun solve(
        equation: String
    ): AlgebraResult {
        if (
            equation.contains("x²") ||
            equation.contains("x^2")
        ) {

            val result =
                QuadraticSolver.solve(
                    equation
                )

            return AlgebraResult(
                answer = result.answer,
                steps = result.steps
            )
        }
        if (
            equation.contains("(") &&
            equation.contains(")")
        ) {

            return ParenthesesAlgebraSolver
                .solve(equation)
        }
        val parsed =
            EquationParser.parse(
                equation
            )

        val coefficient =

            parsed.leftCoefficient -
                    parsed.rightCoefficient

        val constant =

            parsed.rightConstant -
                    parsed.leftConstant

        val x =
            constant / coefficient

        return AlgebraResult(

            answer = x.toString(),

            steps =
                AlgebraStepsEngine.generateSteps(
                    parsed
                )
        )
    }
}