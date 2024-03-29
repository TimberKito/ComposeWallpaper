package com.kc.composewallpaper.tools

import com.google.gson.Gson
import com.kc.composewallpaper.model.RootModel
import java.io.InputStream
import java.io.InputStreamReader

object Json2ModelSerializer {
    fun parseJsonFile(jsonInputStream: InputStream): List<RootModel> {
        val reader = InputStreamReader(jsonInputStream)
        val jsonString = reader.readText()
        return Gson().fromJson(jsonString, Array<RootModel>::class.java).toList()
    }
}

