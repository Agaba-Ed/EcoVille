package com.cleanerworld.ecoville.ui.authentication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cleanerworld.ecoville.MainActivity
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityAuntheticationBinding
import com.cleanerworld.ecoville.ui.authentication.login.LoginActivity
import com.cleanerworld.ecoville.ui.authentication.register.RegisterActivity
import com.cleanerworld.ecoville.ui.authentication.register.RegisterActivity.Companion.TAG
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuntheticationActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int=0
    private lateinit var binding:ActivityAuntheticationBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @SuppressLint("LongLogTag")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }


    }

    @SuppressLint("LongLogTag")
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    user?.let { updateUI(it) }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext,"Aunthentication failed",Toast.LENGTH_LONG).show()

                }
            }

    }


    @SuppressLint("LongLogTag")
    private fun onSignInResult(resultCode: Int) {
        if(resultCode== RESULT_OK){
            // Sign in success, update UI with the signed-in user's information
            Log.d(RegisterActivity.TAG, "signInWithGmail:success")
            Toast.makeText(baseContext, "Authentication SUCCESSFUL.",
                Toast.LENGTH_SHORT).show()
            val user = auth.currentUser
            user?.let { updateUI(it) }

        }
        else{
            // If sign in fails, display a message to the user.
            Log.w(RegisterActivity.TAG, "createUserWithEmail:failure")
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
            //updateUI(null)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuntheticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.gmail.setOnClickListener {
            signIn()
        }

        binding.loginOptionButton.setOnClickListener{
            goToLoginScreen()
        }
        binding.registerOptionButton.setOnClickListener{
            goToRegisterScreen()
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

    private fun updateUI(currentUser: FirebaseUser) {
        val intent=Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun goToLoginScreen(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
    fun goToRegisterScreen(){
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}