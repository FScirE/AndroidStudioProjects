package com.example.groupapp

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.groupapp.databinding.FragmentFirstBinding
import java.io.File
import kotlin.math.roundToInt
import kotlin.random.Random

class PersonHandler {
    private var nameList = mutableListOf<String>()
    val fh = FileHandler()

    fun initialize() {
        nameList = fh.getListFromFile()
    }

    fun addNameToList(name: String): Boolean {
        if (name != "") {
            for (n in nameList) {
                if (n == name) return false
            }
        }

        nameList.add(name)
        fh.appendTextToFile(name)
        return true
    }

    fun removeNameFromList(): Boolean {
        if (nameListSize() < 1) return false
        nameList.removeLast()
        fh.removeLastRowFromFile()
        return true
    }

    fun namesAsString(): String {
        var newText = ""
        for (n in nameList) {
            newText += n + "\n"
        }
        return newText
    }

    fun peoplePerGroup(numGroups: String): String {
        var ppg: Int
        if (numGroups != "" && nameListSize() > 0 && numGroups.toInt() > 0) {
            ppg = (nameListSize() / numGroups.toFloat()).roundToInt()
            if (ppg == 0) ppg = 1
            return "People per group: ~$ppg"
        }
        return "People per group: ~-"
    }

    fun nameListSize(): Int {
        return nameList.size
    }

    fun namesAsRandomGroups(numGroups: Int): String {
        val r = Random(System.nanoTime())
        val randomized = (nameList as List<String>).shuffled(r)

        var tp = nameListSize()
        var output = ""

        var index = 0
        var i = numGroups
        while (i > 0) {
            val ppg = (tp / i.toFloat()).roundToInt()
            output += "----------- GROUP ${numGroups - i + 1} -----------\n"
            for (j in 1 .. ppg ) {
                output += randomized[index] + "\n"
                index++
            }
            output += "\n"

            tp -= ppg
            i--
        }

        return output
    }
}