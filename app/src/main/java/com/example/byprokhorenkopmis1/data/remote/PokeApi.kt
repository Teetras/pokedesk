package com.example.byprokhorenkopmis1.data.remote

import retrofit2.http.Query


interface PokeApi {
    suspend fun getPokemonList(){
       // @Query("limit") limit: Int,

    }
}