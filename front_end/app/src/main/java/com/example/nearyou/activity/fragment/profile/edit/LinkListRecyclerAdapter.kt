package com.example.nearyou.activity.fragment.profile.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.model.Link
import com.squareup.picasso.Picasso

class LinkListRecyclerAdapter(var dataMedia: Array<Link>, private val ctx: Context) :
    RecyclerView.Adapter<LinkListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaLogo: ImageView = view.findViewById(R.id.logo_modify)
        val mediaLink: EditText = view.findViewById(R.id.link_modify)
        val mediaDeleteBtn: EditText = view.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_list_link_modify, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val media = dataMedia[index]

        Picasso.get()
            .load("ERROR")
            .error(R.mipmap.ic_error)
            .into(viewHolder.mediaLogo)

        viewHolder.mediaLink.hint = media.link

        viewHolder.mediaDeleteBtn.setOnClickListener {
            val tabTemp: MutableList<Link> = dataMedia.toMutableList()
            tabTemp.removeAt(index)
            dataMedia = tabTemp.toTypedArray()
        }
    }

    override fun getItemCount(): Int {
        return dataMedia.size
    }

    fun getItems(): Array<Link> {
        return dataMedia
    }
}