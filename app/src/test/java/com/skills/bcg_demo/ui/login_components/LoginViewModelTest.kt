package com.skills.bcg_demo.ui.login_components

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.skills.bcg_demo.TestCoroutineRule
import com.skills.bcg_demo.data.network.ApiDataSource
import com.skills.bcg_demo.data.network.RequestInterface
import com.skills.bcg_demo.models.ApiModels
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response.success
import javax.annotation.Resource

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class LoginViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: ApiDataSource

    @Mock
    private lateinit var requestInterface: RequestInterface
    @Mock
    private lateinit var userLogin: ApiModels.UserLoginRequest

    @Mock
    private lateinit var userLoginResult: MutableLiveData<ApiModels.UserLoginResponse>

    @Before
    fun setUp() {

    }




    @After
    fun tearDown() {
    }
}