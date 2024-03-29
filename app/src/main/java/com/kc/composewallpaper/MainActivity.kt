package com.kc.composewallpaper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.kc.composewallpaper.databinding.ActivityMainBinding
import com.kc.composewallpaper.tools.Json2ModelSerializer
import com.kc.composewallpaper.fragment.ViewPagerFragment
import com.kc.composewallpaper.model.RootModel
import com.kc.composewallpaper.tools.PhoneScreenTool


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentList: ArrayList<Fragment>

    /**
     * 主菜单按钮
     */
    @Composable
    fun MenuCompose() {
        Image(
            painter = painterResource(id = R.drawable.svg_menu),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .size(40.dp)
        )
    }

    /**
     * 标题
     */
    @Composable
    fun TitleCompose() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = androidx.compose.ui.graphics.Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                style = TextStyle.Default,
                modifier = Modifier
                    .wrapContentSize()
                    .height(40.dp)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        view.setPadding(0, PhoneScreenTool.getScreenSize(this), 0, 0)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE) or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        binding.menuCompose.setContent {
            MenuCompose()
        }
        binding.titleCompose.setContent {
            TitleCompose()
        }


        // 绑定抽屉中的GOOGLE按钮
        binding.layoutRate.setOnClickListener() {
            viewUrl()
        }
        // 绑定抽屉中的分享按钮
        binding.layoutShare.setOnClickListener() {
            shareUrl()
        }
        // 绑定抽屉中的版本信息
        val versionName = getVersionName()
        binding.textAppVersion.text = versionName

        // 打开抽屉
//        binding.imageMenu.setOnClickListener() {
//            binding.drawerParent.openDrawer(GravityCompat.START)
//        }

        binding.drawerParent.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }
            override fun onDrawerOpened(drawerView: View) {
                drawerView.isClickable = true
            }
            override fun onDrawerClosed(drawerView: View) {

            }
            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        // 得到wallpaperModelList 并截取 3--15
        val rootModelList: MutableList<RootModel> = mutableListOf()
        rootModelList.addAll(
            Json2ModelSerializer.parseJsonFile(assets.open("my_wallpaper.json"))
        )
        // 随机打乱List
        rootModelList.shuffle()
        // 遍历父节点数量创建Tab
        for (i in rootModelList) {
            // 添加
            binding.tabLayout.addTab(
                // 新建自定义的item_tab
                binding.tabLayout.newTab().setCustomView(R.layout.tab_item_view)
            )
        }
        fragmentList = arrayListOf()
        // 遍历赋值Tab名称
        for (i in 0 until binding.tabLayout.tabCount) {
            val tabView = binding.tabLayout.getTabAt(i)?.customView
            if (tabView != null) {
                // 给Tab赋值name
                val wallpaperModel = rootModelList[i]
                val textName = tabView.findViewById<TextView>(R.id.text_wallpaper_name)
                textName.text = wallpaperModel.name

                // 创建viewpager的fragment集合,带 wallpaperModel 参数
                fragmentList.add(ViewPagerFragment(wallpaperModel))
            }
        }

        /**
         * 设置 ViewPager 的 offscreenPageLimit 属性，指定 ViewPager 在当前页面附近应该保留多少个页面。
         * 这样可以提前加载并保留附近的页面，以提高用户体验和流畅度。
         * 通常设置为当前可见页面数量的一半或稍多一点。
         */
        binding.viewpager.offscreenPageLimit = 3
        // 创建 viewpager
        binding.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            // viewpager的数量
            override fun getCount(): Int {
                return fragmentList.size
            }

            // 绑定fragment
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getPageTitle(position: Int): CharSequence {
                return rootModelList[position].name
            }
        }
        // 将tab已viewpager对应，实现跳转
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    /**
     * 获取应用程序的版本名称
     * @return 应用程序的版本名称，如果获取失败则返回 null
     */
    private fun getVersionName(): String {
        val pInfo: PackageInfo
        try {
            pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return ""
        }
        return "Version: " + pInfo.versionName
    }

    /**
     * 打开分享链接
     */
    private fun shareUrl() {
        // 商店中包的位置
        val url = getString(R.string.share_link) + packageName
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(intent)
    }

    /**
     * 打开商店链接
     */
    private fun viewUrl() {
        // 商店中包的位置
        val url = getString(R.string.share_link) + packageName
        // 创建intent打开链接
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    /**
     * 自适应设备状态栏高度
     */
    private fun dpCovertPx(context: Context): Int {
        // 获取当前设备的屏幕密度，并赋值给变量 scale
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}