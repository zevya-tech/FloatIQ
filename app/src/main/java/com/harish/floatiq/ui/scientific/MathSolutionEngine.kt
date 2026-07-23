package com.harish.floatiq.ui.scientific

import kotlin.math.*
import com.harish.floatiq.ui.algebra.AlgebraSolver
import com.harish.floatiq.ui.algebra.SimultaneousSolver
import com.harish.floatiq.ui.algebra.FactorizationSolver
import com.harish.floatiq.ui.algebra.ExpansionSolver
import com.harish.floatiq.ui.algebra.SimplificationSolver

data class SolutionResult(
    val result: String,
    val steps: List<String>
)

object MathSolutionEngine {
    private fun solveFunctionsRecursively(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        while (
            expr.contains("sin(") ||
            expr.contains("cos(") ||
            expr.contains("tan(") ||
            expr.contains("log(") ||
            expr.contains("ln(") ||
            expr.contains("sqrt(")
        ) {

            val previous = expr

            expr = solveSingleFunction(expr, steps)

            if (expr == previous) break
        }

        return expr
    }
    private fun solveSingleFunction(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        val functionRegex =
            Regex("""(sin|cos|tan|log|ln|sqrt)\(([^()]*)\)""")

        val match =
            functionRegex.find(expr)
                ?: return expr

        val function =
            match.groupValues[1]

        var inner =
            match.groupValues[2]

        inner =
            replaceConstants(inner, steps)

        inner =
            solveRecursively(inner, steps)

        val value =
            inner.toDouble()

//        val result =
//            when (function) {
//
//                "sin" ->
//                    sin(Math.toRadians(value))
//
//                "cos" ->
//                    cos(Math.toRadians(value))
//
//                "tan" ->
//                    tan(Math.toRadians(value))
//
//                "log" ->
//                    log10(value)
//
//                "ln" ->
//                    ln(value)
//
//                "sqrt" ->
//                    sqrt(value)
//
//                else -> value
//            }
        val result =
            when (function) {

                "sin" ->
                    sin(Math.toRadians(value))

                "cos" ->
                    cos(Math.toRadians(value))

                "tan" -> {

                    val cosValue =
                        cos(Math.toRadians(value))

                    if (abs(cosValue) < 1E-12) {

                        steps.add(
                            "tan($value)\n= Undefined"
                        )

                        return "Undefined"
                    }

                    tan(Math.toRadians(value))
                }

                "log" -> {

                    if (value <= 0) {

                        steps.add(
                            "log($value)\n= Undefined"
                        )

                        return "Undefined"
                    }

                    log10(value)
                }

                "ln" -> {

                    if (value <= 0) {

                        steps.add(
                            "ln($value)\n= Undefined"
                        )

                        return "Undefined"
                    }

                    ln(value)
                }

                "sqrt" -> {

                    if (value < 0) {

                        steps.add(
                            "sqrt($value)\n= Undefined"
                        )

                        return "Undefined"
                    }

                    sqrt(value)
                }

                else -> value
            }
        steps.add(
            "$function($value)\n= $result"
        )

        return expr.replace(
            match.value,
            result.toString()
        )
    }
    private const val MAX_VISIBLE_STEPS = 15
    private fun solveBasicExpression(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        val operationRegex =
            Regex("""(-?\d+(\.\d+)?)([*/+\-])(-?\d+(\.\d+)?)""")

        while (true) {

            val multiplyDivide =
                Regex("""(-?\d+(\.\d+)?)([*/])(-?\d+(\.\d+)?)""")
                    .find(expr)

            val addSubtract =
                Regex("""(-?\d+(\.\d+)?)([+\-])(-?\d+(\.\d+)?)""")
                    .find(expr)

            val match =
                multiplyDivide ?: addSubtract ?: break

            val left =
                match.groupValues[1].toDouble()

            val operator =
                match.groupValues[3]

            val right =
                match.groupValues[4].toDouble()

            val result =
                when (operator) {

                    "*" -> left * right
                    "/" -> left / right
                    "+" -> left + right
                    "-" -> left - right
                    else -> 0.0
                }

            val before =
                match.value

            val after =
                if (result % 1.0 == 0.0)
                    result.toLong().toString()
                else
                    result.toString()

            steps.add(
                "$before\n= $after"
            )

            expr =
                expr.replaceFirst(
                    before,
                    after
                )
        }

        return expr
    }
    private fun solveParentheses(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        val parenthesesRegex =
            Regex("""\(([^()]+)\)""")

        while (parenthesesRegex.containsMatchIn(expr)) {

            val match =
                parenthesesRegex.find(expr)!!

            val innerExpression =
                match.groupValues[1]

            val solved =
                solveBasicExpression(
                    innerExpression,
                    steps
                )

            steps.add(
                "($innerExpression)\n= $solved"
            )

            expr =
                expr.replaceFirst(
                    match.value,
                    solved
                )
        }

        return expr
    }
    private fun solvePowers(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        val powerRegex =
            Regex("""(-?\d+(\.\d+)?)\^(-?\d+(\.\d+)?)""")

        while (powerRegex.containsMatchIn(expr)) {

            val match =
                powerRegex.find(expr)!!

            val base =
                match.groupValues[1].toDouble()

            val exponent =
                match.groupValues[3].toDouble()

            val result =
                base.pow(exponent)

            val after =
                if (result % 1.0 == 0.0)
                    result.toLong().toString()
                else
                    result.toString()

            steps.add(
                "${match.value}\n= $after"
            )

            expr =
                expr.replaceFirst(
                    match.value,
                    after
                )
        }

        return expr
    }
    private fun replaceConstants(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        if (expr.contains("π")) {

            steps.add(
                "π\n= ${Math.PI}"
            )

            expr =
                expr.replace(
                    "π",
                    Math.PI.toString()
                )
        }

        val eRegex =
            Regex("""(?<![a-zA-Z])e(?![a-zA-Z])""")

        if (eRegex.containsMatchIn(expr)) {

            steps.add(
                "e\n= ${Math.E}"
            )

            expr =
                eRegex.replace(
                    expr,
                    Math.E.toString()
                )
        }

        return expr
    }
    private fun solveRecursively(
        expression: String,
        steps: MutableList<String>
    ): String {

        var expr = expression

        val parenthesesRegex =
            Regex("""\(([^()]+)\)""")

        while (parenthesesRegex.containsMatchIn(expr)) {

            val match =
                parenthesesRegex.find(expr)!!

            val inner =
                match.groupValues[1]

            val solvedInner =
                solveRecursively(
                    inner,
                    steps
                )

            steps.add(
                "($inner)\n= $solvedInner"
            )

            expr =
                expr.replaceFirst(
                    match.value,
                    solvedInner
                )
        }

        expr =
            solvePowers(
                expr,
                steps
            )

        expr =
            solveBasicExpression(
                expr,
                steps
            )

        return expr
    }
    fun solve(
        expression: String
    ): SolutionResult {

        val steps = mutableListOf<String>()

        try {

            var expr = expression

            if (
                Regex("[a-zA-Z]").containsMatchIn(expression) &&
                !expression.contains("=") &&
                !expression.contains("^") &&
                !expression.contains("²") &&
                !expression.contains("(")
            ) {

                val result =
                    SimplificationSolver.solve(
                        expression
                    )

                return SolutionResult(
                    result = result.answer,
                    steps = result.steps
                )
            }
            if (
                expression.contains("(") &&
                expression.contains(")") &&
                Regex("[a-zA-Z]").containsMatchIn(expression) &&
                !expression.contains("sin(") &&
                !expression.contains("cos(") &&
                !expression.contains("tan(") &&
                !expression.contains("log(") &&
                !expression.contains("ln(") &&
                !expression.contains("sqrt(")
            ) {

                val result =
                    ExpansionSolver.solve(
                        expression
                    )

                if (
                    result.answer !=
                    "Unsupported Format"
                ) {

                    return SolutionResult(
                        result = result.answer,
                        steps = result.steps
                    )
                }
            }
//            if (
//                expression.contains("(") &&
//                expression.contains(")") &&
//                Regex("[a-zA-Z]").containsMatchIn(expression)
//            ) {
//
//                val result =
//                    ExpansionSolver.solve(
//                        expression
//                    )
//
//                if (
//                    result.answer !=
//                    "Unsupported Format"
//                ) {
//
//                    return SolutionResult(
//                        result = result.answer,
//                        steps = result.steps
//                    )
//                }
//            }
            if (
                !expression.contains("=") &&
                (
                        expression.contains("x²") ||
                                expression.contains("x^2") ||
                                expression.contains("x2")
                        )
            ) {

                val result =
                    FactorizationSolver.solve(
                        expression
                    )

                return SolutionResult(
                    result = result.answer,
                    steps = result.steps
                )
            }
            if (
                expression.contains("\n") &&
                expression.contains("x") &&
                expression.contains("y")
            ) {

                val result =
                    SimultaneousSolver.solve(
                        expression
                    )

                return SolutionResult(
                    result = result.answer,
                    steps = result.steps
                )
            }
            expr =
                expr.replace(
                    "pi",
                    "π"
                )

            if (
                expression.contains("=") &&
                (
                        expression.contains("x") ||
                                expression.contains("x²") ||
                                expression.contains("x^2")
                        )
            ){
                val algebraResult =
                    AlgebraSolver.solve(
                        expression
                    )

                return SolutionResult(
                    result = algebraResult.answer,
                    steps = algebraResult.steps
                )
            }
//            val sinRegex =
//                Regex("""sin\(([^()]*)\)""")
//
//            while (sinRegex.containsMatchIn(expr)) {
//
//                val match = sinRegex.find(expr)!!
//
//
//
//
//                var inner =
//                    match.groupValues[1]
//
//                inner =
//                    replaceConstants(
//                        inner,
//                        steps
//                    )
//
//                inner =
//                    solveRecursively(
//                        inner,
//                        steps
//                    )
//
//                if (inner == "e") {
//
//                    steps.add(
//                        "e\n= ${Math.E}"
//                    )
//
//                    inner =
//                        Math.E.toString()
//                }
//
//                if (inner == "π") {
//
//                    steps.add(
//                        "π\n= ${Math.PI}"
//                    )
//
//                    inner =
//                        Math.PI.toString()
//                }
//
//                val value =
//                    inner.toDouble()
//                val result =
//                    sin(Math.toRadians(value))
//
//                steps.add(
//                    "sin($value)\n= $result"
//                )
//
//                expr =
//                    expr.replace(
//                        match.value,
//                        result.toString()
//                    )
//            }
//
//            val cosRegex =
//                Regex("""cos\(([^()]*)\)""")
//
//            while (cosRegex.containsMatchIn(expr)) {
//
//                val match = cosRegex.find(expr)!!
//
//
//                var inner =
//                    match.groupValues[1]
//
//                inner =
//                    replaceConstants(
//                        inner,
//                        steps
//                    )
//
//                inner =
//                    solveRecursively(
//                        inner,
//                        steps
//                    )
//                if (inner == "e") {
//
//                    steps.add(
//                        "e\n= ${Math.E}"
//                    )
//
//                    inner =
//                        Math.E.toString()
//                }
//
//                if (inner == "π") {
//
//                    steps.add(
//                        "π\n= ${Math.PI}"
//                    )
//
//                    inner =
//                        Math.PI.toString()
//                }
//
//                val value =
//                    inner.toDouble()
//
//                val result =
//                    cos(Math.toRadians(value))
//
//                steps.add(
//                    "cos($value)\n= $result"
//                )
//
//                expr =
//                    expr.replace(
//                        match.value,
//                        result.toString()
//                    )
//            }
//            val tanRegex =
//                Regex("""tan\(([^()]*)\)""")
//
//            while (tanRegex.containsMatchIn(expr)) {
//
//                val match = tanRegex.find(expr)!!
//
//
//
//                var inner =
//                    match.groupValues[1]
//
//                inner =
//                    replaceConstants(
//                        inner,
//                        steps
//                    )
//
//                inner =
//                    solveRecursively(
//                        inner,
//                        steps
//                    )
//
//                if (inner == "e") {
//
//                    steps.add(
//                        "e\n= ${Math.E}"
//                    )
//
//                    inner =
//                        Math.E.toString()
//                }
//
//                if (inner == "π") {
//
//                    steps.add(
//                        "π\n= ${Math.PI}"
//                    )
//
//                    inner =
//                        Math.PI.toString()
//                }
//
//                val value =
//                    inner.toDouble()
//
//                val cosine =
//                    cos(Math.toRadians(value))
//
//                if (abs(cosine) < 1e-10) {
//
//                    steps.add(
//                        "tan($value)\n= Undefined"
//                    )
//
//                    return SolutionResult(
//                        result = "Undefined",
//                        steps = steps
//                    )
//                }
//
//                val result =
//                    tan(Math.toRadians(value))
//                steps.add(
//                    "tan($value)\n= $result"
//                )
//
//                expr =
//                    expr.replace(
//                        match.value,
//                        result.toString()
//                    )
//            }
//            val logRegex =
//                Regex("""log\(([^()]*)\)""")
//
//            while (logRegex.containsMatchIn(expr)) {
//
//                val match = logRegex.find(expr)!!
//
//
//
//
//                var inner =
//                    match.groupValues[1]
//
//                inner =
//                    replaceConstants(
//                        inner,
//                        steps
//                    )
//
//                inner =
//                    solveRecursively(
//                        inner,
//                        steps
//                    )
//
//                if (inner == "e") {
//
//                    steps.add(
//                        "e\n= ${Math.E}"
//                    )
//
//                    inner =
//                        Math.E.toString()
//                }
//
//                if (inner == "π") {
//
//                    steps.add(
//                        "π\n= ${Math.PI}"
//                    )
//
//                    inner =
//                        Math.PI.toString()
//                }
//
//                val value =
//                    inner.toDouble()
//                val result =
//                    log10(value)
//
//                steps.add(
//                    "log($value)\n= $result"
//                )
//
//                expr =
//                    expr.replace(
//                        match.value,
//                        result.toString()
//                    )
//            }
//
//            val lnRegex =
//                Regex("""ln\(([^()]*)\)""")
//
//            while (lnRegex.containsMatchIn(expr)) {
//
//                val match = lnRegex.find(expr)!!
//
//                var inner =
//                    match.groupValues[1]
//
//                inner =
//                    replaceConstants(
//                        inner,
//                        steps
//                    )
//
//                inner =
//                    solveRecursively(
//                        inner,
//                        steps
//                    )
//
//                if (inner == "e") {
//
//                    steps.add(
//                        "e\n= ${Math.E}"
//                    )
//
//                    inner =
//                        Math.E.toString()
//                }
//
//                if (inner == "π") {
//
//                    steps.add(
//                        "π\n= ${Math.PI}"
//                    )
//
//                    inner =
//                        Math.PI.toString()
//                }
//
//                val value =
//                    inner.toDouble()
//
//                val result =
//                    ln(value)
//
//                steps.add(
//                    "ln($value)\n= $result"
//                )
//
//                expr =
//                    expr.replace(
//                        match.value,
//                        result.toString()
//                    )
//            }
//            val sqrtRegex =
//                Regex("""sqrt\(([^()]*)\)""")
//
//            while (sqrtRegex.containsMatchIn(expr)) {
//
//                val match = sqrtRegex.find(expr)!!
//
//
//
//
//                var inner =
//                    match.groupValues[1]
//
//                inner =
//                    replaceConstants(
//                        inner,
//                        steps
//                    )
//
//                inner =
//                    solveRecursively(
//                        inner,
//                        steps
//                    )
//
//                if (inner == "e") {
//
//                    steps.add(
//                        "e\n= ${Math.E}"
//                    )
//
//                    inner =
//                        Math.E.toString()
//                }
//
//                if (inner == "π") {
//
//                    steps.add(
//                        "π\n= ${Math.PI}"
//                    )
//
//                    inner =
//                        Math.PI.toString()
//                }
//
//                val value =
//                    inner.toDouble()
//                val result =
//                    sqrt(value)
//
//                steps.add(
//                    "sqrt($value)\n= $result"
//                )
//
//                expr =
//                    expr.replace(
//                        match.value,
//                        result.toString()
//                    )
//            }
            expr =
                solveFunctionsRecursively(
                    expr,
                    steps
                )
            if (expr.contains("π")) {

                steps.add(
                    "π\n= ${Math.PI}"
                )

                expr =
                    expr.replace(
                        "π",
                        Math.PI.toString()
                    )
            }

            val eRegex =
                Regex("""(?<![a-zA-Z])e(?![a-zA-Z])""")

            if (eRegex.containsMatchIn(expr)) {

                steps.add(
                    "e\n= ${Math.E}"
                )

                expr =
                    eRegex.replace(
                        expr,
                        Math.E.toString()
                    )
            }

            val solvedExpression =
                solveRecursively(
                    expr,
                    steps
                )
            val finalResult =
                ScientificCalculatorEngine
                    .evaluate(solvedExpression)

            steps.add(
                "Final Result\n= $finalResult"
            )

            return SolutionResult(
                result = finalResult,
                steps =
                    if (steps.size > MAX_VISIBLE_STEPS)
                        steps.take(MAX_VISIBLE_STEPS)
                    else
                        steps
            )

        } catch (e: Exception) {

            return SolutionResult(
                result = "Error",
                steps = listOf(
                    e.toString()
                )
            )
        }
    }
}