package com.zaurh.sparrow.data

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Storage

class AppwriteManager(context: Context) {
    private val client = Client(context)
        .setEndpoint("https://cloud.appwrite.io/v1")
        .setProject("67c9770d0010fbe74ecd")
    val storage = Storage(client)
}
