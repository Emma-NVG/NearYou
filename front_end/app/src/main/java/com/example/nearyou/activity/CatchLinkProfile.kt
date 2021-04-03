package com.example.nearyou.activity

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.nearyou.activity.fragment.profile.ProfileFragment
import com.example.nearyou.databinding.ActivityCatchLinkProfileBinding
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.member.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CatchLinkProfile : AppCompatActivity() {
    private lateinit var binding: ActivityCatchLinkProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityCatchLinkProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val appLinkIntent = intent
        val appLinkData = appLinkIntent.data

        val segments = appLinkData?.pathSegments
        val token = segments?.get(segments.size - 2).toString()
        val id = segments?.get(segments.size - 3).toString()



        CoroutineScope(Dispatchers.IO).launch {
            val result = Member.manager.retrieveData(id, token)

            withContext(Dispatchers.Main) {
                when (result.code) {
                    ResponseCode.S_SUCCESS -> {
                        val bundle = Bundle()
                        bundle.putString("User", Json.encodeToString(result.data))

                        val profileFragment = ProfileFragment()
                        profileFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .add(binding.fragment.id, ProfileFragment())
                            .commit()
                    }
                    else -> {
                        Log.e("Error", result.toString())
                    }
                }
            }


        }
    }
}