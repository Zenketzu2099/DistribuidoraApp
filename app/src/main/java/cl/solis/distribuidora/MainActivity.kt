package cl.solis.distribuidora

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.solis.distribuidora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón o acción para ir al login
        binding.tvMain.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
