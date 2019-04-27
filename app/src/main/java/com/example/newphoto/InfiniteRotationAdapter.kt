package com.example.newphoto

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView


import kotlinx.android.synthetic.main.photo_recyclerview_cell.view.*


/**
 * Created by tomoaki on 2017/08/13.
 */
class InfiniteRotationAdapter(photos: ArrayList<Int>) : RecyclerView.Adapter<InfiniteRotationAdapter.PhotoViewHolder1>() {

    private val list: List<Int> = listOf(photos.last()) + photos + listOf(photos.first())



    override fun onBindViewHolder(holder: InfiniteRotationAdapter.PhotoViewHolder1, position: Int) {
        print("1111113333333333")
        holder?.let {
            val photoIndex = list[position % list.size]
            holder.photoImageView.setImageResource(photoIndex)
        }
    }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfiniteRotationAdapter.PhotoViewHolder1 {
        val view = LayoutInflater
            .from(parent?.context).inflate(R.layout.photo_recyclerview_cell, parent, false)
        return InfiniteRotationAdapter.PhotoViewHolder1(view)
    }

    override fun getItemCount(): Int {
        print("1111111111111111")
        return list.size
    }



    // stores and recycles views as they are scrolled off screen
    class PhotoViewHolder1(val photoHolderView: View): RecyclerView.ViewHolder(photoHolderView) {
        var photoImageView: ImageView

        init {
            this.photoImageView = photoHolderView.coverPhoto
        }
    }
}

