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
 * Filename:DemoApplication.kt
 * Author:
 * Creation Date: 20/10/2020
 *
 * ****************************************************
 * ******************************************************
 */
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