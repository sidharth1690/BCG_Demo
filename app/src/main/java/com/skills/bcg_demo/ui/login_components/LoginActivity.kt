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
 * Filename:LoginActivity.kt
 * Author:
 * Creation Date: 20/10/2020 09:30 AM
 *
 * ****************************************************
 * ******************************************************
 */
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
import com.skills.bcg_demo.utils.AppUtils
import com.skills.bcg_demo.utils.AppUtils.isEmailValid
import com.skills.bcg_demo.utils.PreferenceHelper

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mLoginViewModel: LoginViewModel
    private lateinit var mActivityLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(PreferenceHelper.readValueFromPreferences(PreferenceHelper.USER_ID,this).isNotEmpty()){
            redirectToMainActivity()
        }
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setDataBinding()
        setUpAllObserver()
    }

    /**
     * set up binding
     */
    private fun setDataBinding() {
        mActivityLoginBinding =
            DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        mActivityLoginBinding.lifecycleOwner = this
        mActivityLoginBinding.loginViewModel = mLoginViewModel
        mActivityLoginBinding.btnLogin.setOnClickListener(this)
    }

    /**
     * set up observers for live data instances
     */
    private fun setUpAllObserver() {
        mLoginViewModel.getLoginResponseData()?.observe(this, Observer {
            // do some stuff here after login response
        })

        mLoginViewModel.onApiFail()?.observe(this, Observer {
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
                    if(mActivityLoginBinding.cbRememberMe.isChecked) {
                        PreferenceHelper.writeStringToPreferences(
                            PreferenceHelper.USER_ID,
                            mActivityLoginBinding.etEmail.toString().trim(),
                            this
                        )
                    }
                    redirectToMainActivity()
//                if(AppUtils.isNetworkConnected(this)) {
//                    mLoginViewModel.requestUserLogin(mActivityLoginBinding.etEmail.text.toString().trim(), mActivityLoginBinding.etPassword.text.toString().trim()
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
            if (mActivityLoginBinding.etEmail.text.toString().trim().isEmpty()) {
                mActivityLoginBinding.etEmail.error = getString(R.string.err_empty_email)
                mActivityLoginBinding.etEmail.requestFocus()
                return false
            } else if (!isEmailValid(mActivityLoginBinding.etEmail.text.toString().trim())) {
                mActivityLoginBinding.etEmail.error = (getString(R.string.err_invalid_email))
                mActivityLoginBinding.etEmail.requestFocus()
                return false

            } else if (mActivityLoginBinding.etPassword.text.toString().trim().isEmpty()) {
                mActivityLoginBinding.etPassword.error = getString(R.string.err_empty_password)
                mActivityLoginBinding.etPassword.requestFocus()
                return false
            }else if (mActivityLoginBinding.etPassword.text.toString().trim().length<6) {
                mActivityLoginBinding.etPassword.error = getString(R.string.err_password_length)
                mActivityLoginBinding.etPassword.requestFocus()
                return false
            }
        return true
    }


}


