package com.harish.floatiq.ui.currency

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: CurrencyApi by lazy {

        Retrofit.Builder()

            .baseUrl(
                "https://api.frankfurter.app/"
            )

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

            .create(
                CurrencyApi::class.java
            )
    }
}