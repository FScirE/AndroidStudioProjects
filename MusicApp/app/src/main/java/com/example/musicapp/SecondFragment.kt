package com.example.musicapp

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var metaRetriever: MediaMetadataRetriever
    private lateinit var seekBar: SeekBar
    private var threadRunning = true
    private var previousPlayState = false

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
        mediaPlayer.setVolume(0.5f, 0.5f)

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

        binding.repeatButton.setOnClickListener {
            if (mediaPlayer.isLooping) {
                repeatOff()
            } else {
                repeatOn()
            }
        }

        mediaPlayer.setOnCompletionListener {
            stopMediaPlayer()
            //Update seekbar pos to end of file
            seekBar.progress = mediaPlayer.currentPosition
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

        seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.trackProgressTime.text = formattedTime(progress / 1000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                previousPlayState = mediaPlayer.isPlaying
                stopMediaPlayer()
                mediaPlayer.prepare()
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mediaPlayer.seekTo(seekBar.progress)
                if (previousPlayState) startMediaPlayer()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        threadRunning = false
        progressThread.join()

        mediaPlayer.stop()
        mediaPlayer.release()
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
        if (title == null) title = FileHandler.getFileName(context?.applicationContext!!)
        binding.songName.text = title
    }
    private fun setImage() {
        var art: ByteArray? = null
        try {
            art = metaRetriever.embeddedPicture
        } catch(e: Exception) { }
        if (art == null) binding.songImage.setImageDrawable(ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_report_image, null))
        else binding.songImage.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.size))
    }

    private fun setPlaybackSpeed(speed: Float) {
        if (mediaPlayer.isPlaying) {
            val playbackParams = PlaybackParams()
            playbackParams.speed = speed
            mediaPlayer.playbackParams = playbackParams
            binding.currentSpeed.text = (speed * 100).toInt().toString() + "%"
        } else {
            mediaPlayer.setVolume(0f, 0f)
            val playbackParams = PlaybackParams()
            playbackParams.speed = speed
            mediaPlayer.playbackParams = playbackParams
            binding.currentSpeed.text = (speed * 100).toInt().toString() + "%"
            pauseMediaPlayer()
            mediaPlayer.setVolume(0.5f, 0.5f)
        }
    }

    private val progressThread = Thread {
        while (threadRunning) {
            if (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
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
    private fun stopMediaPlayer() {
        binding.playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(resources ,android.R.drawable.ic_media_play, null))
        mediaPlayer.stop()
    }

    private fun repeatOn() {
        binding.repeatButton.background.setTint(context?.getColorFromAttr(android.R.attr.colorActivatedHighlight)!!)
        mediaPlayer.isLooping = true
    }
    private fun repeatOff() {
        binding.repeatButton.background.setTint(context?.getColorFromAttr(android.R.attr.colorAccent)!!)
        mediaPlayer.isLooping = false
    }

    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }
}