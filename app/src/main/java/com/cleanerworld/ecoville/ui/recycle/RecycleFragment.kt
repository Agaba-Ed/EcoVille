package com.cleanerworld.ecoville.ui.recycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cleanerworld.ecoville.databinding.FragmentRecycleBinding

class RecycleFragment : Fragment() {

    private var _binding: FragmentRecycleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recycleViewModel =
            ViewModelProvider(this).get(RecycleViewModel::class.java)

        _binding = FragmentRecycleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRecycle
        recycleViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}