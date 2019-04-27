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

    internal lateinit var recyclerView: RecyclerView

    private val layoutManager: LinearLayoutManager
    private lateinit var onScrollListener: OnScrollListener

   // private var dispose: Disposable? = null
    private var timer: Timer? = null

    init {
        View.inflate(context, R.layout.view_infinite_rotation, this)
        recyclerView = recyclerView_horizontalList
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
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
                        }
                    }
                recyclerView.addOnScrollListener(onScrollListener)
                recyclerView.scrollToPosition(1)
            }
    }

    fun autoScroll(listSize: Int, intervalInMillis: Long) {

        timer?.let {
            return
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                TODO("Do something")
                val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
                recyclerView.smoothScrollToPosition(firstItemVisible + 1)
            }
        }, intervalInMillis)




//
//        dispose?.let {
//            if(!it.isDisposed) return
//        }
//        dispose = Flowable.interval(intervalInMillis, TimeUnit.MILLISECONDS)
//            .map { it % listSize + 1 }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                recyclerView.smoothScrollToPosition(it.toInt() + 1)
//            }
    }

    fun stopAutoScroll() {
        //dispose?.let(Disposable::dispose)
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
}

