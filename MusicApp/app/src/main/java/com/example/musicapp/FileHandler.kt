package com.example.musicapp

import android.content.Context
import android.net.Uri
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import java.io.File
import java.net.URI

abstract class FileHandler {
    companion object {
        var currentUri = Uri.EMPTY
    }
}