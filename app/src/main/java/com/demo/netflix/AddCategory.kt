package com.demo.netflix
import android.content.Context
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
import com.demo.netflix.modelclasses.CategoryData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
class AddCategory : AppCompatActivity() {
    lateinit var submitCategoryButton:Button
    lateinit var addCategory:EditText
    var categoryList:ArrayList<CategoryData> = ArrayList()
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    lateinit var progressBar:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)
        addCategory = findViewById(R.id.addcategorytext)
        submitCategoryButton = findViewById(R.id.submitcategorybutton)
        backArrow = findViewById(R.id.backarrow)
        screenName = findViewById(R.id.screenname)
        progressBar = findViewById(R.id.progressloader)
        screenName.setText("Add Category")
        val databaseLink = Firebase.firestore
        databaseLink.collection("categories").addSnapshotListener { value, error ->
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val categoryData:CategoryData = dataSnapshot.toObject(CategoryData::class.java)!!
                        categoryData.catDocId = dataSnapshot.id
                        categoryList.add(categoryData)
                    }
                }
            }
            if (error!=null)
            {
                Log.d("addgetcategory",error.toString())
            }
        }
        submitCategoryButton.setOnClickListener {
            if (TextUtils.isEmpty(addCategory.text.toString()))
            {
                Toast.makeText(this@AddCategory, "Please add the name of category", Toast.LENGTH_SHORT).show()
            }
            else
            {
              closeKeyboard(addCategory)
              addData()
            }
        }
        backArrow.setOnClickListener {
            finish()
        }
    }
    public fun addData()
    {
        var savedCategories:Boolean = false
        for(i in categoryList)
        {
            if (addCategory.text.toString().equals(i.catName))
            {
                savedCategories = true
            }
        }
        if (savedCategories)
        {
            Toast.makeText(this@AddCategory, "Category already exists", Toast.LENGTH_SHORT).show()
        }
        else
        {
            progressBar.visibility = View.VISIBLE
            var putCategory:HashMap<String,String> = HashMap()
            putCategory.put("catName",addCategory.text.toString())
            val databaseLink = Firebase.firestore
            databaseLink.collection("categories").document().set(putCategory).addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddCategory, "Category Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AddCategory, "Unable To Add Category", Toast.LENGTH_SHORT).show()
            }
        }
    }
    public fun closeKeyboard(view: EditText){
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,1)
        view.clearFocus()
    }
}