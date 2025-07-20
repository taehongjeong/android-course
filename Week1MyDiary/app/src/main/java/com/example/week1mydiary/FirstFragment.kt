package com.example.week1mydiary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1mydiary.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var diaryAdapter: DiaryAdapter
    private val diaryEntries = mutableListOf<DiaryEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        loadDiaryEntries()
    }
    
    private fun setupRecyclerView() {
        diaryAdapter = DiaryAdapter { entry ->
            val bundle = Bundle().apply {
                putLong("entryId", entry.id)
            }
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
        
        binding.recyclerviewDiary.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diaryAdapter
        }
    }
    
    private fun loadDiaryEntries() {
        val prefs = requireContext().getSharedPreferences("diary_prefs", android.content.Context.MODE_PRIVATE)
        val entriesJson = prefs.getString("diary_entries", null)
        
        if (entriesJson != null) {
            try {
                val entries = DiaryManager.fromJson(entriesJson)
                diaryEntries.clear()
                diaryEntries.addAll(entries)
                updateUI()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            updateUI()
        }
    }
    
    private fun updateUI() {
        if (diaryEntries.isEmpty()) {
            binding.textviewEmpty.visibility = View.VISIBLE
            binding.recyclerviewDiary.visibility = View.GONE
        } else {
            binding.textviewEmpty.visibility = View.GONE
            binding.recyclerviewDiary.visibility = View.VISIBLE
            diaryAdapter.submitList(diaryEntries.toList())
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadDiaryEntries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}