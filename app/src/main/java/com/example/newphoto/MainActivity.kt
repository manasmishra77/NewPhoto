package com.example.newphoto

import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.LayoutDirection
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecycleManager: RecyclerView.LayoutManager

    private lateinit var mDetector: GestureDetectorCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //configureRecyclerView()
        mDetector = GestureDetectorCompat(this, this)
    }

    fun configureRecyclerView() {
        var photoArr = ArrayList<Int>()
        photoArr.add(R.drawable.flower1)
        photoArr.add(R.drawable.flower2)
        photoArr.add(R.drawable.flower3)
        photoArr.add(R.drawable.flower4)
        photoArr.add(R.drawable.flower5)

        photoAdapter = PhotoAdapter(this, photoArr)
        photoRecycleManager = LinearLayoutManager(this, 0, false)
        photoRecyclerView = findViewById<RecyclerView>(R.id.photoRecyclerView).apply {
            layoutManager = photoRecycleManager
            adapter = photoAdapter
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }


    override fun onDown(e: MotionEvent?): Boolean {
        if (e != null) {
            if (e.actionMasked == MotionEvent.ACTION_DOWN) {
                print("Down")
                showInvitationView()
            } else if (e.actionMasked == MotionEvent.ACTION_UP) {
                print("UP")
            }
        }

        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }


    fun showInvitationView() {

    animateINavitationViewPresence()
    }

    fun animateINavitationViewPresence() {
        val layoutParams = invitationHoderview.layoutParams as? ViewGroup.MarginLayoutParams



        val valueAnimator = ValueAnimator.ofInt(1800, 100)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            layoutParams?.topMargin = value
            invitationHoderview.requestLayout()
        }
        valueAnimator.duration = 1000
        valueAnimator.start()

    }



}
