package com.example.mibibliotecav2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mibibliotecav2.model.remote.PrestamosRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_agregarprestamo.*


import kotlinx.android.synthetic.main.fragment_nuevolibro.*
import java.io.ByteArrayOutputStream


class AgregarprestamoFragment : Fragment() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Prestamos")
    private val idprestamo = myRef.push().key

    private val REQUEST_IMAGE_PORTADA = 1234
    private var urlportada = ""
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser
    private val usrid = user?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_agregarprestamo, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_examinarportadapres.setOnClickListener{
            dispatchTakePictureIntent(REQUEST_IMAGE_PORTADA)
        }

        bt_guardarpres.setOnClickListener{
            val titulo = et_titulopres.text.toString()
            val paginas = et_numpaginaspres.text.toString()
            val genero = et_generopres.text.toString()
            val deudor = et_deudorpres.text.toString()


            if (titulo.isNotEmpty() && genero.isNotEmpty() && deudor.isNotEmpty() && paginas.isNotEmpty()) {


                val prestamo: PrestamosRemote = PrestamosRemote(
                    idprestamo,
                    titulo,
                    paginas,
                    genero,
                    urlportada,
                    idprestamo!!
                )
                myRef.child(usrid.toString()).child(idprestamo).setValue(prestamo)
                findNavController().navigate(R.id.action_agregarprestamoFragment_to_prestamosFragment)
            }


            else

            {
                Toast.makeText(requireContext(), "llene todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                if (titulo.isEmpty())
                    et_titulopres.setError("Este campo es obligatorio")
                if (genero.isEmpty())
                    et_generopres.setError("Este campo es obligatorio")
                if (deudor.isEmpty())
                    et_deudorpres.setError("Este campo es obligatorio")
                if (paginas.isEmpty())
                    et_numpaginaspres.setError("Este campo es obligatorio")
            }

        }

    }



    private fun dispatchTakePictureIntent(requestImage: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePictureIntent->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also{
                startActivityForResult(takePictureIntent,requestImage)
            }

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(  resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PORTADA){

            val mStorage = FirebaseStorage.getInstance()
            val id = myRef.push().key
            val photoRef = mStorage.reference.child(idprestamo!!).child(id!!)
            val bitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = photoRef.putBytes(data)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photoRef.downloadUrl }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                        urlportada = task.result.toString()
                        bt_examinarportadapres.text = "Cargada"

                }
            }


        }

    }


}