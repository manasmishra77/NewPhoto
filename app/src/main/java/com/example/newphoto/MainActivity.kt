package com.example.newphoto

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var photoRecycleManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureRecyclerView()
    }

    fun configureRecyclerView() {
        var photoArr = ArrayList<Int>()
        photoArr.add(R.drawable.flower1)
        photoArr.add(R.drawable.flower2)
        photoArr.add(R.drawable.flower3)
        photoArr.add(R.drawable.flower4)
        photoArr.add(R.drawable.flower5)

        photoAdapter = PhotoAdapter(this, photoArr)
        photoRecyclerView = findViewById<RecyclerView>(R.id.photoRecyclerView).apply {
            layoutManager = photoRecycleManager
            adapter = photoAdapter
        }
    }
}
