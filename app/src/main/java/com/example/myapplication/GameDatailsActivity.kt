package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game_scription.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameDatailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_scription)
        val originIntent = intent
        val id = originIntent.getStringExtra("id")
        val baseUrl = "https://androidlessonsapi.herokuapp.com/api/game/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofitClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter) // Data parsing
            .build()
        val service =  retrofitClient .create(WSGameInterface::class.java)
        val callback : Callback<GameDetailsModel> = object : Callback<GameDetailsModel> {
            override fun onFailure(call: Call<GameDetailsModel>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<GameDetailsModel>,
                response: Response<GameDetailsModel>
            ) {
                val gameDetails = response.body()
                if (gameDetails != null) {
                    Glide
                        .with(this@GameDatailsActivity)
                        .load(gameDetails.picture)
                        .into(imgLogo)
                    textViewName.text = Html.fromHtml("<b>Name: </b>" + gameDetails.name)
                    textViewType.text = Html.fromHtml("<b>Type: </b" + gameDetails.type)
                    textViewNbPlayers.text = Html.fromHtml("<b>Nb players: </b>" + gameDetails.players)
                    if (gameDetails.year != null)
                        textViewYear.text = Html.fromHtml("<b>Year: </b>" + gameDetails.year)
                    wiki_button.setOnClickListener {
                        val implicitIntent = Intent(Intent.ACTION_VIEW)
                        implicitIntent.data = Uri.parse(gameDetails.url)
                        startActivity(implicitIntent)
                    }
                    if (gameDetails.description_fr != null)
                        textViewDesc.text = gameDetails.description_fr
                    else
                        textViewDesc.text = gameDetails.description_en
                }
            }
        }
        service.getGameDetails(id).enqueue(callback)
    }
}
