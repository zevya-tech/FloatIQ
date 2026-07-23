package com.harish.floatiq.ui.ai

object ProblemClassifier {

    fun classify(
        expression: String
    ): ProblemInfo {

        val expr =
            expression.replace(" ", "")

        if (
            expr.contains("sin(") ||
            expr.contains("cos(") ||
            expr.contains("tan(")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.TRIGONOMETRY,

                type =
                    ProblemType.TRIGONOMETRY,

                method =
                    "Trigonometric Evaluation",

                confidence = 95
            )
        }

        if (
            expr.contains("log(")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.SCIENTIFIC,

                type =
                    ProblemType.LOGARITHM,

                method =
                    "Logarithm",

                confidence = 95
            )
        }

        if (
            expr.contains("ln(")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.SCIENTIFIC,

                type =
                    ProblemType.NATURAL_LOG,

                method =
                    "Natural Logarithm",

                confidence = 95
            )
        }

        if (
            expr.contains("sqrt(")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.ARITHMETIC,

                type =
                    ProblemType.SQUARE_ROOT,

                method =
                    "Square Root",

                confidence = 95
            )
        }
//        if (
//            expr.contains("x²") ||
//            expr.contains("x^2")
//        ) {
//
//            if (
//                !expr.contains("=")
//            ) {
//
//                return ProblemInfo(
//                    category =
//                        ProblemCategory.ALGEBRA,
//
//                    type =
//                        ProblemType.FACTORIZATION,
//
//                    method =
//                        "Factorization",
//
//                    confidence = 90
//                )
//            }
//
//            return ProblemInfo(
//                category =
//                    ProblemCategory.ALGEBRA,
//
//                type =
//                    ProblemType.QUADRATIC_EQUATION,
//
//                method =
//                    "Quadratic Equation",
//
//                confidence = 90
//            )
//        }
        // Difference Of Squares
        if (
            (expr.contains("x²-") || expr.contains("x^2-")) &&
            expr.any { it.isDigit() }
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.ALGEBRA,

                type =
                    ProblemType.DIFFERENCE_OF_SQUARES,

                method =
                    "Difference Of Squares",

                confidence = 95
            )
        }

// Quadratic Expressions
        if (
            expr.contains("x²") ||
            expr.contains("x^2")
        ) {

            if (!expr.contains("=")) {

                return ProblemInfo(
                    category =
                        ProblemCategory.ALGEBRA,

                    type =
                        ProblemType.FACTORIZATION,

                    method =
                        "Factorization",

                    confidence = 90
                )
            }

            return ProblemInfo(
                category =
                    ProblemCategory.ALGEBRA,

                type =
                    ProblemType.QUADRATIC_EQUATION,

                method =
                    "Quadratic Equation",

                confidence = 90
            )
        }
        if (
            expr.contains("=") &&
            expr.contains("x")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.ALGEBRA,

                type =
                    ProblemType.LINEAR_EQUATION,

                method =
                    "Solve For X",

                confidence = 90
            )
        }

        if (
            expr.contains("%")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.ARITHMETIC,

                type =
                    ProblemType.PERCENTAGE,

                method =
                    "Percentage Calculation",

                confidence = 95
            )
        }

        if (
            expr.contains("π")
        ) {

            return ProblemInfo(
                category =
                    ProblemCategory.ARITHMETIC,

                type =
                    ProblemType.GEOMETRY,

                method =
                    "Geometry Formula",

                confidence = 90
            )
        }

        return ProblemInfo(

            category =
                ProblemCategory.ARITHMETIC,

            type =
                ProblemType.BASIC_ARITHMETIC,

            method =
                "Basic Evaluation",

            confidence = 70
        )
    }
}