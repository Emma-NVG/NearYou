package com.example.nearyou.activity.fragment.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.R
import com.example.nearyou.databinding.FragmentHomeBinding
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import com.example.nearyou.model.user.member.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO vérifier si localisation est activée -> pas activé=erreur active la non d'une pipe en bois
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lateinit var listPerson: Array<Member>


        CoroutineScope(Dispatchers.Main).launch {
            val response = UserDAO.retrieveAllUserNearMe()
            listPerson = UserDAO.retrieveAllUserNearMe().data
            Log.e("test", response.toString())
            val title: TextView = binding.title
            when (response.code) {
                ResponseCode.S_SUCCESS -> {

                    if (listPerson.isNotEmpty()) {
                        recyclerView = binding.recycler
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = ListPersonRecyclerAdapter(listPerson)
                        title.text = getString(R.string.title_list_person)
                        title.setTextColor(Color.BLACK)
                    } else {
                        title.text = getString(R.string.title_list_person_empty)
                        title.setTextColor(Color.RED)
                    }

                }
                else -> {
                    title.text = getString(R.string.title_list_person_empty)
                    title.setTextColor(Color.RED)
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
