package com.cleanerworld.ecoville.ui.authentication.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cleanerworld.ecoville.MainActivity
import com.cleanerworld.ecoville.databinding.ActivityLoginBinding
import com.cleanerworld.ecoville.ui.authentication.register.RegisterActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var password: String
    private lateinit var email: String
    private lateinit var binding:ActivityLoginBinding
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize Firebase Auth
        auth= Firebase.auth
        binding.loginButton.setOnClickListener {
            login()
        }
    }

     override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //reload();
            updateUI(currentUser)
        }
    }

    private fun reload() {
        TODO("Not yet implemented")
        val intent=Intent()
    }

    @SuppressLint("LongLogTag")
    fun login(){
        email=binding.etEmail.text.toString()
        password=binding.etPassword.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent= Intent(baseContext,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}