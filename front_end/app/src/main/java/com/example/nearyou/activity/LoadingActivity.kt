package com.example.nearyou.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.nearyou.R
import com.example.nearyou.databinding.ActivityLoadingBinding

//TODO virer la barre en haut -> passer en Activity
class LoadingActivity : Activity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                val loginActivity = Intent(this@LoadingActivity, LoginActivity::class.java)
                startActivity(loginActivity)
            }
        })
        binding.loadingLogo.startAnimation(fadeIn)
    }
}