package esquer.francisco.misnotas

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_agregar_nota.*
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest

class AgregarNotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getSupportActionBar()?.hide()
        setContentView(R.layout.activity_agregar_nota)

        btn_guardar.setOnClickListener() {
            guardarNota()
        }

    }

    fun guardarNota() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                235
            )

        } else {
            guardar()
        }
    }

    fun guardar() {
        var titulo = et_titulo.text.toString()
        var cuerpo = et_contenido.text.toString()
        if (titulo.isBlank() || cuerpo.isBlank() || titulo.isEmpty() || cuerpo.isEmpty()) {
            Toast.makeText(this, "Error: campos vacios", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val archivo = File(ubicacion(), titulo + ".txt")
                val fos = FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()
                Toast.makeText(
                    this,
                    "Se guardo el archivo en la carpeta publica",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: no se guardo el archivo", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun ubicacion(): String {
        val carpeta = File(Environment.getExternalStorageDirectory(), "notas")
        if (!carpeta.exists()) {
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            235 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    guardar()
                } else {
                    Toast.makeText(this, "Error: permisos denegados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}