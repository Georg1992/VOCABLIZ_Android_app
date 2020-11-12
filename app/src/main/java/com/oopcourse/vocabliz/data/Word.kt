//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.data
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Word(val lang:String,
                val text:String,
                val translations:List<Translation>){

}

