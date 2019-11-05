package com.example.myapplication;

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSGameInterface {

    @GET("list")
    fun getListGame() : Call<List<GameModel>>

    @GET("details")
    fun getGameDetails(@Query("game_id") game_id :String) : Call<GameDetailsModel>
}