// IngresarCodigoActivity.kt
package com.unab.bootcamp.lautipe.profeboton.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.unab.bootcamp.lautipe.profeboton.R

class IngresarCodigoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "IngresarCodigoActivity"
    }

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar_codigo)

        db = FirebaseFirestore.getInstance()

        val enterButton: Button = findViewById(R.id.enter_button)
        val codeEditText: EditText = findViewById(R.id.code_text_enter)

        enterButton.setOnClickListener {
            val sessionCode = codeEditText.text.toString().trim()

            if (sessionCode.isNotEmpty()) {
                db.collection("sesion").document(sessionCode).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            // Cuando la sesión existe, comienza la nueva actividad.
                            val intent = Intent(this, StudentActivity::class.java)
                            intent.putExtra("EXTRA_SESSION_ID", sessionCode) // Pasando el código de sesión a la nueva actividad
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "La sesión no existe", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            } else {
                Toast.makeText(this, "Por favor, ingresa un código", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
