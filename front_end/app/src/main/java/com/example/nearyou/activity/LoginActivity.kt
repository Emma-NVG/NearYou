package com.example.nearyou.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.nearyou.databinding.ActivityLoginBinding
import com.example.nearyou.model.credential.LoginCredential
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val input_mail: EditText = binding.inputMail
        val input_password: EditText = binding.inputPassword
        val btnC: Button = binding.buttonConnection
        val btnI: Button = binding.buttonInscription

        input_mail.addTextChangedListener {
            if (it.toString().isEmpty()) {
                input_mail.error = "Veuillez entrer une adresse mail"
            } else {
                if (!isEmailValid(it.toString())) {
                    input_mail.error = "Adresse mail invalide"
                }
            }
        }

        btnC.setOnClickListener {
            val credentials = LoginCredential(input_mail.text.toString(), input_password.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                if (UserDAO.login(credentials).code == ResponseCode.S_SUCCESS) {
                    val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainActivity)
                }
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