package com.harish.floatiq.storage

import android.content.Context

object FavoritesStorage {

    private const val PREF_NAME =
        "formula_favorites"

    private const val KEY_FAVORITES =
        "favorites"

    fun getFavorites(
        context: Context
    ): Set<String> {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getStringSet(
                KEY_FAVORITES,
                emptySet()
            )
            ?: emptySet()
    }

    fun isFavorite(
        context: Context,
        formulaName: String
    ): Boolean {

        return getFavorites(context)
            .contains(formulaName)
    }

    fun toggleFavorite(
        context: Context,
        formulaName: String
    ) {

        val favorites =
            getFavorites(context)
                .toMutableSet()

        if (
            favorites.contains(
                formulaName
            )
        ) {

            favorites.remove(
                formulaName
            )

        } else {

            favorites.add(
                formulaName
            )
        }

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putStringSet(
                KEY_FAVORITES,
                favorites
            )
            .apply()
    }
}