package com.kc.composewallpaper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kc.composewallpaper.tools.NetTools
import com.kc.composewallpaper.model.DataModel
import com.kc.composewallpaper.model.RootModel
import java.io.InputStream

class VPAdapter(
    private val context: Context, model: RootModel, private val listener: OnItemClickListener
) : RecyclerView.Adapter<VPAdapter.ThumbVH>() {

    private val infoModels = model.data

    interface OnItemClickListener {
        fun onItemClick(position: Int, dataModel: DataModel)
    }

    inner class ThumbVH(view: View) : RecyclerView.ViewHolder(view) {
        val imgItemView: ImageView = itemView.findViewById(R.id.image_item)
        val rootItemLayout = itemView.findViewById<LinearLayout>(R.id.root_layout)
        fun loadItemImg(context: Context, preUrl: String, imageViewThumb: ImageView) {
            try {
                Glide.get(context).registry.replace<GlideUrl, InputStream>(
                    GlideUrl::class.java, InputStream::class.java,
                    OkHttpUrlLoader.Factory(NetTools.getOkHttpClient())
                )
                Glide.with(context)
                    .load(preUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.png_loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.png_loading_err)
                    .into(imageViewThumb)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbVH {
        val view = LayoutInflater.from(context).inflate(R.layout.img_item, parent, false)
        return ThumbVH(view)
    }

    override fun onBindViewHolder(holder: ThumbVH, position: Int) {
        val infoModel = infoModels[position % infoModels.size]
        holder.loadItemImg(context, infoModel.previewThumb, holder.imgItemView)
        holder.rootItemLayout.setOnClickListener {
            listener.onItemClick(position, infoModel)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}
