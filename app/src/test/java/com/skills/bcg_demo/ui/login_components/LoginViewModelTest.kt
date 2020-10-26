package com.skills.bcg_demo.ui.login_components

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.skills.bcg_demo.TestCoroutineRule
import com.example.network_client_module.network.ApiDataSource
import com.example.network_client_module.network.RequestInterface
import com.skills.bcg_demo.models.ApiModels
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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