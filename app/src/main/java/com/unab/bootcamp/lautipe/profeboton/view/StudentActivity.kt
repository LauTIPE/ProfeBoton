package com.unab.bootcamp.lautipe.profeboton.view

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.unab.bootcamp.lautipe.profeboton.R

class StudentActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "StudentActivity"
    }

    private lateinit var db: FirebaseFirestore
    private var estadoPreguntaAnterior: Boolean? = null
    private var sessionId: String? = null
    private var questionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        db = FirebaseFirestore.getInstance()

        sessionId = intent.getStringExtra("EXTRA_SESSION_ID")

        val questionRef = db.collection("sesion").document(sessionId!!).collection("preguntas")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        questionRef.addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (!querySnapshot!!.isEmpty) {
                val document = querySnapshot.documents[0]
                questionId = document.id
                val estadoPregunta = document.getBoolean("estado_pregunta")

                if (estadoPregunta == true) {
                    val classInfo = document.getString("clase")
                    val profesor = document.getString("profesor")
                    val materia = document.getString("materia")
                    val message = document.getString("mensaje")

                    estadoPreguntaAnterior = estadoPregunta  // Actualizar el estado anterior antes de la comparación
                    updateUI(classInfo, profesor, materia, message)
                    showPopup()
                } else {
                    estadoPreguntaAnterior = estadoPregunta  // Actualizar el estado anterior
                }
            }
        }
    }

    private fun updateUI(classInfo: String?, profesor: String?, materia: String?, message: String?) {
        val classInfoTextView: TextView = findViewById(R.id.class_info)
        val messageTextView: TextView = findViewById(R.id.message)

        val classInfoText = "Clase: $classInfo\nProfesor: $profesor\nMateria: $materia"
        classInfoTextView.text = classInfoText
        messageTextView.text = message
    }

    private fun showPopup() {
        Log.d(TAG, "Showing popup")  // Agrega esto para depurar

        val popup = PopupWindow(this)
        val layout = layoutInflater.inflate(R.layout.popup_layout, null)
        popup.contentView = layout

        val optionAButton: Button = layout.findViewById(R.id.button_option_a)
        optionAButton.setOnClickListener {
            sendResponse("alternativaa")
            popup.dismiss()
        }

        val optionBButton: Button = layout.findViewById(R.id.button_option_b)
        optionBButton.setOnClickListener {
            sendResponse("alternativab")
            popup.dismiss()
        }

        popup.showAtLocation(findViewById(R.id.root_layout), Gravity.CENTER, 0, 0)
    }

    private fun sendResponse(choice: String) {
        val questionRef = db.collection("sesion").document(sessionId!!).collection("preguntas")
            .document(questionId!!)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(questionRef)

            val choiceMap = snapshot.get("alternativas_map") as Map<String, Long>
            val currentCount = choiceMap[choice] ?: 0

            transaction.update(questionRef, "alternativas_map.$choice", currentCount + 1)

            null
        }.addOnSuccessListener {
            Log.d(TAG, "Response count updated successfully!")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error updating response count", e)
        }
    }
}
