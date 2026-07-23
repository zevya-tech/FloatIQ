package com.harish.floatiq.ui.ocr

object WordProblemRules {

    fun parse(
        text: String
    ): String? {

        val normalized =
            text.lowercase()
                .replace("\n", " ")
                .replace(Regex("\\s+"), " ")
                .trim()

        val arithmeticExpression =
            normalized
                .replace("what is ", "")
                .replace("plus", "+")
                .replace("minus", "-")
                .replace("times", "*")
                .replace("multiplied by", "*")
                .replace("divided by", "/")
                .replace("?", "")
                .replace(" ", "")

        if (
            arithmeticExpression.matches(
                Regex("""\d+[+\-*/]\d+""")
            )
        ) {
            return arithmeticExpression
        }


        Regex(
            """(\d+).*?(buy|buys|bought|add|added|gain|gained|receive|received|get|got|collect|collected|earn|earned|purchase|purchased|more).*?(\d+)"""
        )
        .find(normalized)?.let {

            val first =
                it.groupValues[1]

            val second =
                it.groupValues[3]

            return "$first+$second"
        }

        /*
         * Pattern:
         * John had 20 apples and gave away 5
         * Ravi lost 10 rupees and lost 3
         */

        Regex(
            """(\d+).*?(lost|gave|spent|removed).*?(\d+)"""
        ).find(normalized)?.let {

            val first =
                it.groupValues[1]

            val second =
                it.groupValues[3]

            return "$first-$second"
        }

        /*
         * Pattern:
         * 5 boxes with 4 apples each
         * 6 rows of 3 chairs
         */

        Regex(
            """(\d+).*?(\d+).*?(each|per)"""
        ).find(normalized)?.let {

            val first =
                it.groupValues[1]

            val second =
                it.groupValues[2]

            return "$first*$second"
        }

        /*
         * Pattern:
         * 20 candies shared among 4 children
         * 100 divided among 5 people
         */

        Regex(
            """(\d+).*?(shared|divided).*?(\d+)"""
        ).find(normalized)?.let {

            val first =
                it.groupValues[1]

            val second =
                it.groupValues[3]

            return "$first/$second"
        }

        return null
    }
}