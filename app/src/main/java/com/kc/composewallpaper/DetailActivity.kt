package com.kc.composewallpaper

import android.app.AlertDialog
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.google.android.filament.Colors
import com.kc.composewallpaper.databinding.ActivityDetailBinding
import com.kc.composewallpaper.tools.DataModel
import okhttp3.internal.wait
import java.io.IOException


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sourceUrl: String

    @Composable
    fun BackCompose() {
        Image(
            painter = painterResource(id = R.drawable.svg_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .padding(10.dp)
                .wrapContentSize(),
            contentScale = ContentScale.Fit
        )
    }

    @Composable
    fun ApplyCompose() {
        Button(
            onClick = {
                binding.coverView.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                val wallpaperManager: WallpaperManager =
                    WallpaperManager.getInstance(applicationContext)
                Glide.with(this).asBitmap().load(sourceUrl).into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        try {
                            wallpaperManager.setBitmap(resource)
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            binding.coverView.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "Wallpaper set successfully!",
                                Toast.LENGTH_SHORT
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
                            Toast.makeText(
                                applicationContext,
                                "Check if the network is connected!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }, shape = RoundedCornerShape(28.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, contentColor = Color.Black
            )
        ) {
            Text(text = "Apply", color = Color.Black)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = 0
        }
        binding.backCompose.setContent {
            BackCompose()
        }
        binding.backCompose.setOnClickListener(View.OnClickListener {
            finish()
        })
        binding.applyCompose.setContent {
            ApplyCompose()
        }
        val dataModel = intent.getSerializableExtra("KEY_EXTRA") as DataModel
        sourceUrl = dataModel.original
        Glide.with(this).load(sourceUrl).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean
            ): Boolean {
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
                binding.pbProgress.visibility = View.INVISIBLE
                return false
            }
        }).into(binding.imageSource)
    }
}





