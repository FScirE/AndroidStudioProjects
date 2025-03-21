package com.example.groupapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.groupapp.databinding.FragmentStartBinding

/**
 * A simple [Fragment] subclass.
 */
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize files and personhandler
        Holder.Value.ph.fh.initialize(requireContext().applicationContext, "people.txt", "result.txt")
        Holder.Value.ph.initialize()

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_FirstFragment)
        }

        binding.lastResult.setOnClickListener {
            findNavController().navigate(R.id.action_StartFragment_to_resultFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //-------------------------

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}