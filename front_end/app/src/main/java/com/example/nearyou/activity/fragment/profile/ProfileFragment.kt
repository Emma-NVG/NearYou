package com.example.nearyou.activity.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.databinding.FragmentProfileBinding
import com.example.nearyou.model.Link
import com.example.nearyou.model.user.UserDAO
import com.example.nearyou.model.user.member.Member
import com.squareup.picasso.Picasso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (arguments?.get("User") != null) {
            val member = Json.decodeFromString<Member>(arguments?.get("User") as String)
            displayInformation(
                member.url_profile,
                member.surname,
                member.age,
                member.custom_status,
                member.links,
                member.first_name
            )
        } else {
            displayInformation(
                UserDAO.user!!.url_profile,
                UserDAO.user!!.surname,
                UserDAO.user!!.age,
                UserDAO.user!!.custom_status,
                UserDAO.user!!.links,
                UserDAO.user!!.first_name
            )
        }

        return root
    }

    private fun displayInformation(
        urlProfile: String,
        surname: String,
        age: Int,
        customStatus: String,
        links: Array<Link>,
        firstName: String
    ) {
        val profilePicture: ImageView = binding.profilePicture
        val profileName: TextView = binding.profileName
        val profileAge: TextView = binding.profileAge
        val profileStatus: TextView = binding.profileStatus
        val profileMedias: RecyclerView = binding.profileMedias
        profileMedias.layoutManager = LinearLayoutManager(context)

        Picasso.get()
                .load(if (urlProfile.isEmpty()) "ERROR" else urlProfile)
                .error(R.mipmap.ic_error)
                .into(profilePicture)

        profileName.text = getString(R.string.display_name, firstName, surname)

        profileAge.text = getString(R.string.display_age, age)
        profileStatus.text = customStatus
        profileMedias.adapter = MediasRecyclerAdapter(links)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}