package com.demo.netflix
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
    lateinit var loggedInUserPhoneNumber:TextView
    lateinit var loggedinUserFirstName:TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var addTitle:TextView
    lateinit var addCategory:TextView
    val databaseLink = Firebase.firestore
    lateinit var manageProfile:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initializeData()
        fragmentDisplay(HomeView())
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()
        navigationDrawerIcon.visibility = View.VISIBLE
        fragmentScreenName.setText("Home")
        clickListener()
        getData()
    }
    fun initializeData()
    {
        bottomNavigationView = findViewById(R.id.navigatescreensbottom)
        fragmentScreenName = findViewById(R.id.fragmentscreenname)
        navigationDrawerIcon = findViewById(R.id.navigationdrawericon)
        drawerLayout = findViewById(R.id.drawerlayoutparent)
        logoutNavDrawer = findViewById(R.id.logoutnavdrawer)
        loggedInUserPhoneNumber = findViewById(R.id.loggedinnumber)
        loggedinUserFirstName = findViewById(R.id.loggedinuserfirstname)
        addTitle = findViewById(R.id.addtitlescreentext)
        addCategory = findViewById(R.id.addcategoryscreentext)
        manageProfile = findViewById(R.id.manageprofilescreentext)
    }
    fun getData()
    {
        val userData = sharedPreferences.getString("signinuser","")
        val getUserData = Gson().fromJson(userData,UserData::class.java)
        if (getUserData!=null && !getUserData.userId.isNullOrEmpty())
        {
            loggedinUserFirstName.setText(getUserData.firstName)
            loggedInUserPhoneNumber.setText(getUserData.mobileNumber)
        }
    }
    fun clickListener()
    {
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.homepage -> {
                    fragmentDisplay(HomeView())
                    fragmentScreenName.setText("Home")
                    navigationDrawerIcon.visibility = View.VISIBLE
                    navigationDrawerIcon.setOnClickListener {
                        drawerLayout.open()
                    }
                    true
                }
                R.id.favourites -> {
                    fragmentDisplay(Favourites())
                    fragmentScreenName.setText("Favourites")
                    navigationDrawerIcon.visibility = View.GONE
                    drawerLayout.close()
                    true
                }
                R.id.categories -> {
                    fragmentDisplay(Categories())
                    fragmentScreenName.setText("Categories")
                    navigationDrawerIcon.visibility = View.GONE
                    drawerLayout.close()
                    true
                }
                else ->{
                    fragmentDisplay(HomeView())
                    fragmentScreenName.setText("Home")
                    true
                }
            }
        }
        logoutNavDrawer.setOnClickListener {
            logoutPopupAction()
        }
        addTitle.setOnClickListener {
            val intent = Intent(this@HomeScreen,AddTitle::class.java)
            startActivity(intent)
        }
        addCategory.setOnClickListener {
            val intent = Intent(this@HomeScreen,AddCategory::class.java)
            startActivity(intent)
        }
        manageProfile.setOnClickListener {
            val intent = Intent(this@HomeScreen,RegisterForAnAccount::class.java)
            startActivity(intent)
        }
    }
    fun logoutPopupAction()
    {
        val logoutPopup = Dialog(this)
        logoutPopup.setContentView(R.layout.logout_popup)
        logoutPopup.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val logoutYes:Button = logoutPopup.findViewById(R.id.logout_yes)
        val logoutNo:Button = logoutPopup.findViewById(R.id.logout_no)
        logoutPopup.show()
        logoutYes.setOnClickListener {
            firebaseAuth.signOut()
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("signinuser","")
            editor.putBoolean("userloggedin",false)
            editor.apply()
            val intent = Intent(this@HomeScreen,LoginScreen::class.java)
            startActivity(intent)
        }
        logoutNo.setOnClickListener {
            logoutPopup.dismiss()
        }
    }
    private fun fragmentDisplay(fragment:Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentviewer,fragment)
        transaction.commit()
    }
}