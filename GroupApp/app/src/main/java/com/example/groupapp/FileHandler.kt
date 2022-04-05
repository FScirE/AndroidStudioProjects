package com.example.groupapp

import android.content.Context
import java.io.File

class FileHandler {
    private lateinit var file: File

    fun initialize(applicationContext: Context, filename: String) {
        file = File(applicationContext.filesDir, filename)
        file.createNewFile()
    }

    fun appendTextToFile(text: String) {
        file.appendText(text + "\n", charset("UTF-8"))
    }

    fun removeLastRowFromFile() {
        val newList = getListFromFile()
        newList.removeLast()

        file.writeText("")
        for (s in newList) {
            file.appendText(s + "\n", charset("UTF-8"))
        }
    }

    fun getListFromFile(): MutableList<String> {
        val list = mutableListOf<String>()
        file.forEachLine { list.add(it) }
        return list
    }
}