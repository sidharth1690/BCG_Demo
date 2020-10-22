package com.skills.bcg_demo.ui.login_components

import android.os.Build
import android.widget.EditText
import androidx.test.rule.ActivityTestRule
import com.skills.bcg_demo.R
import com.skills.bcg_demo.utils.AppUtils
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class LoginActivityTest {

    @Rule
    @JvmField
    var activityRules = ActivityTestRule(LoginActivity::class.java)
    private lateinit var activity: LoginActivity

    @Before
    fun setUp() {
        activity = activityRules.activity
    }

    @Test
    fun testLaunchAndViewOfActivity(){
        val v= activity.findViewById<EditText>(R.id.et_email)
        assertNotNull(v)
    }


    @Test
    fun checkUserInputEmailCase1(){
        assertTrue(AppUtils.isEmailValid("something@gmail.com"))
    }

    @Test
    fun checkUserInputEmailCase2(){
        assertFalse(AppUtils.isEmailValid("    something@gmail.com   "))
        assertFalse(AppUtils.isEmailValid("    something@gmail....com   "))
        assertFalse(AppUtils.isEmailValid("@gmail.com   "))
        assertFalse(AppUtils.isEmailValid(""))
    }

    @Test
    fun internetConnectionTest(){
//        when{
//            AppUtils.isNetworkConnected(mLoginActivity) -> {
//                assertTrue(JCUtils.isNetworkConnected(mLoginActivity))
//            }else -> assertFalse(JCUtils.isNetworkConnected(mLoginActivity))
//        }
    }
    @After
    fun tearDown() {
    }
}