package com.example.musicapp

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentSecondBinding
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger

class SecondFragment : Fragment() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var metaRetriever: MediaMetadataRetriever
    private lateinit var seekBar: SeekBar

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

        metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(context?.applicationContext!!, FileHandler.currentUri)

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context?.applicationContext!!, FileHandler.currentUri)
        mediaPlayer.prepare()

        //Mediaplayer attributes
        mediaPlayer.setVolume(1f, 1f)

        setPlaybackSpeed(1f)

        binding.trackLength.text = formattedTime(mediaPlayer.duration / 1000)

        setTitle()

        seekBar = binding.seekBar
        seekBar.max = mediaPlayer.duration
        progressThread.start()

        binding.playPauseButton.setOnClickListener {
            val button = binding.playPauseButton
            if (mediaPlayer.isPlaying) {
                button.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_play, null))
                mediaPlayer.pause()
            } else {
                button.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_pause, null))
                mediaPlayer.start()
            }
        }

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.prepare()
        }

        binding.speedSeekBar.setOnSeekBarChangeListener(binding)
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

    private fun setPlaybackSpeed(speed: Float) {
        val playbackParams = PlaybackParams()
        playbackParams.speed = speed
        mediaPlayer.playbackParams = playbackParams
    }

    private val progressThread = Thread {
        while (true) {
            if (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
                binding.trackProgressTime.text = formattedTime(mediaPlayer.currentPosition / 1000)
                Thread.sleep(200)
            }
        }
    }

    private fun SeekBar.setOnSeekBarChangeListener(binding: FragmentSecondBinding) {
        val lowSpeed = getString(R.string.low_speed).substring(0, getString(R.string.low_speed).length - 1).toFloat()
        val highSpeed = getString(R.string.high_speed).substring(0, getString(R.string.high_speed).length - 1).toFloat()
        val barProgress = binding.seekBar.progress.toFloat()
        val newSpeed = (highSpeed - lowSpeed) * (barProgress / 100)
        setPlaybackSpeed(newSpeed)
    }
}