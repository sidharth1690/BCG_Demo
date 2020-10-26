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
 * Filename:PreferenceHelper.kt
 * Author:
 * Creation Date: 20/10/2020
 *
 * ****************************************************
 * ******************************************************
 */
package com.skills.bcg_demo.utils

import android.content.Context

object PreferenceHelper {

    val USER_ID = "USER_ID"

    fun writeStringToPreferences(key: String, value: String, activity: Context) {
        val sharedPreference =
            activity.getSharedPreferences("SOME_PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun readValueFromPreferences(key: String, activity: Context): String {
        return activity.getSharedPreferences("SOME_PREFERENCE_NAME", Context.MODE_PRIVATE)
            .getString(key, "").toString()
    }
}




