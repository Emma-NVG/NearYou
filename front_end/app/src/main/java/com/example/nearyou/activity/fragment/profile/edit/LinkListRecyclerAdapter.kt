package com.example.nearyou.activity.fragment.profile.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.model.Link

class LinkListRecyclerAdapter(var dataMedia: MutableList<Link>, private val ctx: Context) :
    RecyclerView.Adapter<LinkListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaLink: EditText = view.findViewById(R.id.link_modify)
        val mediaDeleteBtn: Button = view.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_list_link_modify, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val media = dataMedia[index]

        viewHolder.mediaLink.hint = media.link

        viewHolder.mediaDeleteBtn.setOnClickListener {
            dataMedia.removeAt(index)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dataMedia.size
    }
}