//package com.harish.floatiq.ui.currency
//
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//interface CurrencyApi {
//
//    @GET("latest")
//    suspend fun getRate(
//
//        @Query("from")
//        from: String,
//
//        @Query("to")
//        to: String
//
//    ): Response<CurrencyResponse>
//}
package com.harish.floatiq.ui.currency

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("latest")
    suspend fun getRate(

        @Query("from")
        from: String,

        @Query("symbols")
        to: String

    ): Response<CurrencyResponse>
}