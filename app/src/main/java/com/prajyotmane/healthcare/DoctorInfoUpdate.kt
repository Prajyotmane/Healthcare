package com.prajyotmane.healthcare

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_doctor_info_update.*
import kotlinx.android.synthetic.main.fragment_doctor_profile.*
import java.io.ByteArrayOutputStream

class DoctorInfoUpdate : AppCompatActivity() {

    private val Default_IMAGE_URL = "https://picsum.photos/200"
    private lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 100
    private val currentUser = FirebaseAuth.getInstance().currentUser
    lateinit var loading: LoadingDialogBox

    lateinit var mAuth: FirebaseAuth
    lateinit var doctorId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_info_update)
        loading = LoadingDialogBox(this)
        mAuth = FirebaseAuth.getInstance()
        doctorId = mAuth.currentUser!!.uid

        populateFields()

        update_Doc_Info.setOnClickListener {
            saveInformation()
        }

        image_view.setOnClickListener {
            takePictureIntent()
        }
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(this.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun saveInformation() {
        loading.startLoading()
        val photo = when {
            ::imageUri.isInitialized -> imageUri
            currentUser?.photoUrl == null -> Uri.parse(Default_IMAGE_URL)
            else -> currentUser.photoUrl
        }

        val updates = UserProfileChangeRequest.Builder().setPhotoUri(photo).build()
        //register_update_progress.visibility= View.VISIBLE
        currentUser?.updateProfile(updates)


        var ref = FirebaseDatabase.getInstance().getReference("Doctor")

        ref.child(doctorId).child("firstName").setValue(docUpdateFirstName.text.toString())
        ref.child(doctorId).child("lastName").setValue(docUpdateLastName.text.toString())
        //ref.child(doctorId).child("email").setValue(docUpdateEmail.text.toString())
        //ref.child(doctorId).child("password").setValue(docUpdatePassword.text.toString())
        ref.child(doctorId).child("addressFirst")
            .setValue(doctor_update_FirstAddress.text.toString())
        ref.child(doctorId).child("addressSecond")
            .setValue(doctor_update_SecondAddress.text.toString())
        ref.child(doctorId).child("city").setValue(doctor_update_city.text.toString())
        ref.child(doctorId).child("state").setValue(doctor_update_state.text.toString())
        ref.child(doctorId).child("pincode").setValue(doctor_update_zip.text.toString())
        ref.child(doctorId).child("specialization")
            .setValue(specialization_select.selectedItem.toString())

        ref.child(doctorId).child("description").setValue(docUpdateDescription.text.toString())
        ref.child(doctorId).child("degree").setValue(docUpdateDegree.text.toString())
        ref.child(doctorId).child("experience").setValue(docUpdateYears.text.toString())
        ref.child(doctorId).child("PhotoURL").setValue(currentUser?.photoUrl.toString())

        loading.cancelLoading()
        //register_update_progress.visibility=View.INVISIBLE
        Toast.makeText(this, "Your information has been updated !", Toast.LENGTH_SHORT).show()
        finish()

    }

    fun populateFields() {
        loading.startLoading()
        var db = FirebaseDatabase.getInstance().getReference("Doctor")

        currentUser?.let { user ->
            if (user.photoUrl == null)
                Glide.with(this).load(Default_IMAGE_URL).into(image_view)
            else
                Glide.with(this).load(user.photoUrl).into(image_view)
        }

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var docFname = dataSnapshot.child(doctorId).child("firstName").getValue()
                var docLname = dataSnapshot.child(doctorId).child("lastName").getValue()
                var docEmail = dataSnapshot.child(doctorId).child("email").getValue()
                var docPwd = dataSnapshot.child(doctorId).child("password").getValue()
                var docAddrFirst = dataSnapshot.child(doctorId).child("addressFirst").getValue()
                var docAddrScnd = dataSnapshot.child(doctorId).child("addressSecond").getValue()
                var docCity = dataSnapshot.child(doctorId).child("city").getValue()
                var docState = dataSnapshot.child(doctorId).child("state").getValue()
                var docPinCode = dataSnapshot.child(doctorId).child("pincode").getValue()
                var docSpecialization: String? =
                    dataSnapshot.child(doctorId).child("specialization").getValue().toString()

                var docDesc = dataSnapshot.child(doctorId).child("description").getValue()
                var docDeg = dataSnapshot.child(doctorId).child("degree").getValue()
                var docYrs = dataSnapshot.child(doctorId).child("experience").getValue()

                if (docFname != null) {
                    docUpdateFirstName.setText(docFname.toString())
                }

                if (docLname != null) {
                    docUpdateLastName.setText(docLname.toString())
                }

/*                if(docUpdateEmail != null){
                    docUpdateEmail.setText(docEmail.toString())
                }

                if(docUpdatePassword != null){
                    docUpdatePassword.setText(docPwd.toString())
                }*/

                if (docAddrFirst != null) {
                    doctor_update_FirstAddress.setText(docAddrFirst.toString())
                }

                if (docAddrScnd != null) {
                    doctor_update_SecondAddress.setText(docAddrScnd.toString())
                }

                if (docCity != null) {
                    doctor_update_city.setText(docCity.toString())
                }

                if (docState != null) {
                    doctor_update_state.setText(docState.toString())
                }

                if (docPinCode != null) {
                    doctor_update_zip.setText(docPinCode.toString())
                }

                if (docDesc != null) {
                    docUpdateDescription.setText(docDesc.toString())
                }

                if (docDeg != null) {
                    docUpdateDegree.setText(docDeg.toString())
                }

                if (docYrs != null) {
                    docUpdateYears.setText(docYrs.toString())
                }

                //docUpdateFirstName.setText("Samarth")

                ArrayAdapter.createFromResource(
                    applicationContext, R.array.doc_spl_type, android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    specialization_select.adapter = adapter

                    if (docSpecialization != null) {
                        val spinnerPositionSpecialization: Int =
                            adapter.getPosition(docSpecialization)
                        specialization_select.setSelection(spinnerPositionSpecialization)
                    }
                }
                loading.cancelLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        db.addValueEventListener(postListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        //register_update_progress.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            //register_update_progress.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        //this?.toast(imageUri.toString())
                        image_view.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    //activity?.toast(it.message!!)
                }
            }
        }

    }
}
