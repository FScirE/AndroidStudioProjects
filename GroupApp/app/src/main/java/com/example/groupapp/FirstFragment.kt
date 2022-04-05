package com.example.groupapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.groupapp.databinding.FragmentFirstBinding
import org.w3c.dom.Text

val ph = Holder.Value.ph
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listNames = binding.listNames

        //Recover values when returning
        recoverValues(listNames)

        binding.addName.setOnClickListener {
            val name = binding.inputName.text.toString()
            if (!ph.addNameToList(name)) toast("Name already exists in list")
            listNames.text = ph.namesAsString()

            refreshPpg(binding.numGroups.text.toString())
        }

        binding.removeName.setOnClickListener {
            if (!ph.removeNameFromList()) toast("No names in list")
            listNames.text = ph.namesAsString()
        }

        binding.numGroups.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) { }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                refreshPpg(s.toString())
            }
        })

        binding.buttonFirst.setOnClickListener {
            if (ph.nameListSize() > 0) findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            else toast("No names in list")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //----------------------

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    //-----------------------

    private fun toast(text: String) {
        val myToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        myToast.show()
    }

    private fun refreshPpg(input: String) {
        binding.peoplePerGroup.text = ph.peoplePerGroup(input)

        if (input != "") Holder.Value.gr = input.toInt()
        else Holder.Value.gr = 1
    }

    private fun recoverValues(listNames: TextView) {
        listNames.text = ph.namesAsString()
        if (listNames.text == "") listNames.text = getString(R.string.listnames)

        binding.numGroups.setText(Holder.Value.gr.toString(), TextView.BufferType.EDITABLE)
        refreshPpg(binding.numGroups.text.toString())
    }
}