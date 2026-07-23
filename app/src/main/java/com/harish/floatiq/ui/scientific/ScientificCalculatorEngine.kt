package com.harish.floatiq.ui.scientific
import com.harish.floatiq.ui.scientific.ExpressionEvaluator
import kotlin.math.*
import com.harish.floatiq.ui.scientific.ExpressionTokenizer
import com.harish.floatiq.ui.utils.NumberFormatter

private fun formatResult(
    value: Double
): String {

    return NumberFormatter.format(value)
}
object ScientificCalculatorEngine {

    private fun normalizeResult(
        value: Double
    ): Double {

        return when {

            value.isNaN() ->
                Double.NaN

            value.isInfinite() ->
                Double.NaN

            abs(value) < 1E-12 ->
                0.0

            else ->
                value
        }
    }
    private fun insertImplicitMultiplication(
        expression: String
    ): String {

        var expr = expression

        // 2(3+4) -> 2×(3+4)
        expr = expr.replace(
            Regex("""(\d)\("""),
            "$1×("
        )

        // )( -> )×(
        expr = expr.replace(
            Regex("""\)\("""),
            ")×("
        )

        // 2sin(30) -> 2×sin(30)
        expr = expr.replace(
            Regex("""(\d)(sin|cos|tan|log|ln|√)"""),
            "$1×$2"
        )

        // 2π -> 2×π
        expr = expr.replace(
            Regex("""(\d)(π|e)"""),
            "$1×$2"
        )

        // )sin(30) -> )×sin(30)
        expr = expr.replace(
            Regex("""\)(sin|cos|tan|log|ln|√)"""),
            ")×$1"
        )

        // )π -> )×π
        expr = expr.replace(
            Regex("""\)(π|e)"""),
            ")×$1"
        )

        return expr
    }
    private fun evaluateInnerExpression(
        expression: String
    ): Double {

        expression.toDoubleOrNull()?.let {
            return it
        }

        val tokens =
            ExpressionTokenizer
                .tokenize(expression)

        return ExpressionEvaluator
            .evaluate(
                tokens.joinToString("")
            )
    }

