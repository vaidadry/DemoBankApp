package com.vaidadry.demobankapp.data

import com.vaidadry.demobankapp.data.model.ApiTransaction
import retrofit2.Response
import retrofit2.http.GET

interface TransactionsApi {

    @GET("ebb5bfdc-efda-4966-9ecf-d2c171d6985a")
    suspend fun getAllTransactions(): Response<List<ApiTransaction>>
}