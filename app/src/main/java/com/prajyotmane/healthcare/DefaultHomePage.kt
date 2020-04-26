package com.prajyotmane.healthcare

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_default_home_page.*
import kotlinx.android.synthetic.main.fragment_default_home_page.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DefaultHomePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class DefaultHomePage : Fragment() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_default_home_page, container, false)

        view.button.setOnClickListener {
            mAuth?.signOut()
            var intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}
