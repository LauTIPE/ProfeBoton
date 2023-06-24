package com.unab.bootcamp.lautipe.profeboton.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.unab.bootcamp.lautipe.profeboton.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val teacherButton: Button = findViewById(R.id.button_teacher)
        teacherButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val studentButton: Button = findViewById(R.id.button_student)
        studentButton.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        }
    }
}
