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

class OnlyOnNetflixViewMore : AppCompatActivity() {
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    lateinit var onlyOnNetflixViewMoreList:RecyclerView
    lateinit var verticalThumbnailsAdapter: VerticalThumbnailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_only_on_netflix_view_more)
        screenName = findViewById(R.id.screenname)
        onlyOnNetflixViewMoreList = findViewById(R.id.onlyonnetflixviewmorelist)
        backArrow = findViewById(R.id.backarrow)
        screenName.setText("Only On Netflix")
        onlyOnNetflixViewMoreList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        onlyOnNetflixViewMoreList.setHasFixedSize(true)
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.coverglassonion,"","","Glass Onion","Duration: 2 Hrs 19 Min","Cast: Daniel Craig, Edward Norton, Dave Bautiesta, Ethan Hawke","Director: Rian Johnson"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.strangerthingsposter,"","","Stranger Things","Duration: 4 Seasons","Cast: Millie Bobby Brown, Noah Schnapp, Finn Wolfhard","Director: The Duffer Brothers"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.moneyheistposter,"","","Money Heist","Duration: 4 Seasons","Alvaro Monte, Pedro Alonso, Alba Flores","Director: Alex Pina"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.houseofcardsposter,"","","House Of Cards","Duration: 3 Seasons","Robin Wright, Michael Kelly, Kevin Spaces","Director: David Fincher"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.coverpeakyblinders,"","","Peaky Blinders","Duration: 6 Seasons","Cast: Cillian Murphy, Tom Hardy, Paul Anderson", "Director: Colm McCarthy"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.extractioncover,"","","Extraction","Cast: 1 Hr 50 Min","Cast: Chris Hemsworth, Pankaj Tripathi, Randeep Hooda","Director: Joe Russo"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.extractiontwo,"","","Extraction 2","Cast: 1 Hr 50 Min","Cast: Chris Hemsworth, Pankaj Tripathi, Randeep Hooda","Director: Joe Russo"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.sandmanposter,"","","The Sandman","Duration: 1 Season","Cast: Tom Sturridge, Boyd Holbrook","Director: David S. Goyer"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.mindhunterposter,"","","Mindhunter","Duration: 2 Seasons","Cast: Gary Oldman","Director: David Fincher"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.triplefrontierposter,"","","Triple Frontier","Duration: 1hr 49Min", "Cast: Ben Affleck, Oscar Issac, Pedro Pascal","Director: J.C. Chandor"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.luciferposter,"","","Lucifer","Duration: 3 Seasons","Cast: Tom Ellis, Lauren German, D.B. Woodside","Director: Tom Ellis"))
//        onlyOnNetflixMoreData.add(VerticalViewData(R.drawable.coverokja,"","","Okja","Duration: 2 Hrs","Cast: Jake Gyllenhall, Paul Dano, Lilly Collins, Tilda Swinton,Giancarlo Esposito","Director : Bong Joon-Ho"))
//        verticalThumbnailsAdapter = VerticalThumbnailsAdapter(onlyOnNetflixMoreData,this@OnlyOnNetflixViewMore)
//        onlyOnNetflixViewMoreList.adapter = verticalThumbnailsAdapter
        backArrow.setOnClickListener {
            val intent = Intent(this@OnlyOnNetflixViewMore, DemoNetflixHomePage::class.java)
            startActivity(intent)
        }
    }
}