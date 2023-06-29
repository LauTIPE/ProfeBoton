// CrearCodigoActivity.kt
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

// Definición de la clase CrearCodigoActivity que hereda de AppCompatActivity
class CrearCodigoActivity : AppCompatActivity() {

    // Constante para el tag de los logs
    companion object {
        private const val TAG = "CrearCodigoActivity"
    }

    // Declaración de las variables que se usarán en la clase
    private lateinit var db: FirebaseFirestore  // Instancia de Firestore

    // Función que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_codigo)

        db = FirebaseFirestore.getInstance() // Inicialización de la instancia de Firestore

        // Inicialización de los elementos de la vista
        val createButton: Button = findViewById(R.id.create_button)
        val codeEditText: EditText = findViewById(R.id.code_text_create)

        createButton.setOnClickListener {
            val sessionCode = codeEditText.text.toString().trim()

            if (sessionCode.isNotEmpty()) {
                // Crear una nueva sesión con el código proporcionado
                val newSession = hashMapOf(
                    "id" to sessionCode,
                    // Añadir cualquier otro campo que desees
                )

                // Agregar la nueva sesión a Firestore
                db.collection("sesion").document(sessionCode).set(newSession)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                        Toast.makeText(this, "Sesión creada exitivamente", Toast.LENGTH_SHORT).show()

                        // Después de que la sesión se crea exitosamente, nos movemos a la actividad TeacherActivity
                        val intent = Intent(this, TeacherActivity::class.java)
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
