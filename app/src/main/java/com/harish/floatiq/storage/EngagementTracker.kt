package com.harish.floatiq.storage

import android.content.Context

object EngagementTracker {

    private const val PREF_NAME = "floatiq_engagement"

    private const val KEY_CALCULATION_COUNT =
        "calculation_count"

    private const val KEY_REVIEW_SHOWN =
        "review_shown"

    private const val KEY_PENDING_REVIEW =
        "pending_review"

    private const val REVIEW_TRIGGER_COUNT = 20


    private fun prefs(
        context: Context
    ) =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )


    fun onSuccessfulCalculation(
        context: Context
    ) {

        val preferences =
            prefs(context)

        if (
            preferences.getBoolean(
                KEY_REVIEW_SHOWN,
                false
            )
        ) {
            return
        }

        val currentCount =
            preferences.getInt(
                KEY_CALCULATION_COUNT,
                0
            ) + 1

        preferences.edit()

            .putInt(
                KEY_CALCULATION_COUNT,
                currentCount
            )

            .apply()

        if (
            currentCount >= REVIEW_TRIGGER_COUNT
        ) {

            preferences.edit()

                .putBoolean(
                    KEY_PENDING_REVIEW,
                    true
                )

                .apply()
        }
    }


    fun shouldAskForReview(
        context: Context
    ): Boolean {

        return prefs(context)

            .getBoolean(
                KEY_PENDING_REVIEW,
                false
            )
    }


    fun markReviewRequested(
        context: Context
    ) {

        prefs(context)

            .edit()

            .putBoolean(
                KEY_PENDING_REVIEW,
                false
            )

            .putBoolean(
                KEY_REVIEW_SHOWN,
                true
            )

            .apply()
    }


    fun getCalculationCount(
        context: Context
    ): Int {

        return prefs(context)

            .getInt(
                KEY_CALCULATION_COUNT,
                0
            )
    }


    fun reset(
        context: Context
    ) {

        prefs(context)

            .edit()

            .clear()

            .apply()
    }
}