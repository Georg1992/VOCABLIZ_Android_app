//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.network

import com.oopcourse.vocabliz.data.Word
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type


private const val BASE_URL =
    "https://users.metropolia.fi/~georgv/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


var listMyData: Type = Types.newParameterizedType(
    MutableList::class.java,
    Word::class.java
)
var adapter: JsonAdapter<List<Word>> = moshi.adapter<List<Word>>(listMyData)


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WordsApiService {
    @GET("Dictionary")
    suspend fun getProperties():
            MutableList<Word>
}

object WordsApi {
    val retrofitService : WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java) }
}