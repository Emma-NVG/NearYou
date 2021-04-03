package com.example.nearyou.activity.fragment.home

import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nearyou.R
import com.example.nearyou.databinding.FragmentHomeBinding
import com.example.nearyou.model.Permission
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import com.example.nearyou.model.user.member.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun retrieveUserNearMe() {
        // Check if geolocation active
        val lm: LocationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        binding.reload.setOnClickListener {
            retrieveUserNearMe()
        }

        val title: TextView = binding.title
        val recyclerView = binding.recycler
        if (gpsEnabled && Permission.isPermissionsAllowed(
                        arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        requireContext()
                )
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                title.visibility = View.GONE
                recyclerView.visibility = View.GONE

                val response = UserDAO.retrieveAllUserNearMe()
                val listPerson: Array<Member> = UserDAO.retrieveAllUserNearMe().data

                title.visibility = View.VISIBLE
                when (response.code) {
                    ResponseCode.S_SUCCESS -> {
                        if (listPerson.isNotEmpty()) {
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.adapter = ListPersonRecyclerAdapter(
                                    listPerson,
                                    findNavController(),
                                    requireContext()
                            )
                            recyclerView.visibility = View.VISIBLE

                            title.text = getString(R.string.title_list_person)
                            title.setTextColor(Color.BLACK)
                        } else {
                            title.text = getString(R.string.title_list_person_empty)
                            title.setTextColor(Color.RED)

                            recyclerView.visibility = View.GONE
                        }
                    }
                    ResponseCode.E_NO_INTERNET -> {
                        title.text = getString(R.string.no_internet)
                        title.setTextColor(Color.RED)

                        recyclerView.visibility = View.GONE
                    }
                    else -> {
                        title.text = getString(R.string.unknown_error)
                        title.setTextColor(Color.RED)

                        recyclerView.visibility = View.GONE
                    }
                }
            }
        } else {
            if (gpsEnabled) {
                // Permission isn't allowed
                title.text = getString(R.string.location_needed)
                title.setTextColor(Color.RED)

                recyclerView.visibility = View.GONE
            } else {
                title.text = getString(R.string.location_deactivate)
                title.setTextColor(Color.RED)

                recyclerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
