package com.example.nearyou.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.nearyou.databinding.ActivityInscriptionBinding
import com.example.nearyou.model.credential.SignCredential
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InscriptionActivity : Activity() {
    private lateinit var binding: ActivityInscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val input_mail: EditText = binding.inputMail
        val input_password: EditText = binding.inputPassword
        val input_password_c: EditText = binding.inputPasswordC
        val input_age: EditText = binding.inputAge
        val input_name: EditText = binding.inputName
        val input_fname: EditText = binding.inputFname

        val btnC: Button = binding.buttonConnection
        val btnI: Button = binding.buttonInscription

        input_mail.addTextChangedListener {
            if (it.toString().isEmpty()){
                input_mail.setError("Veuillez entrer une adresse mail")
            }else{
                if (! isEmailValid( it.toString())) {
                    input_mail.setError("Adresse mail invalide")
                }
            }
        }

        //TODO refactor en fonction/method extract
        btnI.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                if (input_age.toString().isEmpty() || input_fname.toString().isEmpty() || input_password.toString().isEmpty() || input_age.toString().isEmpty() ||input_name.toString().isEmpty() || input_password_c.toString().isEmpty() ) {
                    if(isEmailValid(input_mail.text.toString())){
                        if (input_password.text.toString().equals(input_password_c.text.toString())) {
                            val signCredentials = SignCredential(input_mail.text.toString(),input_password.text.toString(),input_name.text.toString(),input_fname.text.toString(), Integer.parseInt(input_age.text.toString()))
                            UserDAO.signup(signCredentials)
                            val mainActivity = Intent(this@InscriptionActivity, MainActivity::class.java)
                            startActivity(mainActivity)
                        }else {
                            input_password_c.setError("Mot de passe différent")
                        }
                    }
                }
            }
        }

        btnC.setOnClickListener {
            val loginActivity = Intent(this@InscriptionActivity, LoginActivity::class.java)
            startActivity(loginActivity)
        }

    }

    //TODO refactor
    private fun isEmailValid(email: String): Boolean {
        val cs: CharSequence = email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches()
    }

}