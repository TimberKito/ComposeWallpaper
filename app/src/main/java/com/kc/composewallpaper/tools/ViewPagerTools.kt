package com.kc.composewallpaper.tools

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

class ZoomOutPageTransformer : ViewPager.PageTransformer {
    @SuppressLint("NewApi")
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        Log.e("TAG", "$view , $position")
        if (position < -1) {
            view.setAlpha(0f)
        } else if (position <= 1) {
            val scaleFactor =
                max(MIN_SCALE.toDouble(), (1 - abs(position.toDouble())).toDouble()).toFloat()
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.translationX = horzMargin - vertMargin / 2
            } else {
                view.translationX = -horzMargin + vertMargin / 2
            }
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.setAlpha(
                MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            )
        } else {
            view.setAlpha(0f)
        }
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
}