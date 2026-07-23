package com.harish.floatiq.ui.scientific

object ExpressionTokenizer {

    fun tokenize(
        expression: String
    ): List<String> {

        val tokens = mutableListOf<String>()

        var current = ""

        expression.forEach { char ->

            when (char) {

                '+', '-', '×', '/', '(', ')' -> {

                    if (current.isNotEmpty()) {

                        tokens.add(current)
                        current = ""
                    }

                    tokens.add(char.toString())
                }

                else -> {

                    current += char
                }
            }
        }

        if (current.isNotEmpty()) {

            tokens.add(current)
        }

        return tokens
    }
}