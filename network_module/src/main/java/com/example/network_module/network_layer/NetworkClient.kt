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
 * Filename:NetworkClient.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */
package com.example.network_module.network_layer

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    //creating a Network Interceptor to add authentication token to get approved at server for interaction
    private val interceptor = Interceptor { chain ->
        val url = chain.request().url().newBuilder().build()
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer sometoekhjgh76576frr76r6r76rr76")
            .url(url)
            .build()
        chain.proceed(request)
    }

    // here we have created a networking client using OkHttp and added authInterceptor.
    private val apiClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().client(apiClient)
            .baseUrl("https://someserverpoints.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: RequestInterface = getRetrofit().create(RequestInterface::class.java)

}