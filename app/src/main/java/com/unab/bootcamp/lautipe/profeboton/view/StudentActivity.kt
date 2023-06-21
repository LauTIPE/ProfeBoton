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

class StudentActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "StudentActivity"
    }


    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        db = FirebaseFirestore.getInstance()

        val sessionRef = db.collection("sessions").document("sessionID1")
        sessionRef.collection("questions")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                if (!snapshots!!.isEmpty) {
                    for (dc in snapshots.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            showPopup()
                        }
                    }
                }
            }
    }

    fun showPopup() {
        val popup = PopupWindow(this)
        val layout = layoutInflater.inflate(R.layout.popup_layout, null)
        popup.contentView = layout

        val yesButton: Button = layout.findViewById(R.id.button_option_one)
        yesButton.setOnClickListener {
            // Aquí debes agregar el código para enviar la respuesta "up" a Firebase
            popup.dismiss()
        }

        val noButton: Button = layout.findViewById(R.id.button_option_two)
        noButton.setOnClickListener {
            // Aquí debes agregar el código para enviar la respuesta "down" a Firebase
            popup.dismiss()
        }

        popup.showAtLocation(findViewById(R.id.root_layout), Gravity.CENTER, 0, 0)
    }
}

