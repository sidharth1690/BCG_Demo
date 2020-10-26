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
 * Filename:RequestInterface.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */
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
