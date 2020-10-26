/*
*********************************************************
* ******************************************************
 * Copyright 2020 MobileProgrammingLLC
 *  All Rights Reserved*
 *
 * No portion of this material may be reproduced in any form without the written permission of MobileProgrammingLLC.
 * All information contained in this document is MobileProgrammingLLC*'s  private property and trade secret.
 *
 * $Id-
 * Filename:RetrofitClient.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */
package com.skills.bcg_demo.data.network

import com.skills.bcg_demo.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private var mRequestInterface: RequestInterface? = null
    private const val API_HEADER_CONTENT_TYPE = "Content-Type"
    private const val AUTHORIZATION = "Authorization"
    fun getClient(): RequestInterface? {
       return getNetWorkClient()
    }


    /**
     * network client to handle network request
     */
    private fun getNetWorkClient(): RequestInterface? {
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header(API_HEADER_CONTENT_TYPE, Constants.API_HEADER)
                .header(AUTHORIZATION, "Bearer tok_hjhgyut76e2tgt87t278871te8787")
                .build()
            chain.proceed(request)
        }.readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        mRequestInterface = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(RequestInterface::class.java)
        return mRequestInterface
    }
}

































































































































































