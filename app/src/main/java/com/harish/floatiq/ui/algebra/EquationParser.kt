//package com.harish.floatiq.ui.algebra
//
//object EquationParser {
//
//
//fun parse(
//    equation: String
//): Triple<Double, Double, Double> {
//
//    val cleaned =
//        equation.replace(" ", "")
//
//    val parts =
//        cleaned.split("=")
//
//    if (parts.size != 2)
//        throw Exception("Invalid equation")
//
//    val left =
//        parts[0]
//
//    val right =
//        parts[1].toDouble()
//
//    var coefficient = 0.0
//    var constant = 0.0
//
//    val termRegex =
//        Regex("""([+-]?)(\d*)x""")
//
//    termRegex.findAll(left)
//        .forEach {
//
//            val sign =
//                if (it.groupValues[1] == "-")
//                    -1
//                else
//                    1
//
//            val value =
//                when (it.groupValues[2]) {
//
//                    "" -> 1.0
//                    else ->
//                        it.groupValues[2].toDouble()
//                }
//
//            coefficient +=
//                sign * value
//        }
//
//    val constantRegex =
//        Regex("""([+-]\d+)(?!x)""")
//
//    constantRegex.findAll(left)
//        .forEach {
//
//            constant +=
//                it.value.toDouble()
//        }
//
//    return Triple(
//        coefficient,
//        constant,
//        right
//    )
//}
//}
package com.harish.floatiq.ui.algebra

object EquationParser {

    fun parse(
        equation: String
    ): AlgebraEquation {

        val cleaned =
            equation.replace(" ", "")

        val parts =
            cleaned.split("=")

        if (parts.size != 2)
            throw Exception("Invalid equation")

        val left =
            parseSide(parts[0])

        val right =
            parseSide(parts[1])

        return AlgebraEquation(

            leftCoefficient = left.first,

            leftConstant = left.second,

            rightCoefficient = right.first,

            rightConstant = right.second
        )
    }

    private fun parseSide(
        expression: String
    ): Pair<Double, Double> {

        var coefficient = 0.0

        var constant = 0.0

        val normalized =
            expression.replace("-", "+-")

        val terms =
            normalized.split("+")
                .filter { it.isNotBlank() }

        terms.forEach { term ->

            if (term.contains("x")) {

                val value =
                    term.replace("x", "")

                coefficient += when (value) {

                    "", "+" -> 1.0

                    "-" -> -1.0

                    else -> value.toDouble()
                }

            } else {

                constant += term.toDouble()
            }
        }

        return Pair(
            coefficient,
            constant
        )
    }
}