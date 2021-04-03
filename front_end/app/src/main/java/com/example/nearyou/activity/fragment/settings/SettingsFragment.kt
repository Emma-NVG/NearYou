package com.example.nearyou.activity.fragment.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.nearyou.activity.MainActivity
import com.example.nearyou.databinding.FragmentSettingsBinding
import com.example.nearyou.model.database.DatabaseHandler
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val frenchLang: ConstraintLayout = binding.containerFr
        val englishLang: ConstraintLayout = binding.containerEn
        val switch: SwitchMaterial = binding.switchTheme

        //change appearance of toggle according to state in database
        switch.isChecked = DatabaseHandler(requireContext()).getConf("theme") == "black"

        // To listen for a switch's checked/unchecked state changes
        switch.setOnCheckedChangeListener { _, _ ->

            when (DatabaseHandler(requireContext()).getConf("theme")) {

                "light" -> {
                    if (switch.isChecked) {
                        DatabaseHandler(requireContext()).changeConf("theme", "black")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }

                "black" -> {
                    if (!switch.isChecked) {
                        DatabaseHandler(requireContext()).changeConf("theme", "light")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }

            }
        }

        frenchLang.setOnClickListener() {
            DatabaseHandler(requireContext()).changeConf("language", "fr")
            val res = DatabaseHandler(requireContext()).getConf("language")

            Log.e("lang", res)
        }
        englishLang.setOnClickListener() {
            DatabaseHandler(requireContext()).changeConf("language", "en")
            val res = DatabaseHandler(requireContext()).getConf("language")

            Log.e("lang", res)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}