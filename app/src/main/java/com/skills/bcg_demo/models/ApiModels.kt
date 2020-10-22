package com.skills.bcg_demo.models

class ApiModels {


    data class UserLoginRequest(val email:String, val password:String)
    data class LoginModel(val email:String, val password:String)

    data class UpdateStepCountRequest(val stepCount:Long)


    data class UserLoginResponse(val error: String,
                                 val successful: Int,
                                 val message: String,
                                 val messageType: String?)

    data class UserUpdatedStepsResponse(val error: String,
                                 val successful: Int,
                                 val message: String,
                                 val steps: String,
                                 val messageType: String?)

    data class GoogleFitResponse(var userStepCount: Long? = 0,
                                 var totalDistanceTravelled: Long? = 0,
                                 var totalCaloriesBurned: Long? = 0,
                                 var activityData: ArrayList<FitActivityResponse> = ArrayList())

    data class FitActivityResponse(var activityCode: Int,
                                   var activityName: String,
                                   var activityDuration: Long,
                                   var caloriesBurned: Long)
}