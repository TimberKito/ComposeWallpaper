package com.kc.composewallpaper.tools

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class SystemUiController(activity: ComponentActivity) {
    private val window = activity.window

    init {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
    }

    fun setStatusBarVisibility(visible: Boolean) {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        if (visible) {
            controller?.show(WindowInsetsCompat.Type.statusBars())
        } else {
            controller?.hide(WindowInsetsCompat.Type.statusBars())
        }
    }

    fun setSystemBarsVisibility(visible: Boolean) {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        if (visible) {
            controller?.show(WindowInsetsCompat.Type.systemBars())
        } else {
            controller?.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
fun rememberSystemUiController(): SystemUiController {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    return remember(activity) { SystemUiController(activity) }
}

@Composable
fun ImmersiveStatusBarDemo() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(android.graphics.Color.TRANSPARENT)
    systemUiController.setStatusBarVisibility(true)
    systemUiController.setSystemBarsVisibility(true)
}