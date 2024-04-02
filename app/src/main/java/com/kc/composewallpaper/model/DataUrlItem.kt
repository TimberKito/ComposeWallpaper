package com.kc.composewallpaper.model

import java.io.Serializable

data class DataModel(
    val banner: String,
    val original: String,
    val previewThumb: String,
    val source: String
):Serializable