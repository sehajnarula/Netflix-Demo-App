package com.demo.netflix
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.adapter.VerticalThumbnailsAdapter
import com.demo.netflix.modelclasses.VerticalViewData

class BecauseYouSawVerticalView : AppCompatActivity() {
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    lateinit var becauseYouSawVerticalList:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_because_you_saw_vertical_view)
        screenName = findViewById(R.id.screenname)
        backArrow = findViewById(R.id.backarrow)
        screenName.setText("Because You Saw The Batman")
        becauseYouSawVerticalList = findViewById(R.id.becauseyousawviewmorelist)
        becauseYouSawVerticalList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        backArrow.setOnClickListener {
            val intent = Intent(this@BecauseYouSawVerticalView, DemoNetflixHomePage::class.java)
            startActivity(intent)
        }

    }
}