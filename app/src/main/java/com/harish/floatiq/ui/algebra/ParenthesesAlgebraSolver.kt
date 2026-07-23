package com.harish.floatiq.ui.algebra

object ParenthesesAlgebraSolver {

//    fun solve(
//        equation: String
//    ): AlgebraResult {
//
//        val cleaned =
//            equation.replace(" ", "")
//
//        val parts =
//            cleaned.split("=")
//
//        val left =
//            parts[0]
//
//        val rhs =
//            parts[1].toDouble()
//
//        val regex =
//            Regex("""(\d+)\(([-]?\d*)x([+-]\d+)\)""")
//
//        val match =
//            regex.find(left)
//                ?: throw Exception("Invalid equation")
//
//        val multiplier =
//            match.groupValues[1].toDouble()
//
//        val coefficient =
//            when (match.groupValues[2]) {
//
//                "" -> 1.0
//
//                "-" -> -1.0
//
//                else ->
//                    match.groupValues[2].toDouble()
//            }
//
//        val constant =
//            match.groupValues[3].toDouble()
//
//        val expandedCoefficient =
//            multiplier * coefficient
//
//        val expandedConstant =
//            multiplier * constant
//
//        val x =
//            (rhs - expandedConstant) /
//                    expandedCoefficient
//
//        return AlgebraResult(
//
//            answer = x.toString(),
//
//            steps =
//                ParenthesesStepsEngine.generateSteps(
//
//                    multiplier,
//
//                    coefficient,
//
//                    constant,
//
//                    rhs
//                )
//        )
//    }
fun solve(
    equation: String
): AlgebraResult {

    val cleaned =
        equation.replace(" ", "")

    val parts =
        cleaned.split("=")

    if (parts.size != 2)
        throw Exception("Invalid equation")

    val left =
        parts[0]

    val rhs =
        parts[1].toDouble()

    val outerRegex =
        Regex("""(\d+)\((.+)\)""")

    val outerMatch =
        outerRegex.find(left)
            ?: throw Exception("Invalid equation")

    val multiplier =
        outerMatch.groupValues[1].toDouble()

    val innerExpression =
        outerMatch.groupValues[2]

    var innerCoefficient = 0.0

    var innerConstant = 0.0

    val normalized =
        innerExpression.replace("-", "+-")

    val terms =
        normalized.split("+")
            .filter {
                it.isNotBlank()
            }

    terms.forEach { term ->

        if (term.contains("x")) {

            val value =
                term.replace("x", "")

            innerCoefficient +=
                when (value) {

                    "" -> 1.0

                    "+" -> 1.0

                    "-" -> -1.0

                    else -> value.toDouble()
                }

        } else {

            innerConstant +=
                term.toDouble()
        }
    }

    val expandedCoefficient =
        multiplier * innerCoefficient

    val expandedConstant =
        multiplier * innerConstant

    val x =
        (rhs - expandedConstant) /
                expandedCoefficient

    return AlgebraResult(

        answer = x.toString(),

        steps =
            ParenthesesStepsEngine.generateSteps(

                multiplier,

                innerCoefficient,

                innerConstant,

                rhs
            )
    )
}
}