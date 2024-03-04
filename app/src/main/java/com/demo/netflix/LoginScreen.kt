package com.demo.netflix
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.demo.netflix.modelclasses.UserData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit
class LoginScreen : AppCompatActivity() {
    lateinit var phoneNumberField:EditText
    lateinit var signIn:Button
    lateinit var progressBar:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        progressBar = findViewById(R.id.progressloader)
        phoneNumberField = findViewById(R.id.userphonenumberfield)
        signIn = findViewById(R.id.signinbutton)
        signIn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (!phoneNumberField.text.toString().isEmpty() && phoneNumberField.text.toString().length==10) {
                val intent = Intent(applicationContext,OtpScreen::class.java)
                intent.putExtra("mobilenumber","+91${phoneNumberField.text}")
                progressBar.visibility = View.GONE
                startActivity(intent)
            }
            else
            {
                progressBar.visibility = View.GONE
                Toast.makeText(this@LoginScreen, "Please fill the correct mobile number", Toast.LENGTH_SHORT).show()
            }
            closeKeyboard(phoneNumberField)
        }
    }
    private fun closeKeyboard(view: EditText)
    {
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,1)
        view.clearFocus()
    }
}