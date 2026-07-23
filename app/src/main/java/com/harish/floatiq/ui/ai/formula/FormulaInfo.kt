package com.harish.floatiq.ui.ai.formula

data class FormulaInfo(

    val name: String,

    val category: FormulaCategory,

    val formula: String,

    val explanation: String,

    val variables: String,

    val example: String,

    val applications: String,

    val keywords: List<String>
)