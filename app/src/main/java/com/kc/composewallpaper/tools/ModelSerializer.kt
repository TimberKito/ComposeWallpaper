package com.kc.composewallpaper.tools

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kc.composewallpaper.model.RootModel
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

object Json2ModelSerializer {
    fun parseJsonFile(jsonInputStream: InputStream): List<RootModel> {
        val reader = InputStreamReader(jsonInputStream)
        val jsonString = reader.readText()
        val type = object : TypeToken<MutableList<RootModel>>() {}.type
        return parseArray<MutableList<RootModel>>(jsonString, type)
    }
}

inline fun <reified T> parseArray(json: String, typeToken: Type): T {
    val gson = GsonBuilder().create()
    return gson.fromJson<T>(json, typeToken)
}




