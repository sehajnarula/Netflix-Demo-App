package com.demo.netflix
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.netflix.adapter.CategorySpinnerAdapter
import com.demo.netflix.modelclasses.CategoryData
import com.demo.netflix.modelclasses.MovieDataClass
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
class AddTitle : AppCompatActivity() {
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    lateinit var titleName:EditText
    lateinit var titleVideoid:EditText
    lateinit var titleDescription:EditText
    lateinit var progressBar:RelativeLayout
    var movieList:ArrayList<MovieDataClass> = ArrayList()
    var categoryList:ArrayList<CategoryData> = ArrayList()
    lateinit var addTitle:Button
    lateinit var categoryDropDown:Spinner
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_title)
        screenName = findViewById(R.id.screenname)
        backArrow = findViewById(R.id.backarrow)
        titleName = findViewById(R.id.addtitlename)
        titleVideoid = findViewById(R.id.addtitlevideoid)
        titleDescription = findViewById(R.id.addtitledescription)
        progressBar = findViewById(R.id.progressloader)
        addTitle = findViewById(R.id.addtitlebutton)
        categoryDropDown = findViewById(R.id.categorydropdown)
        screenName.setText("Add Title")
        getData()
        backArrow.setOnClickListener {
            finish()
        }
        addTitle.setOnClickListener {
            if (TextUtils.isEmpty(titleName.text.toString()))
            {
                Toast.makeText(this@AddTitle, "Please Enter Title Name", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(titleVideoid.text.toString()))
            {
                Toast.makeText(this@AddTitle, "Please Enter Video Id Of Title", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(titleDescription.text.toString()))
            {
                Toast.makeText(this@AddTitle, "Please Enter Description Of Title", Toast.LENGTH_SHORT).show()
            }
            else{
                closeKeyboard(titleDescription)
                addData()
            }
        }
    }
    public fun getData()
    {
        val databaseLink = Firebase.firestore
        databaseLink.collection("appData").addSnapshotListener { value, error ->
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val movieDataClass:MovieDataClass = dataSnapshot.toObject(MovieDataClass::class.java)!!
                        movieDataClass.movieId = dataSnapshot.id
                        movieList.add(movieDataClass)
                    }
                }
            }
            if (error!=null)
            {
                Log.d("titlefetcherror",error.toString())
            }
        }
        databaseLink.collection("categories").addSnapshotListener { value, error ->
            if (value!=null)
            {
                for (categorySnapshot in value.documents)
                {
                    if (categorySnapshot!=null)
                    {
                        val categoryData:CategoryData = categorySnapshot.toObject(CategoryData::class.java)!!
                        categoryData.catDocId = categorySnapshot.id
                        categoryList.add(categoryData)
                    }
                }
            }
            categorySpinnerAdapter = CategorySpinnerAdapter(categoryList,this@AddTitle)
            categoryDropDown.adapter = categorySpinnerAdapter
            if (error!=null)
            {
                Log.d("categoryfetcherror",error.toString())
            }
        }
    }
    public fun addData()
    {
        var savedTitle:Boolean = false
        //val selectedSpinnerText:String = categoryDropDown.selectedItem.toString()
        for (i in movieList)
        {
            if (titleVideoid.text.toString().equals(i.videoId))
            {
                savedTitle = true
            }
        }
        if (savedTitle)
        {
            Toast.makeText(this@AddTitle, "This title already exists", Toast.LENGTH_SHORT).show()
        }
        else {
            progressBar.visibility = View.VISIBLE
            val dataBaseLink = Firebase.firestore
            val putTitleData: HashMap<String, String> = HashMap()
            putTitleData.put("title", titleName.text.toString())
            putTitleData.put("videoId", titleVideoid.text.toString())
            putTitleData.put("desc", titleDescription.text.toString())
            //putTitleData.put("catId",selectedSpinnerText)
            dataBaseLink.collection("appData").document().set(putTitleData).addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddTitle, "Title Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddTitle, "Unable To Add Title", Toast.LENGTH_SHORT).show()
            }
        }
    }
    public fun closeKeyboard(view: EditText){
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,1)
        view.clearFocus()
    }
}