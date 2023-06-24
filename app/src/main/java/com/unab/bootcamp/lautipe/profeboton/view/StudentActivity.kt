// StudentActivity.kt (Estudiante)
package com.unab.bootcamp.lautipe.profeboton.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.Gravity
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.unab.bootcamp.lautipe.profeboton.R

// Definición de la clase StudentActivity que hereda de AppCompatActivity
class StudentActivity : AppCompatActivity() {

    // Constante para el tag de los logs
    companion object {
        private const val TAG = "StudentActivity"
    }

    // Declaración de las variables que se usarán en la clase
    private lateinit var db: FirebaseFirestore  // Instancia de Firestore
    private lateinit var lastQuestionId: String // ID de la última pregunta

    // Función que se ejecuta al crear la actividad, en este caso crear un document sesión
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        db = FirebaseFirestore.getInstance() // Inicialización de la instancia de Firestore

        // Creación de la referencia a la sesión actual
        val sessionRef = db.collection("Sessions").document("sessionID")

        // Escucha de cambios en la colección de preguntas activas
        sessionRef.collection("activeQuestions")
            .orderBy("timeStamp", Query.Direction.DESCENDING) // Ordenar por el timestamp en orden descendente
            .limit(1) // Limitar los resultados a 1
            .addSnapshotListener { snapshots, e ->
                if (e != null) {  // Si hay un error, se muestra en los logs y se detiene la ejecución
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                // Si los snapshots no están vacíos
                if (!snapshots!!.isEmpty) {
                    // Iterar sobre los cambios en los documentos
                    for (dc in snapshots.documentChanges) {
                        // Si se ha añadido un nuevo documento
                        if (dc.type == DocumentChange.Type.ADDED) {
                            lastQuestionId = dc.document.id // Guardar el ID de la última pregunta
                            showPopup() // Mostrar el popup
                        }
                    }
                }
            }
    }

    // Función para mostrar el popup
    fun showPopup() {
        val popup = PopupWindow(this)  // Creación de una nueva ventana emergente
        val layout = layoutInflater.inflate(R.layout.popup_layout, null)  // Inflado del layout del popup
        popup.contentView = layout  // Asignación del layout al contenido del popup

        // Inicialización de los botones del popup y asignación de su comportamiento al hacer click
        val yesButton: Button = layout.findViewById(R.id.button_option_one)
        yesButton.setOnClickListener {
            sendResponse("up")  // Enviar la respuesta "up" al hacer click en "sí"
            popup.dismiss()  // Cerrar el popup
        }

        val noButton: Button = layout.findViewById(R.id.button_option_two)
        noButton.setOnClickListener {
            sendResponse("down") // Enviar la respuesta "down" al hacer click en "no"
            popup.dismiss() // Cerrar el popup
        }

        // Mostrar el popup en el centro de la pantalla
        popup.showAtLocation(findViewById(R.id.root_layout), Gravity.CENTER, 0, 0)
    }

    // Función para enviar la respuesta
    fun sendResponse(choice: String) {
        // Creación de un HashMap con la respuesta
        val response = hashMapOf(
            "studentChoice" to choice
        )

        // Añadir la respuesta a la colección de respuestas de la pregunta activa
        db.collection("Sessions").document("sessionID")
            .collection("activeQuestions").document(lastQuestionId)
            .collection("responses")
            .add(response)
            .addOnSuccessListener {
                Log.d(TAG, "Response successfully written!")  // Log en caso de éxito
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing response", e)  // Log en caso de error
            }
    }
}
