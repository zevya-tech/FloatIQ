//package com.harish.floatiq.storage

package com.harish.floatiq.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harish.floatiq.data.HistoryItem

object HistoryStorage {

    private const val PREF_NAME = "floatiq_history"

    private const val KEY_HISTORY = "history"

    private val gson = Gson()

    fun saveCalculation(

        context: Context,

        expression: String,

        result: String

    ) {

        val history =
            getHistory(context).toMutableList()

        history.add(

            0,

            HistoryItem(

                expression = expression,

                result = result,

                timestamp = System.currentTimeMillis()
            )
        )

        val json =
            gson.toJson(history)

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                KEY_HISTORY,
                json
            )
            .apply()
    }

fun getHistory(
    context: Context
): List<HistoryItem> {

    return try {

        val json =
            context
                .getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                )
                .getString(
                    KEY_HISTORY,
                    null
                )

        if (json.isNullOrEmpty())
            return emptyList()

        val type =
            object :
                TypeToken<List<HistoryItem>>() {}.type

        gson.fromJson<List<HistoryItem>>(
            json,
            type
        ) ?: emptyList()

    } catch (e: Exception) {

        emptyList()
    }
}

fun clearHistory(
    context: Context
) {

    context
        .getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        .edit()
        .remove(KEY_HISTORY)
        .apply()
}

    fun deleteHistoryItem(

        context: Context,

        item: HistoryItem

    ) {

        val history =
            getHistory(context)
                .toMutableList()

        history.remove(item)

        val json =
            gson.toJson(history)

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                KEY_HISTORY,
                json
            )
            .apply()
    }

}