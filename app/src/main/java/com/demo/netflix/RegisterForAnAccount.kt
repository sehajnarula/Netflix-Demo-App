package com.demo.netflix
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.demo.netflix.modelclasses.UserData
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
class RegisterForAnAccount : AppCompatActivity() {
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    lateinit var userFirstName:EditText
    lateinit var userLastName:EditText
    lateinit var userDob:EditText
    lateinit var userEmail:EditText
    lateinit var userPhoneNumber:EditText
    lateinit var progressBar:RelativeLayout
    lateinit var registerAccount:Button
    lateinit var sharedPreferences: SharedPreferences
    private val userDataList:ArrayList<UserData> = ArrayList()
    var loginScreenPhoneNumber:String?=null
    var userDocId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_for_an_account)
        initializeData()
        calendar = Calendar.getInstance()
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        loginScreenPhoneNumber = intent.getStringExtra("registernumber")
        userPhoneNumber.setText(loginScreenPhoneNumber)
        screenName.setText("Sign Up")
        clickListeners()
        manageUserProfileData()
    }
    public fun initializeData(){
        screenName = findViewById(R.id.screenname)
        backArrow = findViewById(R.id.backarrow)
        userFirstName = findViewById(R.id.userfirstname)
        userLastName = findViewById(R.id.userlastname)
        userDob = findViewById(R.id.userdob)
        userEmail = findViewById(R.id.useremail)
        userPhoneNumber = findViewById(R.id.usersignupnumber)
        progressBar = findViewById(R.id.progressloader)
        registerAccount = findViewById(R.id.registeraccountbutton)
    }
    fun clickListeners(){
        userDob.setOnClickListener {
            datePicker()
        }
        registerAccount.setOnClickListener {
            if (TextUtils.isEmpty(userFirstName.text.toString()))
            {
                Toast.makeText(this@RegisterForAnAccount, "Please fill your first name", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(userDob.text.toString()))
            {
                Toast.makeText(this, "Please enter your birth date", Toast.LENGTH_SHORT).show()
            }
            else if (ageCalculator(userDob.text.toString())==false)
            {
                Toast.makeText(this@RegisterForAnAccount, "Age should be 18 or above to register an account", Toast.LENGTH_SHORT).show()
            }
            else if(registerAccount.text.equals("Update Profile"))
            {
                updateData()
            }
//            else if (!CommonFunctions.isValidEmail(userEmail.text.toString()))
//        {
//            Toast.makeText(this@RegisterForAnAccount, "Please enter a valid E-mail", Toast.LENGTH_SHORT).show()
//        }
            else{
                var savedUserData:Boolean = false
                for(i in userDataList)
                {
                    if (userPhoneNumber.text.toString().equals(i.mobileNumber))
                    {
                        savedUserData = true
                    }
                }
                if (savedUserData)
                {
                    Toast.makeText(this@RegisterForAnAccount, "An account already exists with this phone number", Toast.LENGTH_SHORT).show()
                }
                else{
                    addData()
                }
            }
            closeKeyboard(userPhoneNumber)
        }
        backArrow.setOnClickListener {
            finish()
        }
    }
    fun manageUserProfileData()
    {
       val userData = sharedPreferences.getString("signinuser","")
       val getUserData = Gson().fromJson(userData,UserData::class.java)
       if (getUserData!=null && !getUserData.userId.isNullOrEmpty())
       {
           userFirstName.setText(getUserData.firstName)
           userLastName.setText(getUserData.lastName)
           userPhoneNumber.setText(getUserData.mobileNumber)
           userEmail.setText(getUserData.emailAddress)
           userDob.setText(getUserData.dateOfBirth)
           userDocId = getUserData.userId
           userFirstName.isFocusable = false
           userDob.isFocusable = false
       }
        screenName.setText("Manage Profile")
        registerAccount.setText("Update Profile")
    }
    lateinit var calendar : Calendar
    public fun datePicker()
    {
        val datePickerDialog = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            calendar.set(Calendar.YEAR,i)
            calendar.set(Calendar.MONTH,i2)
            calendar.set(Calendar.DAY_OF_MONTH,i3)
            val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
            userDob.setText(simpleDateFormat.format(calendar.time))
        }
        val dateDialog:DatePickerDialog
        dateDialog = DatePickerDialog(this,datePickerDialog,calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
        val cal:Calendar = Calendar.getInstance()
        val year:Int = cal.get(Calendar.YEAR)-18
        val month:Int = cal.get(Calendar.MONTH)
        val date:Int = cal.get(Calendar.DATE)
        cal.set(year, month, date)
        val minTime:Long = cal.timeInMillis
        dateDialog.datePicker.maxDate=minTime
        dateDialog.show()
    }
    public fun ageCalculator(userBirthDate:String):Boolean
    {
        try {
            val sdf:SimpleDateFormat= SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date: Date = sdf.parse(userBirthDate)
            val birth:Calendar = Calendar.getInstance()
            birth.time=date
            val today:Calendar = Calendar.getInstance()
            var difference:Int = today.get(Calendar.YEAR)-birth.get(Calendar.YEAR)
            if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH))
            {
                difference--
            }
            if (today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))
            {
                difference--
            }
            return difference>=18
        }
        catch (e:Exception)
        {
            return false
        }
    }
    public fun addData()
    {
        progressBar.visibility = View.VISIBLE
        val databaseReference = Firebase.firestore
        val putUserData:HashMap<String,String> = HashMap()
        putUserData.put("firstName",userFirstName.text.toString())
        putUserData.put("lastName",userLastName.text.toString())
        putUserData.put("dateOfBirth",userDob.text.toString())
        putUserData.put("emailAddress",userEmail.text.toString())
        putUserData.put("mobileNumber",userPhoneNumber.text.toString())
        var ref = databaseReference.collection("Users")
        var id = ref.document().id
        ref.document(id).set(putUserData).addOnSuccessListener {
            Toast.makeText(this@RegisterForAnAccount, "Account Registered", Toast.LENGTH_SHORT).show()
            Log.d("useridsaved", id)
            progressBar.visibility = View.GONE
            val userData=UserData(
                firstName = userFirstName.text.toString(),
                lastName = userLastName.text.toString(),
                dateOfBirth = userDob.text.toString(),
                emailAddress = userEmail.text.toString(),
                mobileNumber = userPhoneNumber.text.toString(),
                userId = id
            )
            val gson = Gson().toJson(userData)
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("signinuser",gson)
            editor.putBoolean("userloggedin",true)
            editor.apply()
            val intent = Intent(this@RegisterForAnAccount,DemoNetflixHomePage::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this@RegisterForAnAccount, "Unable To Register Account", Toast.LENGTH_SHORT).show()
        }
    }
    public fun updateData(){
        progressBar.visibility = View.VISIBLE
        val updateUser:HashMap<String,Any> = HashMap()
        updateUser.put("lastName",userLastName.text.toString())
        updateUser.put("emailAddress",userEmail.text.toString())
        val databaseLink = Firebase.firestore
        databaseLink.collection("Users").document(userDocId!!).set(updateUser).addOnSuccessListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this@RegisterForAnAccount, "Profile Updated", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this@RegisterForAnAccount, "Unable to update profile", Toast.LENGTH_SHORT).show()
        }
    }
    private fun closeKeyboard(view: EditText)
    {
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,1)
        view.clearFocus()
    }
}