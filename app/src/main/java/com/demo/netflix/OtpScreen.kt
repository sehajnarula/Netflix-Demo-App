package com.demo.netflix
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.demo.netflix.modelclasses.UserData
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import `in`.aabhasjindal.otptextview.OtpTextView
import java.util.concurrent.TimeUnit
class OtpScreen : AppCompatActivity() {
    lateinit var otpField:OtpTextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var openToHome:Button
    lateinit var otpVerificationId:String
    lateinit var otpResendId:PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var phoneNumberFromLogin:String
    lateinit var resendOtpText:TextView
    lateinit var screenname:TextView
    lateinit var backarrow:ImageView
    lateinit var remainingTime:TextView
    lateinit var sharedPreferences: SharedPreferences
    private val userDataList:ArrayList<UserData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        screenname = findViewById(R.id.screenname)
        screenname.setText("OTP Verification")
        remainingTime = findViewById(R.id.showreversetime)
        backarrow = findViewById(R.id.backarrow)
        openToHome = findViewById(R.id.opentohomebutton)
        otpField = findViewById(R.id.otpfield)
        resendOtpText = findViewById(R.id.resendotp)
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        phoneNumberFromLogin = intent.getStringExtra("mobilenumber")!!

        getData()
        
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("verficationsuccessfull","$p0")
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("phonenumberverification",p0.toString())
                resendOtpText.visibility = View.VISIBLE
            }
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                otpVerificationId = p0
                otpResendId = p1
                Toast.makeText(this@OtpScreen, "OTP Sent", Toast.LENGTH_SHORT).show()
                reverseTimer()
            }
        }
        otpGet()
       
        openToHome.setOnClickListener {
            val otpString = otpField.otp.toString().trim()
            if (!otpString.isEmpty())
            {
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    otpVerificationId, otpString)

                signInWithPhoneNumber(credential)
            }
            else
            {
                Toast.makeText(this@OtpScreen, "OTP not recieved", Toast.LENGTH_SHORT).show()
            }
        }
        resendOtpText.setOnClickListener {
            resendOtp()
        }
        backarrow.setOnClickListener {
            finish()
        }
    }
    public fun getData()
    {
        val databaseLink = Firebase.firestore
        databaseLink.collection("Users").addSnapshotListener { value, error ->
            userDataList.clear()
            if (error!=null)
            {
                Log.d("fetcherror",error.toString())
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (value!=null)
            {
                for (dataSnapshot in value!!.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val userData:UserData = dataSnapshot.toObject(UserData::class.java)!!
                        userData.userId = dataSnapshot.id
                        userDataList.add(userData)
                    }
                }
            }
        }
    }
    public fun signInWithPhoneNumber(credential: PhoneAuthCredential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{task->
            if (task.isSuccessful)
            {
                verifySavedData()
            }
            else
            {
                Toast.makeText(this@OtpScreen, "Failed to login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun otpGet(){
        val numberSignIn = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumberFromLogin)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@OtpScreen)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(numberSignIn)
    }
    public fun resendOtp()
    {
        val numberSignIn = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumberFromLogin)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@OtpScreen)
            .setCallbacks(callbacks)
            .setForceResendingToken(otpResendId)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(numberSignIn)
    }
    public fun reverseTimer()
    {
        remainingTime.visibility = View.VISIBLE
        object : CountDownTimer(60000,1000){
            override fun onTick(millisUntilFinished: Long) {
                remainingTime.setText("Time Left:" +millisUntilFinished / 1000)
            }

            override fun onFinish() {
                remainingTime.visibility = View.GONE
                resendOtpText.visibility = View.VISIBLE
            }
        }.start()
    }
    public fun verifySavedData()
    {
        var userData:UserData?=null
        for (i in userDataList)
        {
            if (phoneNumberFromLogin.equals(i.mobileNumber))
            {
                userData = i
            }
        }
        if (userData==null)
        {
            val registerIntent = Intent(this@OtpScreen,RegisterForAnAccount::class.java)
            registerIntent.putExtra("registernumber",phoneNumberFromLogin)
            startActivity(registerIntent)
        }
        else
        {
            val gson = Gson().toJson(userData)
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("signinuser",gson)
            editor.putBoolean("userloggedin",true)
            editor.apply()
            val homeIntent = Intent(this@OtpScreen,HomeScreen::class.java)
            homeIntent.putExtra("registernumber",phoneNumberFromLogin)
            startActivity(homeIntent)
        }
    }
}