package com.skills.bcg_demo.components

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skills.bcg_demo.ui.home_components.HomeViewModel
import com.skills.bcg_demo.ui.login_components.LoginViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel() as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}