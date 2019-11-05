package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.service.autofill.Validators.not
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val baseUrl = "https://androidlessonsapi.herokuapp.com/api/game/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofitClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter) // Data parsing
            .build()
        val service =  retrofitClient .create(WSGameInterface::class.java)
        val callback : Callback<List<GameModel>> = object : Callback<List<GameModel>> {
            override fun onFailure(call: Call<List<GameModel>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<List<GameModel>>,
                response: Response<List<GameModel>>
            ) {
                val list = response.body()
                if (list != null) {
                    val endInclusive = list.size -1
                    val imgList = arrayListOf<ImageView>()
                    imgList.add(img1)
                    imgList.add(img2)
                    imgList.add(img3)
                    imgList.add(img4)
                    val listUsedIndex = arrayListOf<Int>();
                    var i = 0
                    while (i < list.size -1) {
                        val randomIndex = (Math.random() * ((endInclusive + 1) - i) + i).toInt()
                        if (!listUsedIndex.contains(randomIndex)) {
                            val game = list.get(randomIndex)
                            var img = imgList.get(0)
                            if (listUsedIndex.size != 0)
                                img = imgList.get(listUsedIndex.size -1)
                            img.setOnClickListener {
                                val explicitIntent = Intent(this@MainActivity, GameDatailsActivity::class.java)
                                explicitIntent.putExtra("id", game.id.toString())
                                startActivity(explicitIntent)
                            }
                            Glide
                                .with(this@MainActivity)
                                .load(game.picture)
                                .into(img)
                            listUsedIndex.add(randomIndex)
                            if (listUsedIndex.size == 5)
                                break
                        }
                        i+=1
                    }
                }
            }
        }
        service.getListGame().enqueue(callback)
    }
}
