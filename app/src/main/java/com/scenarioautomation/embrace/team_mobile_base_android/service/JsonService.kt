package com.scenarioautomation.embrace.team_mobile_base_android.service

import android.util.Log
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonService @Inject constructor() {

    inline fun <reified T> parseFromJson(value: String): T? {
        return try {
            Gson().fromJson(value, T::class.java)
        } catch (e: Exception) {
            Log.e("JSON", "Fail to parse from json: ${e.message}")
            null
        }
    }

    inline fun <reified T> parseToJson(json: T): String? {
        return try {
            Gson().toJson(json)
        } catch (e: Exception) {
            Log.e("JSON", "Fail to parse to json: ${e.message}")
            null
        }
    }

}