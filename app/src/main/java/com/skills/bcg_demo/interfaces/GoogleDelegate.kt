package com.skills.bcg_demo.interfaces

import java.util.ArrayList

interface GoogleDelegate {
    fun onGoogleFitDataFetchSuccess(mStepsList: ArrayList<Long>)
    fun onGoogleFitDataFetchFailure()
}