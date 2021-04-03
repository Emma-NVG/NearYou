package com.example.nearyou.activity.fragment.settings

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.nearyou.activity.LoadingActivity
import com.example.nearyou.databinding.FragmentSettingsBinding
import com.example.nearyou.model.database.DatabaseHandler
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.*


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

        // change appearance of toggle according to state in database
        initializeDisplay()

        // To listen for a switch's checked/unchecked state changes
        switch.setOnCheckedChangeListener { _, _ ->

            when (DatabaseHandler(requireContext()).getConf("theme")) {

                "light" -> {
                    if (switch.isChecked) {
                        DatabaseHandler(requireContext()).changeConf("theme", "black")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        requireContext().startActivity(Intent(requireContext(), LoadingActivity::class.java))
                        activity?.finishAffinity()
                    }
                }

                "black" -> {
                    if (!switch.isChecked) {
                        DatabaseHandler(requireContext()).changeConf("theme", "light")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        requireContext().startActivity(Intent(requireContext(), LoadingActivity::class.java))
                        activity?.finishAffinity()
                    }
                }

            }
        }

        frenchLang.setOnClickListener {
            DatabaseHandler(requireContext()).changeConf("language", "fr")
            changeLanguage("fr")
        }
        englishLang.setOnClickListener {
            DatabaseHandler(requireContext()).changeConf("language", "en")
            changeLanguage("en")
        }

        return root
    }

    private fun initializeDisplay() {
        val frenchLang: ConstraintLayout = binding.containerFr
        val englishLang: ConstraintLayout = binding.containerEn
        val switch: SwitchMaterial = binding.switchTheme

        switch.isChecked = DatabaseHandler(requireContext()).getConf("theme") == "black"

        when (DatabaseHandler(requireContext()).getConf("language")) {
            "fr" -> {
                frenchLang.isEnabled = false

                binding.labelFrFlag.setTextColor(Color.BLACK)
                binding.labelFrFlag.setTypeface(null, Typeface.BOLD)

                binding.labelEnFlag.setTextColor(Color.GRAY)
                binding.labelEnFlag.setTypeface(null, Typeface.NORMAL)
            }
            "en" -> {
                englishLang.isEnabled = false

                binding.labelEnFlag.setTextColor(Color.BLACK)
                binding.labelEnFlag.setTypeface(null, Typeface.BOLD)

                binding.labelFrFlag.setTextColor(Color.GRAY)
                binding.labelFrFlag.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    private fun changeLanguage(language: String) {
        val conf: Configuration = resources.configuration
        conf.setLocale(Locale(language))
        resources.updateConfiguration(conf, resources.displayMetrics)

        requireContext().startActivity(Intent(requireContext(), LoadingActivity::class.java))
        activity?.finishAffinity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}