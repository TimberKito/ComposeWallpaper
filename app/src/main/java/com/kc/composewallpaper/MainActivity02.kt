//package com.kc.composewallpaper
//
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.TextView
//import androidx.activity.ComponentActivity
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.fragment.app.FragmentPagerAdapter
//import com.kc.composewallpaper.tools.ImgModel
//import com.kc.composewallpaper.tools.Json2ModelSerializer
//import com.kc.composewallpaper.tools.PhoneScreenTool
//import com.kc.composewallpaper.tools.ZoomOutPageTransformer
//
//class MainActivity02 : ComponentActivity() {
//    private lateinit var binding: MainActivityBinding
//    private val imgModelList: MutableList<ImgModel> = mutableListOf()
//    private lateinit var fragmentList: ArrayList<Fragment>
//
//    /**
//     * 主菜单按钮
//     */
//    @Composable
//    fun MenuCompose() {
//        Image(
//            painter = painterResource(id = R.drawable.svg_menu),
//            contentDescription = null,
//            modifier = Modifier
//                .padding(6.dp)
//                .size(40.dp)
//        )
//    }
//
//    /**
//     * 标题
//     */
//    @Composable
//    fun TitleCompose() {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .padding(4.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = stringResource(id = R.string.app_name),
//                color = Color.Black,
//                fontSize = 20.sp,
//                textAlign = TextAlign.Center,
//                style = TextStyle.Default,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .height(40.dp)
//            )
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = MainActivityBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//        view.setPadding(0, PhoneScreenTool.getScreenSize(this), 0, 0)
//        window.decorView.systemUiVisibility =
//            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE) or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        window.statusBarColor = android.graphics.Color.TRANSPARENT
//
//        binding.menuCompose.setContent {
//            MenuCompose()
//        }
//        binding.titleCompose.setContent {
//            TitleCompose()
//        }
//
//        imgModelList.addAll(
//            Json2ModelSerializer.parseJsonFile(assets.open("my_wallpaper.json"))
//        )
//        imgModelList.shuffle()
//
//
//        Log.e("imgModelList", imgModelList.toString())
//        Log.e("imgModelList", imgModelList[2].name)
//
//        // 遍历父节点数量创建Tab
//        for (i in imgModelList) {
//            // 添加
//            binding.tabLayout.addTab(
//                // 新建自定义的item_tab
//                binding.tabLayout.newTab().setCustomView(R.layout.tab_item)
//            )
//        }
//
//        fragmentList = arrayListOf()
//        // 遍历赋值Tab名称
//        for (i in 0 until binding.tabLayout.tabCount) {
//            val tabView = binding.tabLayout.getTabAt(i)?.customView
//            if (tabView != null) {
//                // 给Tab赋值name
//                val imgMode = imgModelList[i]
//                val textName = tabView.findViewById<TextView>(R.id.tab_name)
//                textName.text = imgMode.name
//                // 创建viewpager的fragment集合,带 wallpaperModel 参数
//                fragmentList.add(VPFragment(imgMode))
//            }
//        }
//
//        binding.viewpager.offscreenPageLimit = 3
//
//        binding.viewpager.adapter =
//            object : FragmentPagerAdapter(FragmentActivity().supportFragmentManager) {
//                override fun getCount(): Int {
//                    return fragmentList.size
//                }
//
//                override fun getItem(position: Int): Fragment {
//                    return fragmentList[position]
//                }
//
//                override fun getPageTitle(position: Int): CharSequence {
//                    return imgModelList[position].name
//                }
//            }
//
//        binding.viewpager.setPageTransformer(true, ZoomOutPageTransformer())
//
//        binding.tabLayout.setupWithViewPager(binding.viewpager)
//
//        Log.e("fragment", "${fragmentList.size}")
//        Log.e("fragment", "${fragmentList.size}")
//        Log.e("fragment", "${fragmentList.size}")
//    }
//
//}
//
