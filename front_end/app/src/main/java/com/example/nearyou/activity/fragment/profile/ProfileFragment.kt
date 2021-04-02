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
import com.example.nearyou.databinding.FragmentProfileBinding
import com.example.nearyou.model.Link
import com.example.nearyou.model.user.UserDAO

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

        val profilePicture: ImageView = binding.profilePicture

        val profileName: TextView = binding.profileName
        profileName.text = "${UserDAO.user?.first_name} ${UserDAO.user?.surname}"

        val profileSeparator: ImageView = binding.profileSeparator

        val profileAge: TextView = binding.profileAge
        profileAge.text = "${UserDAO.user?.age} ans"

        val profileStatus: TextView = binding.profileStatus
        profileStatus.text = "${UserDAO.user?.custom_status}"

        var medias = arrayOf(
            Link(0, "https://www.linkedin.com/in/leanarenon/", "", ""),
            Link(1, "https://www.facebook.com/leana.renon.96", "","")
        )

        var profileMedias: RecyclerView = binding.profileMedias
        profileMedias.layoutManager = LinearLayoutManager(context)
        profileMedias.adapter = MediasRecyclerAdapter(medias)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}