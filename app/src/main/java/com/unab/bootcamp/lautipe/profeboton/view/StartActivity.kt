package com.unab.bootcamp.lautipe.profeboton.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.unab.bootcamp.lautipe.profeboton.R
import com.unab.bootcamp.lautipe.profeboton.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)


        // Configurar el click listener del botón del profesor
        binding.bottonTeacher.setOnClickListener {
            // Crear un intent para iniciar la actividad MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Configurar el click listener del botón del estudiante
        binding.buttonStudent.setOnClickListener {
            // Crear un intent para iniciar la actividad StudentActivity
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        }
    }
}
