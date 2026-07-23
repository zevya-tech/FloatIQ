package com.harish.floatiq.ui.ocr

object OCRProcessor {

    fun normalizeMath(
        text: String
    ): String {

        return text
            .replace("×", "*")
            .replace("÷", "/")
            .replace(
                Regex("""(?<=\d)[xX](?=\d)"""),
                "*"
            )
            .replace("pi", "π")
            .replace("PI", "π")
            .replace("²", "^2")
            .replace("³", "^3")
            .replace("=", "")
            .replace(Regex("""\s+"""), "")
            .trim()
            .lowercase()
    }

    fun speechText(
        text: String
    ): String {

        return text
            .replace("+", " plus ")
            .replace("-", " minus ")
            .replace("*", " multiplied by ")
            .replace("÷", " divided by ")
            .replace("/", " divided by ")
            .replace("^", " raised to the power ")
            .replace("π", " pi ")

    }
    fun normalizeForSolver(
        text: String
    ): String {

//        return text
        var normalized =
            text
                .replace(Regex("""\s+"""), "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace(
                    Regex("""(?<=\d)[xX](?=\d)"""),
                    "*"
            )
            .replace("sln(", "sin(")
            .replace("c0s(", "cos(")
            .replace("iog(", "log(")
            .replace("Iog(", "log(")
            .replace("In(", "ln(")
            .replace("pi", "π")
            .replace("PI", "π")
            .replace("x²", "x^2")
            .replace("x³", "x^3")
            .replace("x2", "x^2")
            .replace("x3", "x^3")
            .replace("y²", "y^2")
            .replace("y³", "y^3")
            .replace("y2", "y^2")
            .replace("y3", "y^3")
            .replace("²", "^2")
            .replace("³", "^3")
        normalized =
            normalized.replace(
                Regex("""[Vv](?=\d|\()"""),
                "√"
            )
        normalized =
            normalized.replace(
                Regex("""√(\d+)"""),
                "sqrt($1)"
            )

//        return normalized
        return normalized.lowercase()
    }
    fun isRawMathExpression(
        text: String
    ): Boolean {

        val cleaned =
            normalizeMath(text)

        return cleaned.matches(
            Regex(
                "[a-zA-Z0-9+\\-*/().=π^²³ ]+"
            )
        )
    }
}