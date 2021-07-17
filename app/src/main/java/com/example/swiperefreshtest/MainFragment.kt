package com.example.swiperefreshtest

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class MainFragment : Fragment(), AppBarLayout.OnOffsetChangedListener, ListPositionListener {

    private var tabTransactionAdapter: TabTransactionAdapter? = null
    private var viewPager: ViewPager? = null
    private var appBarLayout: AppBarLayout? = null
    private var tabLayout: TabLayout? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var isAppBarLayoutFullyVisible = true
    private var isTransactionListAtTheTop = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        bindViews(view)
        setupTabTransactionAdapter()
        setupViewPager()
        setupTabLayout()
        setupPageChangeListener()
        setupSwipeRefresh()
        return view
    }

    private fun bindViews(view: View) {
        with(view) {
            viewPager = findViewById(R.id.view_pager)
            appBarLayout = findViewById(R.id.app_bar_layout)
            tabLayout = findViewById(R.id.tab_layout)
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        }
    }

    private fun setupTabTransactionAdapter() {
        tabTransactionAdapter = TabTransactionAdapter(childFragmentManager)
    }

    private fun setupViewPager() {
        viewPager?.adapter = tabTransactionAdapter
        viewPager?.currentItem = 0
        viewPager?.invalidate()
        viewPager?.offscreenPageLimit = 2
    }

    private fun setupTabLayout() {
        tabLayout?.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout?.setupWithViewPager(viewPager, true)
    }

    private fun setupPageChangeListener() {
        viewPager?.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val transactionListFragment = tabTransactionAdapter?.getItem(position) as TransactionListFragment
                isTransactionListAtTheTop = transactionListFragment.isTransactionListAtTheTop
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                appBarLayout?.setExpanded(true, true)
            }
        })
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout?.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefreshLayout?.isRefreshing = false
            }, 500);
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        isAppBarLayoutFullyVisible = (verticalOffset == 0)
        updateSwipeRefreshState()
    }

    private fun updateSwipeRefreshState() {
        swipeRefreshLayout?.isEnabled = (isAppBarLayoutFullyVisible && isTransactionListAtTheTop)
    }

    override fun notifyListIsAtTheTop() {
        isTransactionListAtTheTop = true
        updateSwipeRefreshState()
    }

    override fun notifyListIsNotAtTheTop() {
        isTransactionListAtTheTop = false
        updateSwipeRefreshState()
    }

    override fun onResume() {
        super.onResume()
        appBarLayout?.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appBarLayout?.removeOnOffsetChangedListener(this)
    }
}