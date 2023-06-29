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
    private var estadoPreguntaAnterior: Boolean? = null // Añadir una nueva variable para guardar el valor anterior

    // Función que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        db = FirebaseFirestore.getInstance() // Inicialización de la instancia de Firestore

        // ID del profesor y de la pregunta
        val teacherId = "0B05EqNQzx2kmOjAEH6T"
        val questionId = "4MbODdoMvUpHArtFMnQN"

        // Creación de la referencia a la pregunta específica
        val questionRef = db.collection("profesor").document(teacherId).collection("preguntas").document(questionId)

        // Añadir un SnapshotListener a la pregunta
        questionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")

                // Comprobar si "estado_pregunta" es true
                val estadoPregunta = snapshot.getBoolean("estado_pregunta")

                // Comprobar si el estado ha cambiado a true desde la última vez
                if (estadoPregunta == true && estadoPregunta != estadoPreguntaAnterior) {
                    showPopup() // Mostrar el popup
                }

                // Actualizar el valor de estadoPreguntaAnterior
                estadoPreguntaAnterior = estadoPregunta
            } else {
                Log.d(TAG, "Current data: null")
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
            sendResponse("(y)")  // Enviar la respuesta "(y)" al hacer click en "sí"
            popup.dismiss()  // Cerrar el popup
        }

        val noButton: Button = layout.findViewById(R.id.button_option_two)
        noButton.setOnClickListener {
            sendResponse("(n)") // Enviar la respuesta "(n)" al hacer click en "no"
            popup.dismiss() // Cerrar el popup
        }

        // Mostrar el popup en el centro de la pantalla
        popup.showAtLocation(findViewById(R.id.root_layout), Gravity.CENTER, 0, 0)
    }

    // Función para enviar la respuesta
    fun sendResponse(choice: String) {
        // Determinar el ID de respuesta basado en la elección
        val responseId = if (choice == "(y)") "6jDP5fOeKvSUwh83d1DM" else "NOjKTD8AR63AcN4KO4sj"

// Crear una referencia al documento de la respuesta en Firestore
        val responseRef = db.collection("profesor").document("preguntas").collection("respuesta").document(responseId)

// Iniciar una transacción para incrementar el contador
        db.runTransaction { transaction ->
            // Obtener el documento actual
            val snapshot = transaction.get(responseRef)

            // Leer el valor actual de "conteo_respuesta"
            val currentCount = snapshot.getLong("conteo_respuesta") ?: 0

            // Incrementar "conteo_respuesta" y guardar el nuevo valor
            transaction.update(responseRef, "conteo_respuesta", currentCount + 1)

            // No es necesario retornar nada
            null
        }.addOnSuccessListener {
            Log.d(TAG, "Response count updated successfully!")  // Log en caso de éxito
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error updating response count", e)  // Log en caso de error
        }
    }
}
