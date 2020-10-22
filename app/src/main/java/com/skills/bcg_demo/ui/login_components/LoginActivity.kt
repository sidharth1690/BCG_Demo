package com.skills.bcg_demo.ui.login_components

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.skills.bcg_demo.R
import com.skills.bcg_demo.databinding.ActivityLoginBinding
import com.skills.bcg_demo.ui.home_components.MainActivity
import com.skills.bcg_demo.utils.AppUtils.isEmailValid
import com.skills.bcg_demo.utils.PreferenceHelper

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var activityLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(PreferenceHelper.readValueFromPreferences(PreferenceHelper.USER_ID,this).isNotEmpty()){
            redirectToMainActivity()
        }
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setDataBinding()
        setUpAllObserver()
    }

    /**
     * set up binding
     */
    private fun setDataBinding() {
        activityLoginBinding =
            DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        activityLoginBinding.lifecycleOwner = this
        activityLoginBinding.loginViewModel = loginViewModel
        activityLoginBinding.btnLogin.setOnClickListener(this)
    }

    /**
     * set up observers for live data instances
     */
    private fun setUpAllObserver() {
        loginViewModel.getLoginResponseData()?.observe(this, Observer {
            // do some stuff here after login response
        })

        loginViewModel.onApiFail()?.observe(this, Observer {
            // do some stuff here after login fail response
        })
    }

    /**
     * redirect user to main activity
     */
    private fun redirectToMainActivity(){
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_login->{
                if(validUserInput()){
                    if(activityLoginBinding.cbRememberMe.isChecked) {
                        PreferenceHelper.writeStringToPreferences(
                            PreferenceHelper.USER_ID,
                            activityLoginBinding.etEmail.toString().trim(),
                            this
                        )
                    }
                    redirectToMainActivity()
//                if(AppUtils.isNetworkConnected(this)) {
//                loginViewModel.requestUserLogin(
//                    it?.email.toString().trim(),
//                    it?.password.toString().trim()
//                )
//                }else{
//                    AppUtils.showToast(this,getString(R.string.err_internet_check))
//                }
                }
            }

        }
    }

    /**
     * validate user input
     * we can move this to view model with live data linked to binding
     */
    private fun validUserInput(): Boolean {
            if (activityLoginBinding.etEmail.text.toString().trim().isEmpty()) {
                activityLoginBinding.etEmail.error = getString(R.string.err_empty_email)
                activityLoginBinding.etEmail.requestFocus()
                return false
            } else if (!isEmailValid(activityLoginBinding.etEmail.text.toString().trim())) {
                activityLoginBinding.etEmail.error = (getString(R.string.err_invalid_email))
                activityLoginBinding.etEmail.requestFocus()
                return false

            } else if (activityLoginBinding.etPassword.text.toString().trim().isEmpty()) {
                activityLoginBinding.etPassword.error = getString(R.string.err_empty_password)
                activityLoginBinding.etPassword.requestFocus()
                return false
            }else if (activityLoginBinding.etPassword.text.toString().trim().length<6) {
                activityLoginBinding.etPassword.error = getString(R.string.err_password_length)
                activityLoginBinding.etPassword.requestFocus()
                return false
            }
        return true
    }


}


