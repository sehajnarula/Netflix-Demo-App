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
class NetflixHistorySuggestions : AppCompatActivity() {
    lateinit var historySuggestionsVerticalList:RecyclerView
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_netflix_history_suggestions)
        historySuggestionsVerticalList = findViewById(R.id.historysuggestionsverticallist)
        screenName = findViewById(R.id.screenname)
        backArrow = findViewById(R.id.backarrow)
        screenName.setText("We Think You'll Love These")
        screenName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumbsupsmall,0,0,0)
        historySuggestionsVerticalList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        historySuggestionsVerticalList.setHasFixedSize(true)
        backArrow.setOnClickListener {
            val intent = Intent(this@NetflixHistorySuggestions, DemoNetflixHomePage::class.java)
            startActivity(intent)
        }
//        historySuggestionList.add(VerticalViewData(R.drawable.coverokja,"Recently Added","Top 10","Okja","Duration: 2 Hrs","Cast: Jake Gyllenhall, Paul Dano, Lilly Collins, Tilda Swinton,Giancarlo Esposito","Director : Bong Joon-Ho"))
//        historySuggestionList.add(VerticalViewData(R.drawable.covertopgunmaverick,"Recently Added","","Top Gun Maverick","Duration: 2 Hrs 10 Min","Cast: Tom Cruise, Val Kilmer, Glen Powell, Jennifer Connelly, Miles Teller","Director : Joseph Kosinski"))
//        historySuggestionList.add(VerticalViewData(R.drawable.covernowayhome,"","","Spider Man No Way Home","Duration: 2 Hrs 22 Min","Cast :Tom Holland, Jake Gyllenhall, Zendaya, Jon Favreau, Tobey Maguire, Andrew Garfield, Willem Dafoe,Jamie Foxx","Director : Jon Watts"))
//        historySuggestionList.add(VerticalViewData(R.drawable.covermifour,"","","Mission Impossible Ghost Protocol","Duration: 2 Hrs 11 Min","Cast: Tom Cruise, Simon Pegg, Ving Rhames, Michelle Monaghan, Paula Patton, Jeremy Renner","Director : Brad Bird"))
//        historySuggestionList.add(VerticalViewData(R.drawable.coverglassonion,"","","Glass Onion","Duration: 2 Hrs 19 Min","Cast: Daniel Craig, Edward Norton, Dave Bautiesta, Ethan Hawke","Director: Rian Johnson"))
//        historySuggestionList.add(VerticalViewData(R.drawable.coverthebatmannew,"","Top 10","The Batman","Duration: 2 Hrs 56 Min","Cast: Robert Pattinson, Andy Serkis, Paul Dano, Jeffrey Wright, Zoe Kravitz, Colin Farrell","Director : Matt Reeves"))
//        historySuggestionList.add(VerticalViewData(R.drawable.covergodfatherone,"","","The Godfather","Duration: 3 Hrs","Cast: Al Pacino, Marlon Brando, Robert De Niro","Director: Francis Ford Coppola"))
//        historySuggestionList.add(VerticalViewData(R.drawable.coverelvis,"Recently Added","Top 10","Elvis","Duration: 2 Hrs 40Min","Cast: Tom Hanks, Austin Butler","Baz Lurhmen"))
//        historySuggestionList.add(VerticalViewData(R.drawable.godzillakongfight,"","","Godzilla Vs Kong","Duration: 1 Hr 43 Min","Cast: Millie Bobby Brown, Rebecca Hall, Alexander Skargskard","Director: Adam Wingard"))
//        historySuggestionList.add(VerticalViewData(R.drawable.coverfugitive,"","","The Fugitive","Duration: 2 Hrs 8 Min","Cast: Harrison Ford, Tommy Lee Jones","Director: Andrew Davis"))
//        historySuggestionList.add(VerticalViewData(R.drawable.covereeaao,"","","Everything Everywhere All At Once","Duration: 2 Hrs 20 Min","Cast: Michelle Yoo, Jamie Lee Curtis","Director: The Daniells"))
//        historySuggestionList.add(VerticalViewData(R.drawable.coverjaawan,"","Top 10","Jawan","Duration: 2 Hrs 50 Min","Shah Rukh Khan, Vijay Sethupati, Nayanthara","Director: Atlee"))
//        historySuggestionList.add(VerticalViewData(R.drawable.dunkicover,"Recently Added","Top 10","Dunki","Duration: 2 Hrs 40 Min","Cast: Shah Rukh Khan, Tapsee Panu, Vicky Kaushal, Anil Grover","Director: Rajkumar Hirani"))
//        historySuggestionList.add(VerticalViewData(R.drawable.triplefrontierposter,"","","Triple Frontier","Duration: 1hr 49Min", "Cast: Ben Affleck, Oscar Issac, Pedro Pascal","Director: J.C. Chandor"))
//        historySuggestionList.add(VerticalViewData(R.drawable.mindhunterposter,"","","Mindhunter","Duration: 2 Seasons","Cast: Gary Oldman","Director: David Fincher"))
//        verticalThumbnailsAdapter = VerticalThumbnailsAdapter(historySuggestionList,this)
//        historySuggestionsVerticalList.adapter = verticalThumbnailsAdapter
    }
}