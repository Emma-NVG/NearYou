package com.example.nearyou.activity.fragment.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nearyou.databinding.FragmentProfileEditBinding
import com.example.nearyou.model.Link
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val age: EditText = binding.age
        val name: EditText = binding.name
        val firstname: EditText = binding.firstname
        val status: EditText = binding.status
        val save: Button = binding.save

        val listLink: Array<Link> = UserDAO.user?.links!!
        val recyclerView = binding.listLink
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = LinkListRecyclerAdapter(
            listLink,
            requireContext()
        )

        save.setOnClickListener {
            val list = arrayOf(
                age,
                name,
                firstname,
                status,
            )
            val listEmptyInput = list.filter { it.text.isEmpty() }

            if (listEmptyInput.isEmpty()) {
                if (Integer.parseInt(age.text.toString()) < 130) {
                    CoroutineScope(Dispatchers.Main).launch {
//                        recyclerView.adapter!!.
                        //TODO put modifications in database
                    }
                } else {
                    age.error = "Vous êtes trop vieux"
                }
            } else {
                listEmptyInput.forEach {
                    it.error = "Pas de champ remplis, pas d'inscription !"
                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
