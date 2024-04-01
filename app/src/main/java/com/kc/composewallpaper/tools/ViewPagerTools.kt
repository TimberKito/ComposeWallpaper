package com.kc.composewallpaper.tools

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.nineoldandroids.view.ViewHelper
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

class RotateDownPageTransformer : ViewPager.PageTransformer {
    private var mRot = 0f
    override fun transformPage(view: View, position: Float) {
        Log.e("TAG", "$view , $position")
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setRotation(view, 0F)
        } else if (position <= 1) // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
        { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {
                mRot = ROT_MAX * position
                ViewHelper.setPivotX(view, view.measuredWidth * 0.5f)
                ViewHelper.setPivotY(view, view.measuredHeight.toFloat())
                ViewHelper.setRotation(view, mRot)
            } else {
                mRot = ROT_MAX * position
                ViewHelper.setPivotX(view, view.measuredWidth * 0.5f)
                ViewHelper.setPivotY(view, view.measuredHeight.toFloat())
                ViewHelper.setRotation(view, mRot)
            }

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setRotation(view, 0F)
        }
    }

    companion object {
        private const val ROT_MAX = 20.0f
    }
}
