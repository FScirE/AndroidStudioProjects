package com.example.musicapp

import android.graphics.BitmapFactory
import android.media.Image
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentSecondBinding
import java.io.File


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

        binding.trackLength.text = formattedTime(mediaPlayer.duration / 1000)

        setTitle()
        setImage()

        seekBar = binding.seekBar
        seekBar.max = mediaPlayer.duration
        progressThread.start()

        binding.playPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pauseMediaPlayer()
            } else {
                startMediaPlayer()
            }
        }

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.prepare()
        }

        binding.speedSeekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val lowSpeed = (getString(R.string.low_speed).substring(0, getString(R.string.low_speed).length - 1)).toFloat()
                val highSpeed = (getString(R.string.high_speed).substring(0, getString(R.string.high_speed).length - 1)).toFloat()
                val newSpeed = ((highSpeed - lowSpeed) * (seekBar.progress.toFloat() / 100) + lowSpeed) / 100
                setPlaybackSpeed(newSpeed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //Something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //Something
            }
        })
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
    private fun setImage() {
        val art: ByteArray? = metaRetriever.embeddedPicture
        if (art == null) binding.songImage.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_menu_report_image, null))
        binding.songImage.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art!!.size))
    }

    private fun setPlaybackSpeed(speed: Float) {
        pauseMediaPlayer()
        val playbackParams = PlaybackParams()
        playbackParams.speed = speed
        mediaPlayer.playbackParams = playbackParams
        startMediaPlayer()
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

    private fun pauseMediaPlayer() {
        binding.playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_play, null))
        mediaPlayer.pause()
    }

    private fun startMediaPlayer() {
        binding.playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_pause, null))
        mediaPlayer.start()
    }
}