    private fun replaceScientificFunctions(
        expression: String
    ): String {

        var expr = expression

        val sinRegex =
            Regex("""sin\(([^()]*)\)""")

        val cosRegex =
            Regex("""cos\(([^()]*)\)""")

        val tanRegex =
            Regex("""tan\(([^()]*)\)""")

        val logRegex =
            Regex("""log\(([^()]*)\)""")

        val lnRegex =
            Regex("""ln\(([^()]*)\)""")

        val sqrtRegex =
            Regex("""√\(([^()]*)\)|√(\d+(\.\d+)?)""")

        val squareRegex =
            Regex("""\(([^()]*)\)x²|(\d+(\.\d+)?)x²""")
        val percentRegex =
            Regex("""(\d+(\.\d+)?)%""")
        while (
            sinRegex.containsMatchIn(expr) ||
            cosRegex.containsMatchIn(expr) ||
            tanRegex.containsMatchIn(expr) ||
            logRegex.containsMatchIn(expr) ||
            lnRegex.containsMatchIn(expr)
        ) {

            val sinMatch = sinRegex.find(expr)

            if (sinMatch != null) {

                val value =
                    evaluateInnerExpression(
                        sinMatch.groupValues[1]
                    )

                val result =
                    normalizeResult(
                        sin(Math.toRadians(value))
                    )

                expr = expr.replace(
                    sinMatch.value,
                    result.toString()
                )
                continue
            }

            val cosMatch = cosRegex.find(expr)

            if (cosMatch != null) {

                val value =
                    evaluateInnerExpression(
                        cosMatch.groupValues[1]
                    )

                val result =
                    normalizeResult(
                        cos(Math.toRadians(value))
                    )

                expr = expr.replace(
                    cosMatch.value,
                    result.toString()
                )
                continue
            }

//            val tanMatch = tanRegex.find(expr)
//
//            if (tanMatch != null) {
//
//                val value =
//                    evaluateInnerExpression(
//                        tanMatch.groupValues[1]
//                    )
//
//                val result =
//                    normalizeResult(
//                        tan(Math.toRadians(value))
//                    )
//
//                expr = expr.replace(
//                    tanMatch.value,
//                    result.toString()
//                )
//                continue
//            }
            val tanMatch = tanRegex.find(expr)

            if (tanMatch != null) {

                val value =
                    evaluateInnerExpression(
                        tanMatch.groupValues[1]
                    )

                val cosValue =
                    cos(Math.toRadians(value))

                if (abs(cosValue) < 1E-12) {
                    return "Error"
                }

                val result =
                    normalizeResult(
                        tan(Math.toRadians(value))
                    )
                if (value <= 0) {
                    return "Error"
                }

                expr = expr.replace(
                    tanMatch.value,
                    result.toString()
                )

                continue
            }
            val logMatch = logRegex.find(expr)

            if (logMatch != null) {

                val value =
                    evaluateInnerExpression(
                        logMatch.groupValues[1]
                    )
                if (value <= 0) {
                    return "Error"
                }

                val result =
                    normalizeResult(
                        log10(value)
                    )

                expr = expr.replace(
                    logMatch.value,
                    result.toString()
                )
                continue
            }

            val lnMatch = lnRegex.find(expr)

            if (lnMatch != null) {

                val value =
                    evaluateInnerExpression(
                        lnMatch.groupValues[1]
                    )

                val result =
                    normalizeResult(
                        ln(value)
                    )

                expr = expr.replace(
                    lnMatch.value,
                    result.toString()
                )
                continue
            }
        }

        while (sqrtRegex.containsMatchIn(expr)) {

            val match =
                sqrtRegex.find(expr)!!



            val value =
                if (match.groupValues[1].isNotEmpty()) {

                    val inner =
                        match.groupValues[1]

                    if (
                        inner.toDoubleOrNull() != null
                    ) {
                        inner.toDouble()
                    } else {
                        evaluateInnerExpression(
                            inner
                        )
                    }

                } else {

                    match.groupValues[2].toDouble()
                }

            if (value < 0) {
                return "Error"
            }

            val result =
                normalizeResult(
                    sqrt(value)
                )
            expr =
                expr.replace(
                    match.value,
                    result.toString()
                )
        }
        while (squareRegex.containsMatchIn(expr)) {

            val match =
                squareRegex.find(expr)!!

//            val value =
//                match.groupValues[1].toDouble()
            val value =
                if (match.groupValues[1].isNotEmpty()) {
                    android.util.Log.d(
                        "SQRT_DEBUG",
                        "Input=${match.groupValues[1]}"
                    )
                    evaluateInnerExpression(
                        match.groupValues[1]
                    )

                } else {

                    match.groupValues[2].toDouble()
                }
            val result =
                value * value

            expr =
                expr.replace(
                    match.value,
                    result.toString()
                )
        }
        while (percentRegex.containsMatchIn(expr)) {

            val match =
                percentRegex.find(expr)!!

            val value =
                match.groupValues[1].toDouble()

            val result =
                value / 100.0

            expr =
                expr.replace(
                    match.value,
                    result.toString()
                )
        }


        return expr
    }
    private fun processCalculatorPercentages(
        expression: String
    ): String {

        var expr = expression
        expr =
            expr.replace(
                "π",
                Math.PI.toString()
            )

        expr =
            expr.replace(
                "e",
                Math.E.toString()
            )
        val addPercentRegex =
            Regex("""(\d+(\.\d+)?)\+(\d+(\.\d+)?)%""")

        val subtractPercentRegex =
            Regex("""(\d+(\.\d+)?)\-(\d+(\.\d+)?)%""")

        while (addPercentRegex.containsMatchIn(expr)) {

            val match =
                addPercentRegex.find(expr)!!

            val base =
                match.groupValues[1].toDouble()

            val percent =
                match.groupValues[3].toDouble()

            val result =
                base + (base * percent / 100.0)

            expr =
                expr.replace(
                    match.value,
                    result.toString()
                )
        }

        while (subtractPercentRegex.containsMatchIn(expr)) {

            val match =
                subtractPercentRegex.find(expr)!!

            val base =
                match.groupValues[1].toDouble()

            val percent =
                match.groupValues[3].toDouble()

            val result =
                base - (base * percent / 100.0)

            expr =
                expr.replace(
                    match.value,
                    result.toString()
                )
        }

        return expr
    }
    fun evaluate(expression: String): String {

//        var processedExpression =
//            processCalculatorPercentages(
//                expression
//            )
        var processedExpression =
            insertImplicitMultiplication(
                expression
            )
        android.util.Log.d("CALC_DEBUG", "After implicit multiplication = $processedExpression")
        if (
            processedExpression.contains("NaN") ||
            processedExpression.contains("Infinity")
        ) {
            return "Error"
        }
        processedExpression =
            processCalculatorPercentages(
                processedExpression
            )

        processedExpression =
            replaceScientificFunctions(
                processedExpression
            )
        android.util.Log.d("CALC_DEBUG", "Before evaluator = $processedExpression")
        if (
            processedExpression.toDoubleOrNull() != null
        ) {
            return formatResult(
                processedExpression.toDouble()
            )
        }

        return try {

            when {


                processedExpression.any {
                    it.isDigit() ||
                            it in "+-×/()."
                } -> {

                    val tokens =
                        ExpressionTokenizer
                            .tokenize(processedExpression)

                    formatResult(
                        ExpressionEvaluator
                            .evaluate(
                                tokens.joinToString("")
                            )
                    )
                }

                else -> {

                    "Unsupported"
                }
            }

        } catch (e: Exception) {

            "Error"
        }
    }
}