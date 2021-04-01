package com.example.nearyou.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.nearyou.databinding.ActivityInscriptionBinding
import com.example.nearyou.model.credential.SignCredential
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Integer.parseInt

class InscriptionActivity : Activity() {
    private lateinit var binding: ActivityInscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputMail: EditText = binding.inputMail
        val inputPassword: EditText = binding.inputPassword
        val inputPasswordC: EditText = binding.inputPasswordC
        val inputAge: EditText = binding.inputAge
        val inputName: EditText = binding.inputName
        val inputFirstname: EditText = binding.inputFname

        val btnC: Button = binding.buttonConnection
        val btnI: Button = binding.buttonInscription


        btnI.setOnClickListener {
            val list = arrayOf(
                inputMail,
                inputPassword,
                inputPasswordC,
                inputAge,
                inputName,
                inputFirstname
            )
            val listEmptyInput = list.filter { it.text.isEmpty() }

            if (listEmptyInput.isEmpty()) {
                if (parseInt(inputAge.text.toString()) < 130) {
                    if (isEmailValid(inputMail.text.toString())) {
                        if (inputPassword.text.toString() == inputPasswordC.text.toString()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val signCredentials = SignCredential(inputMail.text.toString(), inputPassword.text.toString(), inputName.text.toString(), inputFirstname.text.toString(), parseInt(inputAge.text.toString()))
                                val response = UserDAO.signUp(signCredentials)
                                when (response.code) {
                                    ResponseCode.S_SUCCESS -> {
                                        UserDAO.saveCredentialCache(signCredentials, applicationContext)
                                        startActivity(Intent(this@InscriptionActivity, MainActivity::class.java))
                                    }
                                    ResponseCode.E_AGE_TOO_YOUNG -> {
                                        inputAge.error = "Vous êtes trop jeune !"
                                    }
                                    ResponseCode.E_EMAIL_TOO_LONG -> {
                                        inputMail.error = "Adresse mail trop longue !"
                                    }
                                    ResponseCode.E_BAD_EMAIL_FORMAT -> {
                                        inputMail.error = "Adresse mail invalide !"
                                    }
                                    ResponseCode.E_EMAIL_KNOWN -> {
                                        inputMail.error = "Adresse mail déjà utilisée !"
                                    }
                                    ResponseCode.E_FIRST_NAME_TOO_LONG -> {
                                        inputFirstname.error = "Prénom trop long !"
                                    }
                                    ResponseCode.E_SURNAME_TOO_LONG -> {
                                        inputName.error = "Nom trop long !"
                                    }
                                    ResponseCode.E_PASSWORD_TOO_LONG -> {
                                        inputPassword.error = "Mot de passe trop long !"
                                    }
                                    ResponseCode.E_PASSWORD_TOO_SHORT -> {
                                        inputPassword.error = "Mot de passe trop court !"
                                    }
                                    else -> inputMail.error = "Une erreur inconnue est survenue"
                                }
                            }
                        } else {
                            inputPasswordC.error = "Mot de passe différent"
                        }
                    } else {
                        inputMail.error = "Adresse mail invalide"
                    }
                } else {
                    inputAge.error = "Vous êtes trop vieux"
                }
            } else {
                listEmptyInput.forEach {
                    it.error = "Pas de champ rempli, pas d'inscription !"
                }
            }
        }

        btnC.setOnClickListener {
            val loginActivity = Intent(this@InscriptionActivity, LoginActivity::class.java)
            startActivity(loginActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        if (UserDAO.user != null) {
            startActivity(Intent(this@InscriptionActivity, MainActivity::class.java))
        }
    }

    //TODO refactor
    private fun isEmailValid(email: String): Boolean {
        val cs: CharSequence = email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches()
    }

}