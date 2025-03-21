package com.example.groupapp

import android.content.Context
import java.io.File

class FileHandler {
    private lateinit var file: File
    private lateinit var resultFile: File

    fun initialize(applicationContext: Context, filename: String, resultFilename: String) {
        file = File(applicationContext.filesDir, filename)
        resultFile = File(applicationContext.filesDir, resultFilename)
        file.createNewFile()
        resultFile.createNewFile()
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

    fun saveResult(result: String) {
        resultFile.writeText("")
        val lines = result.split("\n")
        for (l in lines) resultFile.appendText(l + "\n")
    }

    fun getResultFromFile(): String {
        var string = ""
        resultFile.forEachLine { string += it + "\n" }
        return string
    }
}