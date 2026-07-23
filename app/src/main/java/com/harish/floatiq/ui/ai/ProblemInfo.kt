package com.harish.floatiq.ui.ai

data class ProblemInfo(

    val category: ProblemCategory,

    val type: ProblemType,

    val method: String,

    val confidence: Int
)