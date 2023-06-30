package com.unab.bootcamp.lautipe.profeboton.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.unab.bootcamp.lautipe.profeboton.R

class CrearCodigoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CrearCodigoActivity"
    }

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_codigo)

        db = FirebaseFirestore.getInstance()

        val createButton: Button = findViewById(R.id.create_button)
        val codeEditText: EditText = findViewById(R.id.code_text_create)

        createButton.setOnClickListener {
            val sessionCode = codeEditText.text.toString().trim()

            if (sessionCode.isNotEmpty()) {
                val newSession = hashMapOf(
                    "codigo" to sessionCode
                    // Agrega cualquier otro campo que desees
                )

                // Crea una nueva sesión en Firestore
                db.collection("sesion").document(sessionCode)
                    .set(newSession)
                    .addOnSuccessListener {
                                // más campos aquí...
                             // Puedes agregar campos adicionales aquí

                        Log.d(TAG, "DocumentSnapshot successfully written!")
                        Toast.makeText(this, "Sesión creada exitosamente", Toast.LENGTH_SHORT).show()

                        // Después de que la sesión se crea exitosamente, nos movemos a la actividad TeacherActivity
                        val intent = Intent(this, TeacherActivity::class.java).apply {
                            // Pasa el sessionCode como un extra
                            putExtra("EXTRA_SESSION_CODE", sessionCode)
                        }
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                        Toast.makeText(this, "Error al crear la sesión", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, ingrese un código", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
