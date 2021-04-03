package com.example.nearyou.activity.fragment.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.model.Link
import com.squareup.picasso.Picasso

class MediasRecyclerAdapter(private val dataMedia: Array<Link>) :
    RecyclerView.Adapter<MediasRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaLogo: ImageView = view.findViewById(R.id.media_logo)
        val mediaLink: TextView = view.findViewById(R.id.media_link)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_list_link, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val media = dataMedia[index]

        Picasso.get()
                .load("ERROR")
                .error(R.mipmap.ic_error)
                .into(viewHolder.mediaLogo)

        viewHolder.mediaLink.text = media.link
    }

    override fun getItemCount(): Int {
        return dataMedia.size
    }
}