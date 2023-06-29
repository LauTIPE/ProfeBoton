package com.unab.bootcamp.lautipe.profeboton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.unab.bootcamp.lautipe.profeboton.R

class TeacherActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TeacherActivity"
    }

    private lateinit var db: FirebaseFirestore
    private var sessionCode: String? = null  // Guarda aquí el ID de la sesión
    private var questionId: String? = null // Guarda aquí el ID de la última pregunta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        // Recupera el sessionCode del Intent
        sessionCode = intent.getStringExtra("EXTRA_SESSION_CODE")

        db = FirebaseFirestore.getInstance()

        val button: Button = findViewById(R.id.bottom_start_question)
        button.setOnClickListener {
            // Prepara la pregunta con los campos necesarios.
            // Recuerda que esto es solo un ejemplo, debes reemplazarlo con los datos que necesites.
            val question = hashMapOf(
                "estado_pregunta" to true,
                "timestamp" to Timestamp.now(),
                // más campos aquí...
            )

            // Asegúrate de que la sesión ha sido creada antes de intentar agregar una pregunta.
            if (sessionCode != null) {
                // Agrega una nueva pregunta a la sesión actual en Firestore
                db.collection("sesion").document(sessionCode!!).collection("preguntas")
                    .add(question)
                    .addOnSuccessListener { documentReference ->
                        // Guarda el ID de la pregunta para futuros usos
                        questionId = documentReference.id

                        Log.d(TAG, "Question added with ID: $questionId")

                        // Crear una referencia a la colección de respuestas
                        val responsesRef = db.collection("sesion").document(sessionCode!!).collection("preguntas").document(questionId!!).collection("respuestas")

                        // Añadir un listener a la colección de respuestas
                        responsesRef.addSnapshotListener { snapshots, e ->
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }

                            for (dc in snapshots!!.documentChanges) {
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> Log.d(TAG, "New response: ${dc.document.data}")
                                    DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified response: ${dc.document.data}")
                                    DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed response: ${dc.document.data}")
                                }
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding question", e)
                    }
            } else {
                Log.w(TAG, "Error: Session ID is null")
            }
        }

        val endClassButton: Button = findViewById(R.id.end_class_button)
        endClassButton.setOnClickListener {
            finish()
        }
    }
}
