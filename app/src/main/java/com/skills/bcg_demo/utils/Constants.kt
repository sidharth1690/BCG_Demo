package com.skills.bcg_demo.utils

import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType

class Constants {

    companion object {

        const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 666
        const val BASE_URL = "http://sportsreloaded.thedatavue.com/backend/api/"
        const val API_HEADER = "application/json"
        const val API_RESPONSE_SUCCESS: Int = 1

        val googleFitOptions: FitnessOptions by lazy {
            FitnessOptions.builder()
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE).build()
        }
    }


}