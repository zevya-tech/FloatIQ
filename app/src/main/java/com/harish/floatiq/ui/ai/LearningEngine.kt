package com.harish.floatiq.ui.ai

import android.content.Context
import com.harish.floatiq.storage.LearningMemoryStorage

object LearningEngine {

    fun recordFormulaView(
        context: Context,
        formulaName: String
    ) {

        LearningMemoryStorage
            .recordView(
                context,
                formulaName
            )
    }
    fun clearLearningMemory(
        context: Context
    ) {

        LearningMemoryStorage
            .clearMemory(
                context
            )
    }
//    fun getTopLearningTopics(
//        context: Context
//    ) =
//
//        LearningMemoryStorage
//            .getMemories(context)
//
//            .sortedByDescending {
//                it.viewCount
//            }
//
//            .take(5)
fun getTopLearningTopics(
    context: Context
) =

    LearningMemoryStorage
        .getMemories(context)

        .filter {

            it.viewCount >= 6
        }

        .sortedByDescending {

            it.viewCount
        }

        .take(5)
}