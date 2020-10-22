package com.skills.bcg_demo.utils

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class DemoApplication :Application(){


    init {
        instance = this
    }


    companion object {
        private var instance: DemoApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}