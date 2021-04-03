package com.example.nearyou.activity.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.nearyou.databinding.FragmentSettingsBinding
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

        // To listen for a switch's checked/unchecked state changes
//        switch.setOnCheckedChangeListener { buttonView, isChecked
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}