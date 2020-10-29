package com.skills.bcg_demo.google_fit_components

import android.content.Context
import android.util.Log
import com.example.network_module.network_layer.reponse_model.ApiModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import com.skills.bcg_demo.interfaces.GoogleDelegate
import com.skills.bcg_demo.utils.AppUtils
import com.skills.bcg_demo.utils.Constants
import java.util.*
import java.util.concurrent.TimeUnit

class GoogleClient(val context: Context,var delegate: GoogleDelegate) {

    private var mStepsList: ArrayList<Long> = ArrayList()
    private var mGoogleFitObj = ApiModel.GoogleFitResponse()


    /**
     * prepare request to fetch data from google fit
     */
    fun accessGoogleFit() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val endTime = cal.timeInMillis
        cal.add(Calendar.DAY_OF_WEEK, -14)
        val startTime = cal.timeInMillis
        val readRequest = DataReadRequest.Builder()
            .aggregate(
                DataType.TYPE_STEP_COUNT_DELTA
            )
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()
        val account = GoogleSignIn
            .getAccountForExtension(context, Constants.googleFitOptions)
        Fitness.getHistoryClient(context, account)
            .readData(readRequest)
            .addOnSuccessListener { response: DataReadResponse? ->
                if (response?.buckets?.isNotEmpty()!!) {
                    for (bucket in response.buckets) bucket.dataSets.forEach {
                        Log.e("History", "Number of buckets: " + response.buckets)
                        extractRespectiveDataSet(it)
                    }
                } else if (response.dataSets?.isNotEmpty()!!) response.dataSets.forEach {
                    extractRespectiveDataSet(it)
                }
//                if(mStepsList.size>0) {
//                    prepareBarChartAndSetView(mStepsList)
//                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.d("some data", "OnFailure()", e)
            }
    }

    /**
     * extract required data from the fit response data set
     * data set: returned data data packets from google fit.
     */
    private fun extractRespectiveDataSet(dataSet: DataSet) {
        for (dp in dataSet.dataPoints) {
            dp.dataType.fields.forEach breakLoop@{
                when (it.name) {
                    Field.FIELD_STEPS.name -> {
                        mGoogleFitObj.userStepCount = dp.getValue(Field.FIELD_STEPS).asInt().toLong()
                        Log.e("History", "Number of Steps: " + mGoogleFitObj.userStepCount)
                        mStepsList.add(mGoogleFitObj.userStepCount!!)
                    }

                }
            }
        }
        delegate.onGoogleFitDataFetchSuccess(mStepsList)
    }


    /**
     * Request history API with the HistoryClient object to update step data.
     */
    fun updateData(value:Int): Task<Void?>? {
        val dataSet = insertFitnessData(value)
        var startTime: Long = 0
        var endTime: Long = 0
        for (dataPoint in dataSet.dataPoints) {
            startTime = dataPoint.getStartTime(TimeUnit.MILLISECONDS)
            endTime = dataPoint.getEndTime(TimeUnit.MILLISECONDS)
        }
        val request = DataUpdateRequest.Builder()
            .setDataSet(dataSet)
            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
        return Fitness.getHistoryClient(
            context,
            GoogleSignIn.getLastSignedInAccount(context)!!
        )
            .updateData(request)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // At this point the data has been updated and can be read.
                    Log.i("TAG", "Data update was successful." + task.result)
                    AppUtils.showToast(context, "Your steps updated successfully")
                } else {
                    AppUtils.showToast(context, "Some unknown error occurred")

                    Log.e(
                        "TAG",
                        "There was a problem updating the dataset.",
                        task.exception
                    )
                }
            }
    }



    /**
     * Creates and returns a data set of step count data for updating step for today.
     * returns a data set to be inserted into google fit records.
     */
    private fun insertFitnessData(value: Int): DataSet {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        val startTime = calendar.timeInMillis

        // Create a data source
        val dataSource = DataSource.Builder()
            .setAppPackageName(context.packageName)
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setStreamName("TAG - step count")
            .setType(DataSource.TYPE_RAW)
            .build()

        // Create a data set
        return DataSet.builder(dataSource)
            .add(
                DataPoint.builder(dataSource)
                    .setField(Field.FIELD_STEPS, value)
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build()
            ).build()
    }
}