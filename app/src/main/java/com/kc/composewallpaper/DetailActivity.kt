package com.kc.composewallpaper

import android.app.AlertDialog
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.kc.composewallpaper.databinding.ActivityDetailBinding
import com.kc.composewallpaper.tools.DataModel
import java.io.IOException


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    // 原图url
    private lateinit var sourceUrl: String

    private lateinit var wallpaperDownloader: WallpaperDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
        }

        // 点击返回按钮关闭
        binding.imageBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        // 点击apply按钮下载
        binding.imageWall.setOnClickListener(View.OnClickListener {
            onClickApply()
        })
        // 获取Fragment传来的数据
        val dataModel = intent.getSerializableExtra("KEY_EXTRA") as DataModel
        // 获取图片高清地址
        sourceUrl = dataModel.original

//        Log.e("sourceUrl", sourceUrl)

        // 加载图片
        Glide.with(this).load(sourceUrl)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
            // 回调方法-关闭加载动画
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    // 加载失败时的处理
                    binding.pbProgress.visibility = View.INVISIBLE
                    binding.imgLoadingErr.visibility = View.VISIBLE
                    Toast.makeText(
                        applicationContext, "Check network connection!", Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // 图片加载完成时的处理
                    binding.pbProgress.visibility = View.INVISIBLE
                    return false
                }
            }).into(binding.imageSource)
    }

    /**
     * 设置壁纸
     */
    private fun onClickApply() {
        binding.coverView.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        wallpaperDownloader = WallpaperDownloader()
        wallpaperDownloader.execute(sourceUrl)
    }

    inner class WallpaperDownloader : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            val urlString = params[0]
            var bitmap: Bitmap? = null
            try {
                bitmap = Glide.with(this@DetailActivity)
                    .asBitmap()
                    .load(urlString)
                    .submit()
                    .get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            setWallpaper(result)
        }

        // 用bitmap设置壁纸
        private fun setWallpaper(bitmap: Bitmap?) {
            if (bitmap != null) {
                val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                try {
                    wallpaperManager.setBitmap(bitmap)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.coverView.visibility = View.GONE
                    Toast.makeText(
                        applicationContext, "Wallpaper set successfully!", Toast.LENGTH_SHORT
                    ).show()
                    val builder = AlertDialog.Builder(this@DetailActivity)
                    builder.setMessage("Wallpaper set successfully!").setCancelable(false)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    val dialog = builder.create()
                    dialog.show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.coverView.visibility = View.GONE
                Toast.makeText(
                    applicationContext, "Check network connection!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}



