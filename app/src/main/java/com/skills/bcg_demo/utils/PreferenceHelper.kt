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


//    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
//
//    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
//        val editMe = edit()
//        operation(editMe)
//        editMe.apply()
//    }
//
//    var SharedPreferences.userId
//        get() = getInt(USER_ID, 0)
//        set(value) {
//            editMe {
//                it.putInt(USER_ID, value)
//            }
//        }
//
//    var SharedPreferences.password
//        get() = getString(USER_PASSWORD, "")
//        set(value) {
//            editMe {
//                it.putString(USER_PASSWORD, value)
//            }
//        }
//
//    var SharedPreferences.clearValues
//        get() = { }
//        set(value) {
//            editMe {
//                it.clear()
//            }
//        }



