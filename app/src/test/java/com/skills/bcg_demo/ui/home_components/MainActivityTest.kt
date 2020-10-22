package com.skills.bcg_demo.ui.home_components

import android.os.Build
import android.widget.Button
import android.widget.EditText
import androidx.test.rule.ActivityTestRule
import com.skills.bcg_demo.R
import com.skills.bcg_demo.ui.login_components.LoginActivity
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {


    @Rule
    @JvmField
    var activityRules = ActivityTestRule(MainActivity::class.java)
    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        activity = activityRules.activity
    }

    @Test
    fun testLaunchAndViewOfActivity(){
        val v= activity.findViewById<Button>(R.id.btn_update_steps)
        assertNotNull(v)
    }
    @After
    fun tearDown() {

    }
}