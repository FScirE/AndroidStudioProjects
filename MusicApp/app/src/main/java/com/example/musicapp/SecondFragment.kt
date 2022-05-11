package com.example.musicapp

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.example.musicapp.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    lateinit var mediaPlayer: MediaPlayer

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

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context?.applicationContext!!, FileHandler.currentUri)
        mediaPlayer.prepare()

        val totalSeconds = mediaPlayer.duration.toString().toFloat() / 1000
        val hours = (totalSeconds / 3600).toInt()
        val minutes = ((totalSeconds % 3600) / 60).toInt()
        val seconds = (totalSeconds % 60).toInt()

        if (totalSeconds >= 3600) binding.trackLength.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else binding.trackLength.text = String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}