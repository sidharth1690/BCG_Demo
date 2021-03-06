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
 * Filename:ApiDataSource.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */
package com.skills.bcg_demo.data.network

import android.util.Log
import com.example.network_module.network_layer.NetworkClient
import com.example.network_module.network_layer.Result
import com.example.network_module.network_layer.reponse_model.ApiModels
import com.example.network_module.network_layer.safeApiCall
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject

class ApiDataSource {

    suspend fun userLogin(requestObj: ApiModels.UserLoginRequest) = safeApiCall(call = { userLoginNetworkCall(requestObj) })
    suspend fun updateUserStep(requestObj: ApiModels.UpdateStepCountRequest) = safeApiCall(call = { updateUserStepData(requestObj) })

    private suspend fun userLoginNetworkCall(requestObj: ApiModels.UserLoginRequest): Result<ApiModels.UserLoginResponse> {
        Log.d("login_request", GsonBuilder().setPrettyPrinting().create().toJson(requestObj).toString())
        val response = NetworkClient.apiService.getUserData(requestObj)
        return when {
            response.isSuccessful -> {
                Log.d("login_response", GsonBuilder().setPrettyPrinting().create().toJson(response.body()!!).toString())
                Result.Success(response.body()!!)
            }
            else -> Result.Error(parseErrorBody(response.errorBody()))
        }
    }

    private suspend fun updateUserStepData(requestObj: ApiModels.UpdateStepCountRequest): Result<ApiModels.UserUpdatedStepsResponse> {
        Log.d("updateStepData_request", GsonBuilder().setPrettyPrinting().create().toJson(requestObj).toString())
        val response = NetworkClient.apiService.updateUserStepCount(requestObj)

        return when {
            response.isSuccessful -> {
                Log.d("updateStepData_response", GsonBuilder().setPrettyPrinting().create().toJson(response.body()!!).toString())
                Result.Success(response.body()!!)
            }
            else -> Result.Error(parseErrorBody(response.errorBody()))
        }
    }


    private fun parseErrorBody(data: ResponseBody?): String? {
        return try {
            val errorMessage = JSONObject(data.toString()).getString("message")
            Log.d("HttpServerError", errorMessage)
            errorMessage
        } catch (e: Exception) {
            e.message
        }
    }

}