package com.skills.bcg_demo.ui.home_components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skills.bcg_demo.base_componets.BaseViewModel
import com.skills.bcg_demo.data.network.ApiDataSource
import com.skills.bcg_demo.data.network.RequestInterface
import com.skills.bcg_demo.data.network.RetrofitClient
import com.skills.bcg_demo.models.ApiModels
import com.skills.bcg_demo.utils.Constants
import com.skills.bcg_demo.utils.Result
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : BaseViewModel() {

    private val apiService: RequestInterface? = RetrofitClient.getClient()
    private val repository: ApiDataSource? = apiService?.let { ApiDataSource(it) }
    var userStepUpdateResponse: MutableLiveData<ApiModels.UserUpdatedStepsResponse> = MutableLiveData()
    private var getUserSteps: MutableLiveData<ApiModels.LoginModel>? = null
    private var onApiResponseFail = MutableLiveData<Response<*>>()

    fun getUserStepUpdateResponse(): LiveData<ApiModels.UserUpdatedStepsResponse?>? {
        return userStepUpdateResponse
    }

    fun onApiFail(): LiveData<Response<*>?>? {
        return onApiResponseFail
    }




    private val errorMessage: MutableLiveData<String> = MutableLiveData()


    /**
     * request user login after validating user input
     */
    fun updateStepCount(stepCount:Long) {
        scope.launch {
            getResponseModel(
                repository?.updateUserStep(
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
        userStepUpdateResponse.postValue(type)
    }

}
