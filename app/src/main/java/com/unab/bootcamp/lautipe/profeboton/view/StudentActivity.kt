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
    private lateinit var lastQuestionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        db = FirebaseFirestore.getInstance()

        val sessionRef = db.collection("Sessions").document("sessionID")
        sessionRef.collection("activeQuestions")
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
                            lastQuestionId = dc.document.id
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
            sendResponse("up")
            popup.dismiss()
        }

        val noButton: Button = layout.findViewById(R.id.button_option_two)
        noButton.setOnClickListener {
            sendResponse("down")
            popup.dismiss()
        }

        popup.showAtLocation(findViewById(R.id.root_layout), Gravity.CENTER, 0, 0)
    }
    fun sendResponse(choice: String) {
        val response = hashMapOf(
            "studentChoice" to choice
        )

        db.collection("Sessions").document("sessionID")
            .collection("activeQuestions").document(lastQuestionId)
            .collection("responses")
            .add(response)
            .addOnSuccessListener {
                Log.d(TAG, "Response successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing response", e)
            }
    }
}

