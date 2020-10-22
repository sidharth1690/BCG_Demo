package com.skills.bcg_demo.ui.login_components

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skills.bcg_demo.R
import com.skills.bcg_demo.base_componets.BaseViewModel
import com.skills.bcg_demo.data.network.ApiDataSource
import com.skills.bcg_demo.data.network.RequestInterface
import com.skills.bcg_demo.data.network.RetrofitClient
import com.skills.bcg_demo.models.ApiModels
import com.skills.bcg_demo.utils.Constants
import com.skills.bcg_demo.utils.Result
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : BaseViewModel() {

    private val apiService: RequestInterface? = RetrofitClient.getClient()
    private val repository: ApiDataSource? = apiService?.let { ApiDataSource(it) }
    var userLoginResult: MutableLiveData<ApiModels.UserLoginResponse> = MutableLiveData()
    private var getUser: MutableLiveData<ApiModels.LoginModel>? = null
    private var onApiResponseFail = MutableLiveData<Response<*>>()

    var userName: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()


    fun getLoginResponseData(): LiveData<ApiModels.UserLoginResponse?>? {
        return userLoginResult
    }

    fun onApiFail(): LiveData<Response<*>?>? {
        return onApiResponseFail
    }


    fun getUser(): LiveData<ApiModels.LoginModel?>? {
        if (getUser == null) {
            getUser = MutableLiveData()
        }
        return getUser
    }

    private val errorMessage: MutableLiveData<String> = MutableLiveData()


    /**
     * request user login after validating user input
     */
    fun requestUserLogin(userName: String, password: String) {
        scope.launch {
            getResponseModel(
                repository?.userLogin(
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
        userLoginResult.postValue(type)
    }

//    /**
//     *click handler for views
//     */
//     fun onClick(view: View) {
//        when (view.id) {
//            R.id.tv_login -> {
//                val loginModel=ApiModels.LoginModel(userName.value+"",password.value+"")
//                getUser?.setValue(loginModel)
//            }
//        }
//    }
}
