package com.unab.bootcamp.lautipe.profeboton.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.unab.bootcamp.lautipe.profeboton.R

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Obtener la referencia al TextView del botón del profesor
        val teacherText: TextView = findViewById(R.id.botton_teacher)

        // Configurar el click listener del botón del profesor
        teacherText.setOnClickListener {
            // Crear un intent para iniciar la actividad TeacherActivity
            val intent = Intent(this, TeacherActivity::class.java)
            startActivity(intent)
        }

        // Obtener la referencia al botón del estudiante
        val studentButton: Button = findViewById(R.id.button_student)

        // Configurar el click listener del botón del estudiante
        studentButton.setOnClickListener {
            // Crear un intent para iniciar la actividad StudentActivity
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        }
    }
}
