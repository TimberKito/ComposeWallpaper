package com.kc.composewallpaper.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kc.composewallpaper.R
import com.kc.composewallpaper.DetailActivity
import com.kc.composewallpaper.adapter.ViewPagerAdapter
import com.kc.composewallpaper.tools.DataModel
import com.kc.composewallpaper.tools.RootModel

class ViewPagerFragment(private val rootModel: RootModel) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recyclerview, container, false)

        // 将RecyclerView绑定fragment
        val infoRecyclerView: RecyclerView = view.findViewById(R.id.info_recycler_view)

//        infoRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)
        infoRecyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)

        // 创建适配器
        val viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(
            requireContext(),
            rootModel,
            object : ViewPagerAdapter.OnItemClickListener {
                /**
                 * 单个图片item点击事件
                 */
                /**
                 * 单个图片item点击事件
                 */
                override fun onItemClick(position: Int, dataModel: DataModel) {
                    // 跳转详情页面并传递该页面的参数
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("KEY_EXTRA",dataModel)
                    startActivity(intent)
                }
            })

        infoRecyclerView.adapter = viewPagerAdapter

        return view
    }

}