package com.example.swiperefreshtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swiperefreshtest.dummy.DummyContent
import kotlin.random.Random

class TransactionListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var scrollListener: RecyclerView.OnScrollListener? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var listPositionListener: ListPositionListener? = null

    var isTransactionListAtTheTop = true
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)
        bindViews(view)
        setupRecyclerView()
        createScrollListener()
        return view
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.adapter = MyItemRecyclerViewAdapter(DummyContent.ITEMS)
    }

    private fun createScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                notifyIfListIsAtTheTop()
            }
        }
    }

    private fun notifyIfListIsAtTheTop() {
        val firstItemVisible = linearLayoutManager?.findFirstCompletelyVisibleItemPosition()
        val newState = (firstItemVisible == 0 || firstItemVisible == RecyclerView.NO_POSITION)
        if (isTransactionListAtTheTop != newState) {
            isTransactionListAtTheTop = newState
            if (isTransactionListAtTheTop) {
                listPositionListener?.notifyListIsAtTheTop()
                Toast.makeText(context, "isTransactionListAtTheTop: true", Toast.LENGTH_SHORT)
                    .show()
            } else {
                listPositionListener?.notifyListIsNotAtTheTop()
                Toast.makeText(context, "isTransactionListAtTheTop: false", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun addListPositionListener(listPositionListener: ListPositionListener) {
        this.listPositionListener = listPositionListener
    }

    private fun removeListPositionListener() {
        this.listPositionListener = null
    }

    override fun onResume() {
        super.onResume()
        scrollListener?.let {
            recyclerView?.addOnScrollListener(it)
        }
        notifyIfListIsAtTheTop()
        addListPositionListener(parentFragment as MainFragment)
    }

    override fun onPause() {
        super.onPause()
        scrollListener?.let {
            recyclerView?.removeOnScrollListener(it)
        }
        removeListPositionListener()
    }
}