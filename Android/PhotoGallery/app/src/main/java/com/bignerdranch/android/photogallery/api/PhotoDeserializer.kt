package com.bignerdranch.android.photogallery.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import com.google.gson.Gson

class PhotoDeserializer: JsonDeserializer<PhotoResponse> {
    private var page: Int? = 0
    private var pages: Int? = 0
    var perpage: Int? = 0
    var total: Int? = 0

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PhotoResponse {
        val jsObject = json?.asJsonObject?.get("photos")?.asJsonObject
        page = jsObject?.get("page")?.asInt
        pages = jsObject?.get("pages")?.asInt
        perpage = jsObject?.get("perpage")?.asInt
        total = jsObject?.get("total")?.asInt
        val photos = Gson().fromJson(jsObject, PhotoResponse::class.java)
        page?.let { photos.setPage(it) }
        pages?.let { photos.setPages(it) }
        return photos
    }
}