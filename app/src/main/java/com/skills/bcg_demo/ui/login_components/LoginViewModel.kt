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
 * Filename:LoginViewModel.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */package com.skills.bcg_demo.ui.login_components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.network_module.network_layer.Result
import com.example.network_module.network_layer.reponse_model.ApiModel
import com.example.network_module.network_layer.reponse_model.ApiModels
import com.skills.bcg_demo.base_componets.BaseViewModel
import com.skills.bcg_demo.data.network.ApiDataSource
import com.skills.bcg_demo.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : BaseViewModel() {

    private val mRepository: ApiDataSource? = ApiDataSource()
    var mUserLoginResult: MutableLiveData<ApiModels.UserLoginResponse> = MutableLiveData()
    private var mGetUser: MutableLiveData<ApiModels.LoginModel>? = null
    private var mOnApiResponseFail = MutableLiveData<Response<*>>()

    var userName: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()


    fun getLoginResponseData(): LiveData<ApiModels.UserLoginResponse?>? {
        return mUserLoginResult
    }

    fun onApiFail(): LiveData<Response<*>?>? {
        return mOnApiResponseFail
    }


    fun getUser(): LiveData<ApiModels.LoginModel?>? {
        if (mGetUser == null) {
            mGetUser = MutableLiveData()
        }
        return mGetUser
    }

    private val errorMessage: MutableLiveData<String> = MutableLiveData()


    /**
     * request user login after validating user input
     */
    fun requestUserLogin(userName: String, password: String) {
        mScope.launch {
            getResponseModel(
                mRepository?.userLogin(
                    ApiModels.UserLoginRequest(
                        userName,
                        password
                    )
                )
            )
        }
    }




    private  fun <T> getResponseModel(dataObj: T) {
        when (dataObj) {
            is Result.Success<*> -> when (val isType = dataObj.data) {
                is ApiModels.UserLoginResponse -> when (isType.successful) {
                    Constants.API_RESPONSE_SUCCESS -> {
                        postUserLoginResponseData(isType)
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

    private fun postUserLoginResponseData(type: ApiModels.UserLoginResponse) {
        mUserLoginResult.postValue(type)
    }

}
