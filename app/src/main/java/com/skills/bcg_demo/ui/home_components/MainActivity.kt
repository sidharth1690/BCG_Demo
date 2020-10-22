package com.skills.bcg_demo.ui.home_components

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessActivities
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import com.skills.bcg_demo.R
import com.skills.bcg_demo.components.ViewModelFactory
import com.skills.bcg_demo.databinding.ActivityMainBinding
import com.skills.bcg_demo.models.ApiModels
import com.skills.bcg_demo.utils.AppUtils
import com.skills.bcg_demo.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var activityMainBinding: ActivityMainBinding
    private var stepsList: ArrayList<Long> = ArrayList()
    private var googleFitObj = ApiModels.GoogleFitResponse()
    private val days =
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
    private fun getGoogleAccount() =
        GoogleSignIn.getAccountForExtension(this, Constants.googleFitOptions)

    private var seekBarValue: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel =
            ViewModelProvider(this, ViewModelFactory(this)).get(HomeViewModel::class.java)
        setDataBinding()
        setUpObserver()

        GlobalScope.launch(Dispatchers.Main) {
            connectToGoogleFit()
        }
    }

    /**
     * set observer for live data instances
     */
    private fun setUpObserver() {
        homeViewModel.getUserStepUpdateResponse()?.observe(this, androidx.lifecycle.Observer {
        })

        homeViewModel.onApiFail()?.observe(this, androidx.lifecycle.Observer {

        })

    }

    /**
     * set up data binding
     */
    private fun setDataBinding() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.lifecycleOwner = this
        activityMainBinding.homeViewModel = homeViewModel
        activityMainBinding.tvSeekBarProgress.text = seekbar.progress.toString().plus("/").plus(seekbar.max)
        activityMainBinding.btnUpdateSteps.setOnClickListener(this)
        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                seekBarValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                tv_seek_bar_progress.text =
                    seekBarValue.toString().plus( getString(R.string.separator)).plus(seekBar.max)
            }
        })
    }

    /**
     * set up google account get details
     */
    private fun connectToGoogleFit() {
        when {
            !GoogleSignIn.hasPermissions(
                getGoogleAccount(),
                Constants.googleFitOptions
            ) -> {
                GoogleSignIn.requestPermissions(
                    this,
                    Constants.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    getGoogleAccount(),
                    Constants.googleFitOptions
                )
            }
            else -> accessGoogleFit()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                accessGoogleFit()
            }
        }
    }


    /**
     * extract required data from the fit response data set
     */
    private fun extractRespectiveDataSet(dataSet: DataSet) {
        for (dp in dataSet.dataPoints) {
            dp.dataType.fields.forEach breakLoop@{
                when (it.name) {
                    Field.FIELD_STEPS.name -> {
                        googleFitObj.userStepCount = dp.getValue(Field.FIELD_STEPS).asInt().toLong()
                        Log.e("History", "Number of Steps: " + googleFitObj.userStepCount)
                        stepsList.add(googleFitObj.userStepCount!!)
                    }

                }
            }
        }
        updateTheUiForStepData()
    }


    /**
     * update the view based on the data fetched from fit server
     */
    private fun updateTheUiForStepData(){
        if (stepsList.size == 0) {
            activityMainBinding.tvStepCountForTheDay.text = getString(R.string.no_records)
            activityMainBinding.btnUpdateSteps.isEnabled=false
            activityMainBinding.barChart.visibility=View.GONE
        } else {
            activityMainBinding.tvStepCountForTheDay.text =
                getString(R.string.step_for_the_day_prefix).plus(stepsList[stepsList.size - 1].toString())
            homeViewModel.updateStepCount(stepsList[stepsList.size - 1])
            activityMainBinding.btnUpdateSteps.isEnabled=true
            activityMainBinding.barChart.visibility=View.VISIBLE
        }
    }


    /**
     * prepare request to fetch data from google fit
     */
    private fun accessGoogleFit() {
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
            .getAccountForExtension(this, Constants.googleFitOptions)
        Fitness.getHistoryClient(this, account)
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
                if(stepsList.size>0) {
                    prepareBarChartAndSetView(stepsList)
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.d("some data", "OnFailure()", e)
            }
    }

    /**
     * prepare bar chart
     * stepList: is the array list of steps obtained from the google fit
     */
    private fun prepareBarChartAndSetView(stepsList: ArrayList<Long>) {
        val barDataSet = BarDataSet(getData(stepsList), "Steps Per Day")
        barDataSet.barBorderWidth = 0.9f
        barDataSet.color = R.color.colorAccent
        val barData = BarData(barDataSet)
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val formatter = IndexAxisValueFormatter(days)
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.animateXY(5000, 5000)
        barChart.invalidate()
    }


    /**
     * prepare bar entries from the data received
     * stepList: last 14 days list of step count.
     */
    private fun getData(stepsList: ArrayList<Long>): ArrayList<BarEntry> {
        val entries: ArrayList<BarEntry> = ArrayList()
        for (num in 0 until stepsList.size) {
            entries.add(BarEntry(num.toFloat(), stepsList[num].toFloat()))
        }
        return entries
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_update_steps -> {
                if(seekBarValue<1){
                    Toast.makeText(this,"Please add some steps to update",Toast.LENGTH_LONG).show()
                    return
                }
                val alert = AppUtils.alertBuilder("Are you sure you want to update $seekBarValue steps to Google Fit for today?", this)
                alert.setPositiveButton("Yes") { _, _ ->
                    if(GoogleSignIn.hasPermissions(
                        getGoogleAccount(),
                        Constants.googleFitOptions)) {
                            updateData()
                        }else{
                        Toast.makeText(this,"You don't have permission to update data. Please try later",Toast.LENGTH_LONG).show()
                    }
                }
                alert.setNegativeButton("cancel") { _, _ ->
                }
                val alertDialog: AlertDialog = alert.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    /**
     * Request history API with the HistoryClient object to update step data.
     */
    private fun updateData(): Task<Void?>? {
        val dataSet = insertFitnessData()
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
            this,
            GoogleSignIn.getLastSignedInAccount(this)!!
        )
            .updateData(request)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // At this point the data has been updated and can be read.
                    Log.i("TAG", "Data update was successful."+task.result)
                    AppUtils.showToast(this,"Your steps updated successfully")
                } else {
                    AppUtils.showToast(this,"Some unknown error occurred")

                    Log.e(
                        "TAG",
                        "There was a problem updating the dataset.",
                        task.exception
                    )
                }
            }
    }

