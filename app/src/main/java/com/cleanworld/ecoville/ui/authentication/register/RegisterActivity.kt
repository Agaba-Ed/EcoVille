package com.cleanerworld.ecoville.ui.authentication.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cleanerworld.ecoville.MainActivity
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityRegisterBinding
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)


        setContentView(binding.root)

        auth=Firebase.auth


        binding.registerButton.setOnClickListener {
            register()
        }



    }

    @SuppressLint("LongLogTag")
    fun register(){

        email=binding.etEmail.text.toString()
        password=binding.etPassword.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent=Intent(baseContext,MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    companion object {
        public const val TAG="com.cleanerworld.ecoville.ui.TAG"
    }
}