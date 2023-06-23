package com.unab.bootcamp.lautipe.profeboton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.unab.bootcamp.lautipe.profeboton.R

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        val button: Button = findViewById(R.id.bottom_start_question)
        button.setOnClickListener {
            val question = hashMapOf(
                "timeStamp" to Timestamp.now(),
                "responses" to arrayListOf<String>()
            )

            db.collection("Sessions").document("sessionID").collection("activeQuestions")
                .add(question)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error writing document", e)
                }
        }
    }
}
