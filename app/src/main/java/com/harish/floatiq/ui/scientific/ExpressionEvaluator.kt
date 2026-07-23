package com.harish.floatiq.ui.scientific

object ExpressionEvaluator {

    fun evaluate(expression: String): Double {

        val tokens = expression
            .replace("×", "*")
            .replace("/", "/")

        return calculate(tokens)
    }

    private fun calculate(expression: String): Double {
        android.util.Log.d("CALC_DEBUG", "calculate() input = $expression")
        var expr = expression
        while (expr.contains("(")) {

            val start =
                expr.lastIndexOf("(")

            val end =
                expr.indexOf(")", start)

            val innerExpression =
                expr.substring(
                    start + 1,
                    end
                )

            val innerResult =
                calculate(innerExpression)

            expr =
                expr.replaceRange(
                    start,
                    end + 1,
                    innerResult.toString()
                )
            expr = expr
                .replace("+-", "-")
                .replace("-+", "-")
                .replace("--", "+")
                .replace("++", "+")
        }
        while (expr.contains("^")) {

            val regex =
                Regex("""(-?\d+(\.\d+)?)\^(-?\d+(\.\d+)?)""")

            val match = regex.find(expr) ?: break

            val base =
                match.groupValues[1].toDouble()

            val power =
                match.groupValues[3].toDouble()
            val result =
                Math.pow(base, power)


            expr =
                expr.replaceRange(
                    match.range,
                    result.toString()
                )
        }
        while (expr.contains("*")) {

            val regex =
                Regex("""(-?\d+(\.\d+)?)\*(-?\d+(\.\d+)?)""")

            val match = regex.find(expr) ?: break

            val result =
                match.groupValues[1].toDouble() *
                        match.groupValues[3].toDouble()

            expr =
                expr.replaceRange(
                    match.range,
                    result.toString()
                )
        }

        while (expr.contains("/")) {

            val regex =
                Regex("""(-?\d+(\.\d+)?)/(-?\d+(\.\d+)?)""")

            val match = regex.find(expr) ?: break

            val result =
                match.groupValues[1].toDouble() /
                        match.groupValues[3].toDouble()

            expr =
                expr.replaceRange(
                    match.range,
                    result.toString()
                )
        }
        if (expr.toDoubleOrNull() != null) {
            return expr.toDouble()
        }

//        val numbers =
//            expr.split(
//                Regex("(?<![Ee])(?=[+-])|(?<=[+-])")
//            )
//        var result = numbers[0].toDouble()
//
//        var i = 1
        val numbers =
            expr.split(
                Regex("(?<![Ee])(?=[+-])|(?<=[+-])")
            ).toMutableList()

// Handle expressions starting with unary minus
        if (numbers.isNotEmpty() &&
            numbers[0].isEmpty() &&
            numbers.size >= 3 &&
            numbers[1] == "-"
        ) {
            numbers[2] = "-" + numbers[2]
            numbers.removeAt(1)
            numbers.removeAt(0)
        }

        var result = numbers[0].toDouble()

        var i = 1

        while (i < numbers.size) {

            when (numbers[i]) {

                "+" -> {
                    result += numbers[i + 1].toDouble()
                }

                "-" -> {
                    result -= numbers[i + 1].toDouble()
                }
            }

            i += 2
        }

        return result
    }
}