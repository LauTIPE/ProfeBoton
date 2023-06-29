package com.unab.bootcamp.lautipe.profeboton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.unab.bootcamp.lautipe.profeboton.R


class TeacherActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TeacherActivity"
    }

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        db = FirebaseFirestore.getInstance()

        val button: Button = findViewById(R.id.bottom_start_question)
        button.setOnClickListener {
            // Aquí necesitas obtener el ID de la pregunta que quieres actualizar

            // en este ejemplo, estoy usando "questionID" como un marcador de posición
            val teacherId = "0B05EqNQzx2kmOjAEH6T"
            val questionId = "4MbODdoMvUpHArtFMnQN"
            // Crear una referencia al documento de la pregunta en Firestore
            val questionRef = db.collection("profesor").document(teacherId).collection("preguntas").document(questionId)
            // Actualizar el valor de "estado_pregunta" a true
            questionRef
                .update("estado_pregunta", true)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating document", e)
                }
        }


        val endClassButton: Button = findViewById(R.id.end_class_button)
        endClassButton.setOnClickListener {
            finish()
        }
    }
}
