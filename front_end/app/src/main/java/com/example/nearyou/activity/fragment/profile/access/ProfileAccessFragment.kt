package com.example.nearyou.activity.fragment.profile.access

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nearyou.databinding.FragmentProfileAccessBinding
import com.example.nearyou.model.user.UserDAO
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ProfileAccessFragment : Fragment() {

    private var _binding: FragmentProfileAccessBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileAccessBinding.inflate(inflater, container, false)

        val name: TextView = binding.name
        val age: TextView = binding.age
        val status: TextView = binding.status

        Picasso.get()
            .load(UserDAO.user!!.url_profile)
            .into(binding.imgProfile)

        name.text = "${UserDAO.user?.first_name.toString()} ${
            UserDAO.user?.surname.toString().toUpperCase()
        }"
        age.text = "${UserDAO.user?.age.toString()} ans"
        status.text = UserDAO.user?.custom_status.toString()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (UserDAO.user != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val rootObject= JSONObject()
                rootObject.put("token", UserDAO.user!!.token)
                rootObject.put("id", UserDAO.user!!.ID)

                val bitmap = generateQRCode(rootObject.toString())
                withContext(Dispatchers.Main) {
                    binding.qrCode.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun generateQRCode(text: String): Bitmap {
        val width = 800
        val height = 800
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
        }
        return bitmap
    }
}
