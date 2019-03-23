package com.example.newphoto

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_recyclerview_cell.view.*
import android.view.View
import android.widget.ImageView




//class PhotoAdapter(private val photoIndexes: Array<Int>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>
class PhotoAdapter: RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private var photoIndexes: ArrayList<Int>? = null
    private var layoutInflater: LayoutInflater? = null

    // data is passed into the constructor
    constructor(context: Context, photos: ArrayList<Int>) {
        this.layoutInflater = LayoutInflater.from(context)
        this.photoIndexes = photos
    }

    //Mandatory methods
    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = layoutInflater!!.inflate(R.layout.photo_recyclerview_cell, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(p0: PhotoAdapter.PhotoViewHolder, p1: Int) {
        val photoIndex = photoIndexes!![p1]
        p0.photoImageView.setImageResource(photoIndex)
    }

    // total number of rows
    override fun getItemCount(): Int {
        return photoIndexes!!.count()
    }

    // stores and recycles views as they are scrolled off screen
    class PhotoViewHolder(val photoHolderView: View): RecyclerView.ViewHolder(photoHolderView) {
        var photoImageView: ImageView

        init {
            this.photoImageView = photoHolderView.coverPhoto
        }
    }
}