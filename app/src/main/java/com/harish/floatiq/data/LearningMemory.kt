package com.harish.floatiq.data

data class LearningMemory(

    val topic: String,

    val viewCount: Int = 0,

    val lastViewed: Long = 0L,

    val successRate: Float = 0f
)