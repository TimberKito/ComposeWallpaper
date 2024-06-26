package com.kc.composewallpaper

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.tabs.TabLayout
import com.kc.composewallpaper.databinding.ActivityMainBinding
import com.kc.composewallpaper.tools.PhoneScreenTool
import com.kc.composewallpaper.model.RootModel
import com.kc.composewallpaper.tools.RotateDownPageTransformer
import com.kc.composewallpaper.model.parseJsonFromAssets


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentList: ArrayList<Fragment>

    /**
     * 主菜单按钮
     */
    @Composable
    fun MenuCompose() {
        Image(painter = painterResource(id = R.drawable.svg_menu),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .size(40.dp)
                .clickable {
                    binding.drawerParent.openDrawer(GravityCompat.START)
                })
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

    /**
     * Drawer中的Logo
     */
    @Composable
    fun DrawerTopCompose() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .height(54.dp)
                    .width(54.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.icon_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(54.dp)
                        .width(54.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            fun getVersionName(): String {
                val pInfo: PackageInfo
                try {
                    pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        packageManager.getPackageInfo(
                            packageName, PackageManager.PackageInfoFlags.of(0)
                        )
                    } else {
                        packageManager.getPackageInfo(packageName, 0)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    return ""
                }
                return "Version: " + pInfo.versionName
            }

            val versionName = getVersionName()
            Text(
                text = versionName,
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    @Composable
    fun DrawerRateCompose() {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 25.dp)
                .clickable {
                    // 商店中包的位置
                    val url = getString(R.string.share_link) + packageName
                    // 创建intent打开链接
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(url))
                    startActivity(intent)
                }) {
            Image(
                painter = painterResource(id = R.drawable.svg_google_play),
                contentDescription = null,
                modifier = Modifier.size(27.dp),
            )
            Text(
                text = stringResource(id = R.string.menu_rate),
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }

    @Composable
    fun DrawerShareCompose() {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 25.dp)
                .clickable {
                    // 商店中包的位置
                    val url = getString(R.string.share_link) + packageName
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/plain")
                    intent.putExtra(Intent.EXTRA_TEXT, url)
                    startActivity(intent)
                }) {
            Image(
                painter = painterResource(id = R.drawable.svg_share),
                contentDescription = null,
                modifier = Modifier.size(27.dp),
            )
            Text(
                text = stringResource(id = R.string.menu_share),
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 20.dp)
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

//        binding.menuCompose.setContent {
//            MenuCompose()
//        }
//        binding.titleCompose.setContent {
//            TitleCompose()
//        }

        initDrawer()
        val rootModelList: MutableList<RootModel> = mutableListOf()
       val result =  parseJsonFromAssets(this@MainActivity,"my_wallpaper.json")
        if (result != null) {
            rootModelList.addAll(result)
        }
        rootModelList.shuffle()
        for (i in rootModelList) {
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setCustomView(R.layout.tab_item)
            )
        }
        fragmentList = arrayListOf()
        for (i in 0 until binding.tabLayout.tabCount) {
            val tabView = binding.tabLayout.getTabAt(i)?.customView
            if (tabView != null) {
                // 给Tab赋值name
                val wallpaperModel = rootModelList[i]
                val textName = tabView.findViewById<TextView>(R.id.text_wallpaper_name)
                textName.text = wallpaperModel.name
                fragmentList.add(ViewPagerFragment(wallpaperModel))
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                setTabSize(p0)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
//                val customView = p0?.customView
//                if (customView != null) {
//                    val tabTextView = customView.findViewById<TextView>(R.id.text_wallpaper_name)
//                    tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.toFloat())
//                }
                p0?.customView = null
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                // null
            }

        })

        binding.viewpager.offscreenPageLimit = 3
        binding.viewpager.setPageTransformer(true, RotateDownPageTransformer())
        binding.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getPageTitle(position: Int): CharSequence {
                return rootModelList[position].name
            }
        }
        binding.tabLayout.setupWithViewPager(binding.viewpager)

        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggle_group)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.bt1 -> binding.drawerParent.closeDrawer(GravityCompat.END)
                    R.id.bt2 -> binding.drawerParent.openDrawer(GravityCompat.END)
                }
            }
        }
    }

    private fun setTabSize(p0: TabLayout.Tab?) {
        val textView = TextView(this)
        //字体样式
        val selectedSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 22f, resources.displayMetrics)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize)
        textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD) //加粗
        textView.gravity = Gravity.CENTER
        //选中的字体颜色
        textView.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
        textView.text = p0!!.text
        p0.customView = textView
    }


    private fun initDrawer() {
        binding.drawerParent.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.drawerTopCompose.setContent {
            DrawerTopCompose()
        }
        binding.drawerRateCompose.setContent {
            DrawerRateCompose()
        }
        binding.drawerShareCompose.setContent {
            DrawerShareCompose()
        }
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
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}