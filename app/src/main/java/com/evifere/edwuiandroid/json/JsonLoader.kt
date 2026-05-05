package com.evifere.edwuiandroid.json

import android.content.Context

class JsonLoader {

    companion object {
    public fun loadJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
    }
}
