package com.example.musicapp

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentSecondBinding
import java.io.File

class SecondFragment : Fragment() {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var metaRetriever: MediaMetadataRetriever
    lateinit var thread: Thread

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thread = Thread(progressUpdater)
        thread.start()

        metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(context?.applicationContext!!, FileHandler.currentUri)

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context?.applicationContext!!, FileHandler.currentUri)
        mediaPlayer.prepare()

        mediaPlayer.setVolume(1f, 1f)

        val totalSeconds = mediaPlayer.duration.toString().toInt() / 1000
        binding.trackLength.text = formattedTime(totalSeconds)

        setTitle()

        createProgressParentThread()

        binding.playPauseButton.setOnClickListener {
            val button = binding.playPauseButton
            if (mediaPlayer.isPlaying()) {
                button.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_play, null))
                mediaPlayer.pause()
            } else {
                button.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_pause, null))
                mediaPlayer.start()
            }
        }

        mediaPlayer.setOnCompletionListener {

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formattedTime(totalSeconds: Int): String {
        val hours = (totalSeconds / 3600)
        val minutes = ((totalSeconds % 3600) / 60)
        val seconds = (totalSeconds % 60)

        return if (totalSeconds >= 3600) String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }

    private fun setTitle() {
        var title: String? = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        if (title == null) title = File(FileHandler.currentUri.path!!).name
        binding.songName.text = title
    }

    var progressUpdater: Runnable? = null
    private fun createProgressParentThread() {
        progressUpdater = Runnable {
            while (mediaPlayer.isPlaying()) {
                try {
                    var current = 0
                    val total: Int = mediaPlayer.getDuration()
                    binding.seekBar.max = total
                    while (current < total) {
                        try {
                            Thread.sleep(1000) //Update once per second
                            current = mediaPlayer.getCurrentPosition()
                            binding.seekBar.progress = current
                        } catch (e: InterruptedException) { }
                        catch (e: Exception) { }
                    }
                } catch (e: Exception) { }
            }
        }
    }
}