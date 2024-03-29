package com.kc.composewallpaper.model

import java.io.Serializable
data class DataModel(
    var previewThumb: String, var original: String, var source: String
) : Serializable {}