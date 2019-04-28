package com.example.newphoto

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.HORIZONTAL
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_infinite_rotation.view.*
import java.util.*

/**
 * Created by tomoaki on 2017/08/13.
 */


class InfiniteRotationView(context: Context, attributeSet: AttributeSet)
    : RelativeLayout(context, attributeSet) {

    internal var recyclerView: RecyclerView

    private val layoutManager: CustomLayoutManager
    private lateinit var onScrollListener: OnScrollListener

   // private var dispose: Disposable? = null
    private var timer: Timer? = null

    init {
        View.inflate(context, R.layout.view_infinite_rotation, this)
        recyclerView = recyclerView_horizontalList
        layoutManager = CustomLayoutManager(context)
    }

    fun setAdapter(adapter: InfiniteRotationAdapter) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        adapter.itemCount
            .takeIf { it > 1 }
            ?.apply {
                onScrollListener = OnScrollListener(
                    adapter.itemCount,
                    layoutManager)
                    {
                        // When dragging, we assume user swiped. So we will stop auto rotation
                        if (it == RecyclerView.SCROLL_STATE_DRAGGING) {
                            //dispose?.dispose()
                            timer?.cancel()
                            timer = null
                            autoScroll(intervalInMillis = 5000)
                        }
                    }
                recyclerView.addOnScrollListener(onScrollListener)
                recyclerView.scrollToPosition(1)
            }
    }

    fun autoScroll(intervalInMillis: Long) {
        timer?.let {
            return
        }

        timer = Timer()
        timer?.scheduleAtFixedRate(CustomTimerTask(layoutManager, recyclerView), 0.0.toLong(), intervalInMillis)

    }

    fun disableScrolling() {
        stopAutoScroll()
        layoutManager.disableScrolling()
    }

    fun enableScrolling() {
        autoScroll(5000)
        layoutManager.enableScrolling()
    }

    fun stopAutoScroll() {
        timer?.cancel()
        timer = null
    }

    class CustomTimerTask(val layoutManager: LinearLayoutManager, val recyclerView: RecyclerView): TimerTask() {

        override fun run() {
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            recyclerView.smoothScrollToPosition(firstItemVisible + 1)
        }

    }

    class OnScrollListener(
        val itemCount: Int,
        val layoutManager: LinearLayoutManager,
        val stateChanged: (Int) -> Unit) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()

            if (firstItemVisible > 0 && firstItemVisible % (itemCount - 1) == 0) {
                // When position reaches end of the list, it should go back to the beginning
                recyclerView?.scrollToPosition(1)
            } else if (firstItemVisible == 0) {
                // When position reaches beginning of the list, it should go back to the end
                recyclerView?.scrollToPosition(itemCount - 1)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            stateChanged(newState)
        }
    }

    class CustomLayoutManager(context: Context): LinearLayoutManager(context, HORIZONTAL, false) {
        private var scrollable = true
        fun enableScrolling() {
            scrollable = true
        }

        fun disableScrolling() {
            scrollable = false
        }


        override fun canScrollHorizontally(): Boolean {
            return super.canScrollHorizontally() && scrollable
        }

        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}

