package com.kc.composewallpaper.model

import java.io.Serializable
data class RootModel(
    var name: String, var data: List<DataModel>
) : Serializable {}