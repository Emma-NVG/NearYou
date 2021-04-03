package com.example.nearyou.activity.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.extension.format
import com.example.nearyou.model.user.UserDAO
import com.example.nearyou.model.user.member.Member
import com.squareup.picasso.Picasso
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListPersonRecyclerAdapter(private val dataPerson: Array<Member>, private val navController: NavController, private val ctx: Context) :
    RecyclerView.Adapter<ListPersonRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val picture: ImageView = view.findViewById(R.id.img_profile)
        val name: TextView = view.findViewById(R.id.name)
        val age: TextView = view.findViewById(R.id.age)
        val distance: TextView = view.findViewById(R.id.distance)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_list_person, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val person = dataPerson[position]

        viewHolder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("User", Json.encodeToString(person))
            navController.navigate(R.id.action_nav_home_to_nav_profile, bundle)
        }

        Picasso.get()
                .load(if (person.url_profile.isEmpty()) "ERROR" else person.url_profile)
                .error(R.mipmap.ic_error)
                .into(viewHolder.picture)

        viewHolder.name.text = ctx.getString(R.string.display_name, UserDAO.user?.first_name, UserDAO.user?.surname)
        viewHolder.age.text = ctx.getString(R.string.display_age, person.age)
        viewHolder.distance.text = ctx.getString(R.string.display_distance, person.distance.format(1))
    }

    override fun getItemCount() = dataPerson.size
}