package cl.solis.distribuidora

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvSaludo: TextView
    private lateinit var tvUltima: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnLogout: Button

    // 👉 URL de tu Realtime Database
    private val DB_URL = "https://distribuidoraapp-f3647-default-rtdb.firebaseio.com/"

    // Pide permisos de ubicación solo cuando hace falta
    private val permisoUbicacion = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fine || coarse) {
            guardarUbicacion()
        } else {
            Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        // Si no hay usuario, vuelve al login
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_menu)

        tvSaludo = findViewById(R.id.tvSaludo)
        tvUltima = findViewById(R.id.tvUltima)
        btnGuardar = findViewById(R.id.btnGuardarUbicacion)
        btnLogout = findViewById(R.id.btnLogout)

        tvSaludo.text = "Bienvenido(a), ${auth.currentUser?.email ?: "usuario"}"

        // Cargar última ubicación guardada al abrir
        cargarUltimaUbicacion()

        btnGuardar.setOnClickListener {
            solicitarPermisosSiFaltan()
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }

    private fun solicitarPermisosSiFaltan() {
        val faltanFine = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        val faltanCoarse = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        if (faltanFine || faltanCoarse) {
            val permisos = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (Build.VERSION.SDK_INT >= 29) {
                // No pedimos background aquí; solo foreground
            }
            permisoUbicacion.launch(permisos.toTypedArray())
        } else {
            guardarUbicacion()
        }
    }

    private fun guardarUbicacion() {
        val fused = LocationServices.getFusedLocationProviderClient(this)
        try {
            fused.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    if (loc == null) {
                        Toast.makeText(this, "No se obtuvo ubicación. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val uid = auth.currentUser!!.uid
                    val ref = FirebaseDatabase.getInstance(DB_URL)
                        .getReference("usuarios/$uid/ubicacion")

                    val data = mapOf(
                        "lat" to loc.latitude,
                        "lon" to loc.longitude,
                        "timestamp" to ServerValue.TIMESTAMP
                    )

                    ref.setValue(data)
                        .addOnSuccessListener {
                            val msg = "Ubicación guardada: (${loc.latitude}, ${loc.longitude}) en /usuarios/$uid/ubicacion"
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

                            val fecha = Date(System.currentTimeMillis())
                            val formato = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                            val fechaLegible = formato.format(fecha)

                            tvUltima.text = "Última ubicación guardada: ${"%.6f".format(loc.latitude)}, ${"%.6f".format(loc.longitude)}\n$fechaLegible"
                        }

                        .addOnFailureListener {
                            Toast.makeText(this, "No se pudo guardar en la nube.", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Fallo al obtener ubicación.", Toast.LENGTH_SHORT).show()
                }
        } catch (se: SecurityException) {
            Toast.makeText(this, "Sin permisos de ubicación.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarUltimaUbicacion() {
        val uid = auth.currentUser!!.uid
        val ref = FirebaseDatabase.getInstance(DB_URL).getReference("usuarios/$uid/ubicacion")
        ref.get()
            .addOnSuccessListener { snap ->
                val lat = snap.child("lat").getValue(Double::class.java)
                val lon = snap.child("lon").getValue(Double::class.java)
                if (lat != null && lon != null) {
                    tvUltima.text = "Última ubicación guardada: ${"%.6f".format(lat)}, ${"%.6f".format(lon)}"
                } else {
                    tvUltima.text = "Última ubicación guardada: —"
                }
            }
            .addOnFailureListener {
                tvUltima.text = "Última ubicación guardada: —"
            }
    }
}
