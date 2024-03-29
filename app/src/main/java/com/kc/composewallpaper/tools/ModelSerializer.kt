package com.kc.composewallpaper.tools

import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Serializable

object Json2ModelSerializer {
    fun parseJsonFile(jsonInputStream: InputStream): List<RootModel> {
        val reader = InputStreamReader(jsonInputStream)
        val jsonString = reader.readText()
        return Gson().fromJson(jsonString, Array<RootModel>::class.java).toList()
    }
}

data class DataModel(
    var previewThumb: String, var original: String, var source: String
) : Serializable {}

data class RootModel(
    var name: String, var data: List<DataModel>
) : Serializable {}

