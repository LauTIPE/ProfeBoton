// MainActivity.kt (Profesor)
package com.unab.bootcamp.lautipe.profeboton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.unab.bootcamp.lautipe.profeboton.R

// Definición de la clase MainActivity que hereda de AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Constante para el tag de los logs
    companion object {
        private const val TAG = "MainActivity"
    }

    // Declaración de las variables que se usarán en la clase
    private lateinit var db: FirebaseFirestore // Instancia de Firestore

    // Función que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance() // Inicialización de la instancia de Firestore

        // Inicialización del botón y asignación de su comportamiento al hacer click
        val button: Button = findViewById(R.id.bottom_start_question)
        button.setOnClickListener {
            // Creación de un HashMap con la pregunta
            val question = hashMapOf(
                "timeStamp" to Timestamp.now(), // Marca de tiempo actual
                "responses" to arrayListOf<String>() // Lista vacía para las respuestas
            )

            // Añadir la pregunta a la colección de preguntas activas
            db.collection("Sessions").document("sessionID").collection("activeQuestions")
                .add(question)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!") // Log en caso de éxito
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error writing document", e) // Log en caso de error
                }
        }
    }
}
