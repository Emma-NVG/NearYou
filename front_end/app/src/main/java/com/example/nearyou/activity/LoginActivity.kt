package com.example.nearyou.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.nearyou.databinding.ActivityLoginBinding
import com.example.nearyou.model.credential.LoginCredential
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : Activity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputMail: EditText = binding.inputMail
        val inputPassword: EditText = binding.inputPassword
        val btnC: Button = binding.buttonConnection
        val btnI: Button = binding.buttonInscription

        btnC.setOnClickListener {
            if (isEmailValid(inputMail.text.toString())) {
                val credentials = LoginCredential(inputMail.text.toString(), inputPassword.text.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    val response = UserDAO.login(credentials)
                    if (response.code == ResponseCode.S_SUCCESS) {
                        val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(mainActivity)
                    } else {
                        when (response.code) {
                            ResponseCode.E_EMAIL_TOO_LONG -> {
                                inputMail.error = "Adresse mail trop longue !"
                            }
                            ResponseCode.E_BAD_EMAIL_FORMAT -> {
                                inputMail.error = "Adresse mail invalide !"
                            }
                            ResponseCode.E_WRONG_CREDENTIALS -> {
                                inputMail.error = "L'adresse mail ne correspond pas au mot de passe"
                            }
                        }
                    }
                }
            } else {
                inputMail.error = "Adresse mail invalide"
            }
        }

        btnI.setOnClickListener {
            val inscriptionActivity = Intent(this@LoginActivity, InscriptionActivity::class.java)
            startActivity(inscriptionActivity)
        }

    }

    private fun isEmailValid(email: String): Boolean {
        val cs: CharSequence = email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches()
    }

}

//TODO rename files in signin signup