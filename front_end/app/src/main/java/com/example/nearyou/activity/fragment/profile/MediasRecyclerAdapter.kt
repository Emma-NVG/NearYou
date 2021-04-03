package com.example.nearyou.activity.fragment.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.model.Link


class MediasRecyclerAdapter(private val dataMedia: Array<Link>, private val ctx: Context) :
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

        viewHolder.mediaLink.text = media.link

        viewHolder.itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(viewHolder.mediaLink.text.toString())
            startActivity(ctx, browserIntent, null)
        }

        viewHolder.mediaLink.text = media.link

        val split = (media.link).split("/")
        when {
            split[2].contains("facebook.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.facebook)
            split[2].contains("github.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.github)
            split[2].contains( "google.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.google)
            split[2].contains("youtube.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.youtube)
            split[2].contains("instagram.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.instagram)
            split[2].contains( "linkedin.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.linkedin)
            split[2].contains("pinterest.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.pinterest)
            split[2].contains( "skype.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.skype)
            split[2].contains("snapchat.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.snapchat)
            split[2].contains( "spotify.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.spotify)
            split[2].contains("twitter.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.twitter)
            split[2].contains( "whatsapp.com") -> viewHolder.mediaLogo.setImageResource(R.drawable.whatsapp)
            else -> viewHolder.mediaLogo.setImageResource(R.drawable.website)
        }
    }

    override fun getItemCount(): Int {
        return dataMedia.size
    }
}