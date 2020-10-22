package com.skills.bcg_demo.data.network

import com.skills.bcg_demo.models.ApiModels
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestInterface {

    @POST("some/end_point")
    suspend fun getUserData(@Body body: ApiModels.UserLoginRequest): Response<ApiModels.UserLoginResponse>

    @POST("some/end_point")
    suspend fun updateUserStepCount(@Body body: ApiModels.UpdateStepCountRequest): Response<ApiModels.UserUpdatedStepsResponse>

}
