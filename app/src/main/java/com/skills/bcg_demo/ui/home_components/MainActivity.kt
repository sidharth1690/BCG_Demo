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
 * Filename:MainActivity.kt
 * Author:
 * Creation Date: 20/10/2020 10:00 AM
 *
 * ****************************************************
 * ******************************************************
 */
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
import com.example.network_module.network_layer.reponse_model.ApiModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import com.skills.bcg_demo.R
import com.skills.bcg_demo.components.ViewModelFactory
import com.skills.bcg_demo.databinding.ActivityMainBinding
import com.skills.bcg_demo.google_fit_components.GoogleClient
import com.skills.bcg_demo.interfaces.GoogleDelegate
import com.skills.bcg_demo.utils.AppUtils
import com.skills.bcg_demo.utils.Constants
import com.skills.bcg_demo.utils.Constants.Companion.days
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener, GoogleDelegate {

    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var mActivityMainBinding: ActivityMainBinding
    private fun getGoogleAccount() =
        GoogleSignIn.getAccountForExtension(this, Constants.googleFitOptions)

    private var mSeekBarValue: Int = 0
    private lateinit var mGoogleClient: GoogleClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeViewModel =
            ViewModelProvider(this, ViewModelFactory(this)).get(HomeViewModel::class.java)
        setDataBinding()
        setUpObserver()
        mGoogleClient = GoogleClient(this, this)

        GlobalScope.launch(Dispatchers.Main) {
            connectToGoogleFit()
        }
    }

    /**
     * set observer for live data instances
     */
    private fun setUpObserver() {
        mHomeViewModel.getUserStepUpdateResponse()?.observe(this, androidx.lifecycle.Observer {
        })

        mHomeViewModel.onApiFail()?.observe(this, androidx.lifecycle.Observer {

        })

    }

    /**
     * set up data binding
     */
    private fun setDataBinding() {
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mActivityMainBinding.lifecycleOwner = this
        mActivityMainBinding.homeViewModel = mHomeViewModel
        mActivityMainBinding.tvSeekBarProgress.text =
            seekbar.progress.toString().plus("/").plus(seekbar.max)
        mActivityMainBinding.btnUpdateSteps.setOnClickListener(this)
        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                mSeekBarValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                tv_seek_bar_progress.text =
                    mSeekBarValue.toString().plus(getString(R.string.separator)).plus(seekBar.max)
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
            else -> mGoogleClient.accessGoogleFit()

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                mGoogleClient.accessGoogleFit()
//                accessGoogleFit()
            }
        }
    }



    /**
     * update the view based on the data fetched from fit server
     */
    private fun updateTheUiForStepData( stepList: ArrayList<Long>) {
        if (stepList.size == 0) {
            mActivityMainBinding.tvStepCountForTheDay.text = getString(R.string.no_records)
            mActivityMainBinding.btnUpdateSteps.isEnabled = false
            mActivityMainBinding.barChart.visibility = View.GONE
        } else {
            mActivityMainBinding.tvStepCountForTheDay.text =
                getString(R.string.step_for_the_day_prefix).plus(stepList[stepList.size - 1].toString())

            // server call to update the steps data to  own server
//            homeViewModel.updateStepCount(stepsList[stepsList.size - 1])

            mActivityMainBinding.btnUpdateSteps.isEnabled = true
            mActivityMainBinding.barChart.visibility = View.VISIBLE
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
                if (mSeekBarValue < 1) {
                    Toast.makeText(this, "Please add some steps to update", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                val alert = AppUtils.alertBuilder(
                    "Are you sure you want to update $mSeekBarValue steps to Google Fit for today?",
                    this
                )
                alert.setPositiveButton("Yes") { _, _ ->
                    if (GoogleSignIn.hasPermissions(
                            getGoogleAccount(),
                            Constants.googleFitOptions
                        )
                    ) {
                        mGoogleClient.updateData(mSeekBarValue)
                    } else {
                        Toast.makeText(
                            this,
                            "You don't have permission to update data. Please try later",
                            Toast.LENGTH_LONG
                        ).show()
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


    override fun onGoogleFitDataFetchSuccess(mStepsList: java.util.ArrayList<Long>) {
        updateTheUiForStepData(mStepsList)
        if (mStepsList.size > 0) {
            prepareBarChartAndSetView(mStepsList)
        }
    }

    override fun onGoogleFitDataFetchFailure() {
        TODO("Not yet implemented")
    }


}
