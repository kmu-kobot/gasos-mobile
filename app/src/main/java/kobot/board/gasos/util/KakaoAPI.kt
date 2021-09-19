package kobot.board.gasos.util

import kobot.board.gasos.data.ResultSearchAddress
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {
    @GET("v2/local/search/address.json")
    fun getSearchAddress(
        @Header("Authorization") key : String,
        @Query("query") query : String
    ) : Call<ResultSearchAddress>
}