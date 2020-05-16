package com.prajyotmane.healthcare

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_doctor_profile.*
import kotlinx.android.synthetic.main.fragment_doctor_profile.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_doctor_info_update.*

class DoctorProfileFragment : Fragment() {

    private val Default_IMAGE_URL = "https://picsum.photos/200"
    private lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE=100
    lateinit var loading: LoadingDialogBox
    lateinit var ref:DatabaseReference
lateinit var postListener: ValueEventListener
    private val currUser = FirebaseAuth.getInstance().currentUser

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loading = LoadingDialogBox(activity as Activity)
        var doctorId = auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("Doctor")
        loading.startLoading()

        Log.d("XXXXXXXXXXXXXXXXXXXX",currUser?.photoUrl.toString())


        postListener = object : ValueEventListener {

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
                var docSpecialization = dataSnapshot.child(doctorId).child("specialization").getValue()

                var docDesc = dataSnapshot.child(doctorId).child("description").getValue()
                var docDegree = dataSnapshot.child(doctorId).child("degree").getValue()
                var docExp = dataSnapshot.child(doctorId).child("experience").getValue()
                var photourl = dataSnapshot.child(doctorId).child("PhotoURL").getValue()


                if(docFname != null && docFname.toString().length>0){
                    doctorFname.setText(docFname.toString())
                }else{
                    doctorFname.setText("Not available")
                }

                if(docLname != null && docLname.toString().length>0){
                    doctorLname.setText(docLname.toString())
                }else{
                    doctorLname.setText("Not available")
                }

                if(docEmail != null && docEmail.toString().length>0){
                    doctorEmail.setText(docEmail.toString())
                }else{
                    doctorEmail.setText("Not available")
                }

                //doctorFname.setText("Samarth")
                if(docAddrFirst != null && docAddrFirst.toString().length>0){
                    doctorAddr1Txt.setText(docAddrFirst.toString())
                }else{
                    doctorAddr1Txt.setText("Not available")
                }

                 if(docAddrScnd != null && docAddrScnd.toString().length>0){
                     doctorAddr2Txt.setText(docAddrScnd.toString())
                }else{
                     doctorAddr2Txt.setText("Not available")
                 }

                if(docCity != null && docCity.toString().length>0){
                    docCityTxt.setText(docCity.toString())
                }else{
                    docCityTxt.setText("Not available")
                }

                if(docState != null&& docState.toString().length>0){
                    docStateTxt.setText(docState.toString())
                }else{
                    docStateTxt.setText("Not available")
                }

                if(docPinCode != null && docPinCode.toString().length>0){
                    docZipTxt.setText(docPinCode.toString())
                }else{
                    docZipTxt.setText("Not available")
                }

                if(docSpecialization != null && docSpecialization.toString().length>0){
                    docSpclTxt.setText(docSpecialization.toString())
                }else{
                    docSpclTxt.setText("Not available")
                }

                if(docDesc != null && docDesc.toString().length>0){
                    docDescriptionTxt.setText(docDesc.toString())
                }else{
                    docDescriptionTxt.setText("Not available")
                }

                if(docDegree != null && docDegree.toString().length>0){
                    docDegreeTxt.setText(docDegree.toString())
                }else{
                    docDegreeTxt.setText("Not available")
                }

                if(docExp != null && docExp.toString().length>0){
                    docExpTxt.setText(docExp.toString())
                }else{
                    docExpTxt.setText("Not available")
                }

               currUser?.let {user ->
                   if(user.photoUrl == null)
                       Glide.with(activity!!).load(Default_IMAGE_URL).into(image_profile_view)
                   else
                        Glide.with(activity!!).load(user.photoUrl).into(image_profile_view)
                }
                loading.cancelLoading()
/*                Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show()
                Log.d("URL",currUser?.photoUrl.toString())*/

            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.cancelLoading()
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
            
        }
        ref.addValueEventListener(postListener)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view =  inflater.inflate(R.layout.fragment_doctor_profile,container,false)

        view.updateDocInformation.setOnClickListener {
            startActivity(Intent(context,DoctorInfoUpdate::class.java))
        }

      /*  Picasso.with(context)
            .load(currUser?.photoUrl.toString()+".png")
            .into(view.image_profile_view)
*/

        return view
    }


    override fun onDestroy() {
        ref.removeEventListener(postListener)
        super.onDestroy()
    }
}