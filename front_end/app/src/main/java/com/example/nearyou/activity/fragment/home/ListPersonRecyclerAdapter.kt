package com.example.nearyou.activity.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.extension.format
import com.example.nearyou.model.user.member.Member

class ListPersonRecyclerAdapter(private val dataPerson: Array<Member>) :
    RecyclerView.Adapter<ListPersonRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        viewHolder.name.text = "${person.first_name} ${person.surname.toUpperCase()}"
        viewHolder.age.text = "${person.age} ans"
        viewHolder.distance.text = "${person.distance.format(1)} km"
    }

    override fun getItemCount() = dataPerson.size
}