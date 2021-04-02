package com.example.nearyou.activity.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nearyou.databinding.FragmentHomeBinding
import com.example.nearyou.model.user.member.Member
//TODO vérifier si localisation est activée -> pas activé=erreur active la non d'une pipe en bois
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //TODO récup personne proche ?
    private var listPerson = arrayOf(
        Member(0, "", "martin", "leo", 21, "", 1, arrayOf(), 2.0, ""),
        Member(1, "", "martine", "leo", 21, "", 1, arrayOf(), 2.0, ""),
        Member(2, "", "martina", "leo", 21, "", 1, arrayOf(), 2.0, ""),
        Member(3, "", "martin0", "leo", 21, "", 1, arrayOf(), 2.0, ""),
    )
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recycler
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ListPersonRecyclerAdapter(listPerson)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
