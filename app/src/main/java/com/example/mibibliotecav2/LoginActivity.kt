package com.example.mibibliotecav2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val datos = intent.extras
        val usuario = datos?.getString("usuario")
        val correo = datos?.getString("correo")
        val contrasena = datos?.getString("contrasena")

        IB_facebook_login.setOnClickListener {
            Toast.makeText(this, "Disponible próximamente", Toast.LENGTH_SHORT).show()
        }

        IB_gmail_login.setOnClickListener {
            Toast.makeText(this, "Disponible próximamente", Toast.LENGTH_SHORT).show()
        }

        BT_ingresar_registro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, 1234)
        }
        BT_ingresar_login.setOnClickListener {
            if (ET_contrasena_login.text.toString() == contrasena && ET_correo_login.text.toString() == correo) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("usuario", usuario)
                intent.putExtra("correo", correo)
                intent.putExtra("contrasena", contrasena)
                startActivity(intent)
                finish()
            } else if (contrasena == null || correo == null) {
                Toast.makeText(this, "Por favor, Registrese", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Contraseña o Usuarios incorrectos ", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1234 && resultCode == Activity.RESULT_OK){
            val usuario = data?.extras?.getString("usuario")
            val correo = data?.extras?.getString("correo")
            val contrasena = data?.extras?.getString("contrasena")
            Toast.makeText(this , "usuario: $usuario\ncorreo: $correo\ncontraseña: $contrasena ", Toast.LENGTH_LONG).show()
            BT_ingresar_login.setOnClickListener {
                if(ET_correo_login.text.toString() == correo && ET_contrasena_login.text.toString() == contrasena){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("usuario", usuario)
                    intent.putExtra("correo", correo)
                    intent.putExtra("contrasena", contrasena)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Contraseña o usuario incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}