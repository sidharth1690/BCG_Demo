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
 * Filename:Constants.kt
 * Author:
 * Creation Date: 20/10/2020
 *
 * ****************************************************
 * ******************************************************
 */
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

         val days =
            arrayOf(
                "Day14",
                "Day13",
                "Day12",
                "Day11",
                "Day10",
                "Day9",
                "Day8",
                "Day7",
                "Day6",
                "Day5",
                "Day4",
                "Day3",
                "Day2",
                "Day1"
            )
    }


}