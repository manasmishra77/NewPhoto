package com.example.newphoto

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import android.media.MediaPlayer


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, Animator.AnimatorListener {

    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecycleManager: RecyclerView.LayoutManager
    private lateinit var rotationViewI: InfiniteRotationView

    private lateinit var mDetector: GestureDetectorCompat
    private var bgAnimator: AnimationDrawable? = null

    val screenHeight get() = Resources.getSystem().displayMetrics.heightPixels/densityOfPixel
    val screenWidth get() = Resources.getSystem().displayMetrics.widthPixels/densityOfPixel
    val densityOfPixel: Float = Resources.getSystem().displayMetrics.density
    val maximumMargin = ((screenHeight - 100)*densityOfPixel).toInt()
    val minimumMargin = 5

    var isInvitatioHolderViewPresented: Boolean = false
    var isInvitationHolderAnimationGoingOn: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureViews()
        mDetector = GestureDetectorCompat(invitationHoderview.context, this)
        //photoRecyclerView.setOnTouchListener(OnSwipeTouchListener(this))
//        photoRecyclerView.setOnTouchListener(object: OnSwipeTouchListener(this@MainActivity) {
//            override fun onSwipeDown() {
//                print("swipe")
//                handleInvitationView(isDown = true)
//            }
//
//            override fun onSwipeUp() {
//                //super.onSwipeUp()
//                print("swipe")
//                handleInvitationView(isDown = false)
//            }
//        })
    }

    private fun configureViews() {
        val layoutParams = invitationHoderview.layoutParams as? ViewGroup.MarginLayoutParams
        layoutParams?.topMargin = maximumMargin
        layoutParams?.bottomMargin = (-maximumMargin)
        invitationHoderview.requestLayout()

        //Configure Background of invitationView
        bgAnimator = invitationHoderview.background as? AnimationDrawable
        bgAnimator?.setEnterFadeDuration(6000)
        bgAnimator?.setExitFadeDuration(2000)

        configureRecyclerView()
        configureMediaPlayer()
    }


    fun configureRecyclerView() {
        var photoArr = ArrayList<Int>()
        photoArr.add(R.drawable.pe1)
        photoArr.add(R.drawable.ab2)
        photoArr.add(R.drawable.ab3)
        photoArr.add(R.drawable.ab4)
        photoArr.add(R.drawable.ab5)

//        photoAdapter = PhotoAdapter(this, photoArr)
//        photoRecycleManager = LinearLayoutManager(this, 0, false)
//        photoRecyclerView = findViewById<RecyclerView>(R.id.photoRecyclerView).apply {
//            layoutManager = photoRecycleManager
//            adapter = photoAdapter
//        }
        rotationViewI = iRView
        rotationViewI.setAdapter(InfiniteRotationAdapter(photos = photoArr))
        //rotationViewI.autoScroll(photoArr.size, 2000)

    }

    fun configureMediaPlayer() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.tenderlove)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

    }



    override fun onResume() {
        super.onResume()
        if (bgAnimator != null) {
            if (!bgAnimator!!.isRunning) {
                bgAnimator?.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (bgAnimator != null) {
            if (bgAnimator!!.isRunning) {
                bgAnimator?.stop()
            }
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
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if (velocityY > 0.5) {
            print("Down")
            handleInvitationView(isDown = true)
            return  true
        } else if (velocityY < -0.5) {
            print("Up")
            handleInvitationView(isDown = false)
            return  true
        }
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }


    private fun handleInvitationView(isDown: Boolean) {
        if (isDown) {
            hideInvitationHolderView()
        } else {
            showInvitationHolderView()
        }
    }

    private fun showInvitationHolderView() {
        val layoutParams = invitationHoderview.layoutParams as? ViewGroup.MarginLayoutParams
        var valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        if (isInvitationHolderAnimationGoingOn) {
            return
        }
        if (isInvitatioHolderViewPresented) {
            return
        }
        isInvitationHolderAnimationGoingOn = true
        isInvitatioHolderViewPresented = true

        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            layoutParams?.topMargin = (value*maximumMargin).toInt() + minimumMargin
            layoutParams?.bottomMargin = -(value*maximumMargin).toInt() + minimumMargin
            arrowIconImageView.rotation = (1f-value)*180
            invitationHoderview.requestLayout()
        }
        valueAnimator.addListener(this)
        valueAnimator.duration = 1000
        valueAnimator.start()

    }
    private fun hideInvitationHolderView() {
        val layoutParams = invitationHoderview.layoutParams as? ViewGroup.MarginLayoutParams
        var valueAnimator = ValueAnimator.ofFloat(0f, 1.0f)
        if (isInvitationHolderAnimationGoingOn) {
            return
        }
        if (!isInvitatioHolderViewPresented) {
            return
        }
        isInvitationHolderAnimationGoingOn = true
        isInvitatioHolderViewPresented = false
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            layoutParams?.topMargin = (value*maximumMargin).toInt()
            layoutParams?.bottomMargin = -(value*maximumMargin).toInt()
            arrowIconImageView.rotation = (1f-value)*180
            arrowIconImageView.requestLayout()
            invitationHoderview.requestLayout()
        }
        valueAnimator.addListener(this)
        valueAnimator.duration = 1000
        valueAnimator.start()
    }


    override fun onAnimationStart(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        isInvitationHolderAnimationGoingOn = false
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }



}