//    /** Creates and returns a [DataSet] of step count data to update.  */
//    private fun updateFitnessData(): DataSet {
//        val cal = Calendar.getInstance()
//        val now = Date()
//        cal.time = now
//        cal.add(Calendar.MINUTE, 0)
//        val endTime = cal.timeInMillis
//        cal.add(Calendar.HOUR_OF_DAY, -1)
//        val startTime = cal.timeInMillis
//        val dataSource = DataSource.Builder()
//            .setAppPackageName(this.packageName)
//            .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
//            .setStreamName("activity_update_steps")
//            .setType(DataSource.TYPE_RAW)
//            .build()
//        val dataSet = DataSet.create(dataSource)
//        val dataPoint = DataPoint.builder(dataSource)
//            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
//            .setField(Field.FIELD_STEPS,"")
//            .setIntValues(seekBarValue)
//            .build()
//        dataSet.add(dataPoint)
//        return dataSet
//    }

    /**
     * Creates and returns a data set of step count data for updating step for today.
     */
    private fun insertFitnessData(): DataSet {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        val startTime = calendar.timeInMillis

        // Create a data source
        val dataSource = DataSource.Builder()
            .setAppPackageName(this.packageName)
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setStreamName("TAG - step count")
            .setType(DataSource.TYPE_RAW)
            .build()

        // Create a data set
        return DataSet.builder(dataSource)
            .add(DataPoint.builder(dataSource)
                .setField(Field.FIELD_STEPS, seekBarValue)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .build()
            ).build()
    }


}