package com.demo.netflix
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.demo.netflix.modelclasses.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
class HomeScreen : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var fragmentScreenName:TextView
    lateinit var navigationDrawerIcon:ImageView
    lateinit var drawerLayout:DrawerLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var logoutNavDrawer:TextView
    lateinit var userPhoneNumber:TextView
    var userNumber:String?=null
    lateinit var loggedinUserFirstName:TextView
    lateinit var firebaseAuth: FirebaseAuth
     val userDataList:ArrayList<UserData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        bottomNavigationView = findViewById(R.id.navigatescreensbottom)
        fragmentScreenName = findViewById(R.id.fragmentscreenname)
        navigationDrawerIcon = findViewById(R.id.navigationdrawericon)
        drawerLayout = findViewById(R.id.drawerlayoutparent)
        logoutNavDrawer = findViewById(R.id.logoutnavdrawer)
        userPhoneNumber = findViewById(R.id.loggedinnumber)
        loggedinUserFirstName = findViewById(R.id.loggedinuserfirstname)
        userNumber = intent.getStringExtra("registernumber")
        fragmentDisplay(HomeView())
        userPhoneNumber.setText(userNumber)
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()
        navigationDrawerIcon.visibility = View.VISIBLE
        fragmentScreenName.setText("Home")
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.homepage -> {
                    fragmentDisplay(HomeView())
                    fragmentScreenName.setText("Home")
                    navigationDrawerIcon.visibility = View.VISIBLE
                    navigationDrawerIcon.setOnClickListener {
                        if (drawerLayout.isOpen)
                        {
                            drawerLayout.close()
                        }
                        else
                        {
                            drawerLayout.open()
                        }
                    }
                    true
                }
                R.id.favourites -> {
                    fragmentDisplay(Favourites())
                    fragmentScreenName.setText("Favourites")
                    navigationDrawerIcon.visibility = View.GONE
                    true
                }
                R.id.categories -> {
                    fragmentDisplay(Categories())
                    fragmentScreenName.setText("Categories")
                    navigationDrawerIcon.visibility = View.GONE
                    true
                }
                else ->{
                    fragmentDisplay(HomeView())
                    fragmentScreenName.setText("Home")
                    true
                }
            }
        }
        val databaseLink = Firebase.firestore
        databaseLink.collection("Users").addSnapshotListener { value, error ->
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val userData:UserData = dataSnapshot.toObject(UserData::class.java)!!
                        userData.userId = dataSnapshot.id
                        userDataList.add(userData)
                    }
                }
            }
            for (i in userDataList)
            {
                if (userNumber.equals(i.mobileNumber))
                {
                    loggedinUserFirstName.text = i.firstName
                }
            }
            if (error!=null)
            {
                Log.d("navusererror",error.toString())
            }
        }
        logoutNavDrawer.setOnClickListener {
            firebaseAuth.signOut()
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("signinuser","")
            editor.putBoolean("userloggedin",false)
            editor.apply()
            val intent = Intent(this@HomeScreen,LoginScreen::class.java)
            startActivity(intent)
        }
    }
    public fun fragmentDisplay(fragment:Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentviewer,fragment)
        transaction.commit()
    }
}