package com.example.swiperefreshtest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabTransactionAdapter(fragmentManager: FragmentManager):
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments: ArrayList<TransactionListFragment> = ArrayList<TransactionListFragment>()

    init {
        repeat(3) { fragments.add(TransactionListFragment()) }
    }

    override fun getCount() = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int) = "Frag $position"
}