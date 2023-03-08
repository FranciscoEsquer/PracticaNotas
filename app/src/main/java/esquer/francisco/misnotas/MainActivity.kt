package esquer.francisco.misnotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var notas = ArrayList<Nota>()
    lateinit var adaptador: AdaptadorNotas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getSupportActionBar()?.hide()
        setContentView(R.layout.activity_main)

        fab.setOnClickListener() {
            var intent = Intent(this, AgregarNotaActivity::class.java)
            startActivityForResult(intent, 123)
        }

        adaptador = AdaptadorNotas(this, notas)
        listview.adapter = adaptador

    }

    private fun ubicacion(): File {
        val folder = File(Environment.getExternalStorageDirectory(), "notas")
        if (!folder.exists()) {
            folder.mkdir()
        }
        return folder
    }

    fun leerArchivo(archivo: File) {
        val fis = FileInputStream(archivo)
        val dis = DataInputStream(fis)
        val br = BufferedReader(InputStreamReader(dis))
        var strLine: String? = br.readLine()
        var myData = ""

        while (strLine != null) {
            myData = myData + strLine
            strLine = br.readLine()
        }
        br.close()
        dis.close()
        fis.close()

        var nombre = archivo.name.substring(0, archivo.name.length - 4)

        var nota = Nota(nombre, myData)
        notas.add(nota)
    }

    fun leerNotas() {
        notas.clear()
        var carpeta = File(ubicacion().absolutePath)

        if (carpeta.exists()) {
            var archivos = carpeta.listFiles()
            if (archivos != null) {
                for (archivo in archivos) {
                    leerArchivo(archivo)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            leerNotas()
            adaptador.notifyDataSetChanged()
        }

    }

}