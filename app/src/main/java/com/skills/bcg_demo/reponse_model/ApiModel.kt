package com.example.network_module.network_layer.reponse_model

class ApiModel {


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