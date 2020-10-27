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
 * Filename:HomeViewModel.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */
package com.skills.bcg_demo.ui.home_components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.network_module.network_layer.Result
import com.example.network_module.network_layer.reponse_model.ApiModels
import com.skills.bcg_demo.base_componets.BaseViewModel
import com.skills.bcg_demo.data.network.ApiDataSource
import com.skills.bcg_demo.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : BaseViewModel() {

    private val mRepository: ApiDataSource? = ApiDataSource()
    var mUserStepUpdateResponse: MutableLiveData<ApiModels.UserUpdatedStepsResponse> = MutableLiveData()
    private var mOnApiResponseFail = MutableLiveData<Response<*>>()

    fun getUserStepUpdateResponse(): LiveData<ApiModels.UserUpdatedStepsResponse?>? {
        return mUserStepUpdateResponse
    }

    fun onApiFail(): LiveData<Response<*>?>? {
        return mOnApiResponseFail
    }




    private val errorMessage: MutableLiveData<String> = MutableLiveData()


    /**
     * request user login after validating user input
     */
    fun updateStepCount(stepCount:Long) {
        mScope.launch {
            getResponseModel(
                mRepository?.updateUserStep(
                    ApiModels.UpdateStepCountRequest(
                        stepCount
                    )
                )
            )
        }
    }




    private suspend fun <T> getResponseModel(dataObj: T) {
        when (dataObj) {
            is Result.Success<*> -> when (val isType = dataObj.data) {
                is ApiModels.UserUpdatedStepsResponse -> when (isType.successful) {
                    Constants.API_RESPONSE_SUCCESS -> {
                        postUserStepUpdateResponseData(isType)
                    }
                    else -> {
                        if (isType.messageType != null) {
                            // do some error stuff here..like api
                        }
                    }
                }
                is Result.Error -> onResponseError("")
            }
        }

    }

    private fun onResponseError(error: String?) {
        errorMessage.postValue(error)
    }

    private fun postUserStepUpdateResponseData(type: ApiModels.UserUpdatedStepsResponse) {
        mUserStepUpdateResponse.postValue(type)
    }

}
