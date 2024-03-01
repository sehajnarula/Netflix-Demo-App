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

class NetflixTrendingListViewMore : AppCompatActivity() {
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    lateinit var trendingListViewMoreList:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_netflix_trending_list_view_more)
        screenName = findViewById(R.id.screenname)
        backArrow = findViewById(R.id.backarrow)
        trendingListViewMoreList = findViewById(R.id.trendinglistviewmorelist)
        screenName.setText("Trending List")
        trendingListViewMoreList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        trendingListViewMoreList.setHasFixedSize(true)
//        trendingListData.add(VerticalViewData(R.drawable.coverduneone,"Recently Added","","Dune","Duration: 2 Hrs 40 Min","Cast: Rebecca Ferguson, Oscar Issac, Zendaya, Josh Brolin, Javier Bardem","Director: Denis Vellinue"))
//        trendingListData.add(VerticalViewData(R.drawable.covertopgun,"","","Top Gun","Duration: 1 Hr 49 Min","Cast: Tom Cruise, Kelly Mcgills, Val Kilmer","Director: Tony Scott"))
//        trendingListData.add(VerticalViewData(R.drawable.dunkicover,"Recently Added","Top 10","Dunki","Duration: 2 Hrs 40 Min","Cast: Shah Rukh Khan, Tapsee Panu, Vicky Kaushal, Anil Grover","Director: Rajkumar Hirani"))
//        trendingListData.add(VerticalViewData(R.drawable.coverjaawan,"","Top 10","Jawan","Duration: 2 Hrs 50 Min","Shah Rukh Khan, Vijay Sethupati, Nayanthara","Director: Atlee"))
//        trendingListData.add(VerticalViewData(R.drawable.coverelvis,"Recently Added","Top 10","Elvis","Duration: 2 Hrs 40Min","Cast: Tom Hanks, Austin Butler","Baz Lurhmen"))
//        trendingListData.add(VerticalViewData(R.drawable.covereeaao,"Recently Added","","Everything Everywhere All At Once","Duration: 2 Hrs 20 Min","Cast: Michelle Yoo, Jamie Lee Curtis","Director: The Daniells"))
//        trendingListData.add(VerticalViewData(R.drawable.covergodfatherone,"","","The Godfather","Duration: 3 Hrs","Cast: Al Pacino, Marlon Brando, Robert De Niro","Director: Francis Ford Coppola"))
//        trendingListData.add(VerticalViewData(R.drawable.coverpeakyblinders,"","","Peaky Blinders","Duration: 6 Seasons","Cast: Cillian Murphy, Tom Hardy, Paul Anderson", "Director: Colm McCarthy"))
//        trendingListData.add(VerticalViewData(R.drawable.strangerthingsposter,"","","Stranger Things","Duration: 4 Seasons","Cast: Millie Bobby Brown, Noah Schnapp, Finn Wolfhard","Director: The Duffer Brothers"))
//        trendingListData.add(VerticalViewData(R.drawable.godzillakongfight,"","","Godzilla Vs Kong","Duration: 1 Hr 43 Min","Cast: Millie Bobby Brown, Rebecca Hall, Alexander Skargskard","Director: Adam Wingard"))
//        trendingListData.add(VerticalViewData(R.drawable.moneyheistposter,"","","Money Heist","Duration: 4 Seasons","Alvaro Monte, Pedro Alonso, Alba Flores","Director: Alex Pina"))
//        trendingListData.add(VerticalViewData(R.drawable.sandmanposter,"","","The Sandman","Duration: 1 Season","Cast: Tom Sturridge, Boyd Holbrook","Director: David S. Goyer"))
//        trendingListData.add(VerticalViewData(R.drawable.minorityreportcover,"","","Minority Report","Duration: 2 Hrs 25 Min", "Cast: Tom Cruise, Cameron Diaz, Max Von Sydow, Colin Farell","Director: Steven Spielberg"))
//        trendingListData.add(VerticalViewData(R.drawable.coverfugitive,"","","The Fugitive","Duration: 2 Hrs 8 Min","Cast: Harrison Ford, Tommy Lee Jones","Director: Andrew Davis"))
//        trendingListData.add(VerticalViewData(R.drawable.luciferposter,"","","Lucifer","Duration: 3 Seasons","Cast: Tom Ellis, Lauren German, D.B. Woodside","Director: Tom Ellis"))
//        verticalThumbnailsAdapter = VerticalThumbnailsAdapter(trendingListData,this@NetflixTrendingListViewMore)
//        trendingListViewMoreList.adapter = verticalThumbnailsAdapter
        backArrow.setOnClickListener {
            val intent = Intent(this@NetflixTrendingListViewMore, DemoNetflixHomePage::class.java)
            startActivity(intent)
        }
    }
}