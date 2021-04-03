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
import java.lang.Integer.parseInt


class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null

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
        val firstName: EditText = binding.firstname
        val status: EditText = binding.status
        val addBtn: Button = binding.addBtn
        val saveBtn: Button = binding.saveBtn

        age.setText(UserDAO.user?.age.toString())
        name.setText(UserDAO.user?.surname.toString())
        firstName.setText(UserDAO.user?.first_name.toString())
        status.setText(UserDAO.user?.custom_status.toString())

        val mutableListLink: MutableList<Link> = UserDAO.user?.links!!.toMutableList()
        val recyclerView = binding.listLink

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = LinkListRecyclerAdapter(mutableListLink, requireContext())

        addBtn.setOnClickListener {
            mutableListLink.add(Link(mutableListLink.size, "","",""))
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        saveBtn.setOnClickListener {
            val list = arrayOf(
                age,
                name,
                firstName,
                status,
            )
            val listEmptyInput = list.filter { it.text.isEmpty() }

            if (listEmptyInput.isEmpty()) {
                if (parseInt(age.text.toString()) < 130) {
                    CoroutineScope(Dispatchers.Main).launch {
                        UserDAO.user?.age = parseInt(age.text.toString())
                        UserDAO.user?.surname = name.text.toString()
                        UserDAO.user?.first_name = firstName.text.toString()
                        UserDAO.user?.custom_status = status.text.toString()
                        // UserDAO.user?.links = adapter.getItems()

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
