package com.example.newphoto

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
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
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, Animator.AnimatorListener {

    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecycleManager: RecyclerView.LayoutManager

    private lateinit var mDetector: GestureDetectorCompat
    private var bgAnimator: AnimationDrawable? = null

    val screenHeight get() = Resources.getSystem().displayMetrics.heightPixels/densityOfPixel
    val screenWidth get() = Resources.getSystem().displayMetrics.widthPixels/densityOfPixel
    val densityOfPixel: Float = Resources.getSystem().displayMetrics.density
    val initialTopMargin = ((screenHeight - 50)*densityOfPixel).toInt()

    var isInvitatioHolderViewPresented: Boolean = false
    var isInvitationHolderAnimationGoingOn: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureViews()
        mDetector = GestureDetectorCompat(this, this)
    }

    private fun configureViews() {
//        this.window.decorView.apply {
//            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val layoutParams = invitationHoderview.layoutParams as? ViewGroup.MarginLayoutParams
        layoutParams?.topMargin = initialTopMargin
        layoutParams?.bottomMargin = (- screenHeight).toInt()
        invitationHoderview.requestLayout()

        //Configure Background of invitationView
        bgAnimator = invitationHoderview.background as? AnimationDrawable
        bgAnimator?.setEnterFadeDuration(6000)
        bgAnimator?.setExitFadeDuration(2000)

        //configureRecyclerView()
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
        var valueAnimator = ValueAnimator.ofInt(initialTopMargin, 5)
        if (isInvitationHolderAnimationGoingOn) {
            return
        }
        if (isInvitatioHolderViewPresented) {
            return
        }
        isInvitationHolderAnimationGoingOn = true
        isInvitatioHolderViewPresented = true

        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            layoutParams?.topMargin = value
            arrowIconImageView.rotation = 180f
            invitationHoderview.requestLayout()
        }
        valueAnimator.addListener(this)
        valueAnimator.duration = 1000
        valueAnimator.start()

    }
    private fun hideInvitationHolderView() {
        val layoutParams = invitationHoderview.layoutParams as? ViewGroup.MarginLayoutParams
        var valueAnimator = ValueAnimator.ofInt(5, initialTopMargin)
        if (isInvitationHolderAnimationGoingOn) {
            return
        }
        if (!isInvitatioHolderViewPresented) {
            return
        }
        isInvitationHolderAnimationGoingOn = true
        isInvitatioHolderViewPresented = false
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            layoutParams?.topMargin = value
            arrowIconImageView.rotation = 0f
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
