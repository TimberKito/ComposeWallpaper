package com.kc.composewallpaper.model

import android.content.Context
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

fun parseJsonFromAssets(context: Context, fileName: String): List<RootModel>? {
    var dataItems: List<RootModel>? = null
    try {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        inputStream.close()
        reader.close()

        val gson = Gson()
        // val typeToken = object : TypeToken<List<DataItem>>() {}.type
        // dataItems = gson.fromJson<List<DataItem>>(stringBuilder.toString(), typeToken)

        val dataItemArray = gson.fromJson(stringBuilder.toString(), Array<RootModel>::class.java)
        dataItems = dataItemArray.toList()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return dataItems
}
