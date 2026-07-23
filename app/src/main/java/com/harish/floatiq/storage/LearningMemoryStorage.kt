package com.harish.floatiq.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harish.floatiq.data.LearningMemory

object LearningMemoryStorage {

    private const val PREF_NAME =
        "floatiq_learning_memory"

    private const val KEY_MEMORY =
        "learning_memory"

    private val gson = Gson()

    fun recordView(
        context: Context,
        topic: String
    ) {

        val memories =
            getMemories(context)
                .toMutableList()

        val index =
            memories.indexOfFirst {
                it.topic == topic
            }

        if (index >= 0) {

            val current =
                memories[index]

            memories[index] =
                current.copy(

                    viewCount =
                        current.viewCount + 1,

                    lastViewed =
                        System.currentTimeMillis()
                )

        } else {

            memories.add(

                LearningMemory(

                    topic = topic,

                    viewCount = 1,

                    lastViewed =
                        System.currentTimeMillis()
                )
            )
        }

        saveMemories(
            context,
            memories
        )
    }

    fun getMemories(
        context: Context
    ): List<LearningMemory> {

        val json =
            context
                .getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                )
                .getString(
                    KEY_MEMORY,
                    null
                )

        if (json == null)
            return emptyList()

        val type =
            object :
                TypeToken<List<LearningMemory>>() {}.type

        return gson.fromJson(
            json,
            type
        )
    }

    private fun saveMemories(
        context: Context,
        memories: List<LearningMemory>
    ) {

        val json =
            gson.toJson(memories)

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_MEMORY,
                json
            )
            .apply()
    }

    fun clearMemory(
        context: Context
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .remove(KEY_MEMORY)
            .apply()
    }

}