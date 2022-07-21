package com.bignerdranch.android.photogallery.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import com.google.gson.Gson

class PhotoDeserializer: JsonDeserializer<PhotoResponse> {
    //    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): PhotoResponse {
//
//    }
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PhotoResponse {
        val jsObject = json?.asJsonObject?.get("photos")?.asJsonObject
//        val jsonArray = jsObject?.getAsJsonArray("photo")?.asJsonArray
        val photos = Gson().fromJson(jsObject, PhotoResponse::class.java)
        return photos
    }
}