package com.example.nearyou.activity.fragment.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nearyou.R
import com.example.nearyou.databinding.FragmentProfileEditBinding
import com.example.nearyou.model.Link
import com.example.nearyou.model.response.ResponseCode
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
                    if (status.text.length > 150) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result = UserDAO.updateUserData(
                                    name.text.toString(),
                                    firstName.text.toString(),
                                    parseInt(age.text.toString()),
                                    status.text.toString(),
                                    true,
                                    mutableListLink.filter { it.link.isNotBlank() }.toTypedArray()
                            )

                            when (result.code) {
                                ResponseCode.S_SUCCESS -> {
                                    Toast.makeText(context, R.string.user_data_updated, Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_nav_profile_edit_to_nav_profile)
                                }
                                ResponseCode.E_NO_INTERNET -> {
                                    Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show()
                                }
                                ResponseCode.E_UNAUTHORIZED -> {
                                    Toast.makeText(context, R.string.user_data_unhautorized, Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_nav_profile_edit_to_nav_profile)
                                }
                                else -> Toast.makeText(context, R.string.unknown_error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        status.error = getString(R.string.alert_status_too_long)
                    }
                } else {
                    age.error = getString(R.string.alert_too_old)
                }
            } else {
                listEmptyInput.forEach {
                    it.error = getString(R.string.alert_no_input)
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
