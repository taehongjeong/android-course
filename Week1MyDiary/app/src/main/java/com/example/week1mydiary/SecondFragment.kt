package com.example.week1mydiary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.week1mydiary.databinding.FragmentSecondBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    
    private var entryId: Long = 0L
    private var selectedMood = "😊"
    private var editingEntry: DiaryEntry? = null
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        entryId = arguments?.getLong("entryId", 0L) ?: 0L
        
        setupMoodSelector()
        setupSaveButton()
        
        if (entryId != 0L) {
            loadEntry(entryId)
        }
    }
    
    private fun setupMoodSelector() {
        val moodViews = listOf(
            binding.moodHappy to "😊",
            binding.moodSad to "😢",
            binding.moodAngry to "😡",
            binding.moodNeutral to "😐",
            binding.moodLove to "😍"
        )
        
        moodViews.forEach { (view, mood) ->
            view.setOnClickListener {
                selectedMood = mood
                updateMoodSelection()
            }
        }
        
        updateMoodSelection()
    }
    
    private fun updateMoodSelection() {
        val moodViews = listOf(
            binding.moodHappy, binding.moodSad, binding.moodAngry,
            binding.moodNeutral, binding.moodLove
        )
        
        moodViews.forEach { view ->
            view.alpha = 0.5f
        }
        
        when (selectedMood) {
            "😊" -> binding.moodHappy.alpha = 1.0f
            "😢" -> binding.moodSad.alpha = 1.0f
            "😡" -> binding.moodAngry.alpha = 1.0f
            "😐" -> binding.moodNeutral.alpha = 1.0f
            "😍" -> binding.moodLove.alpha = 1.0f
        }
    }
    
    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            saveDiary()
        }
    }
    
    private fun loadEntry(entryId: Long) {
        val prefs = requireContext().getSharedPreferences("diary_prefs", android.content.Context.MODE_PRIVATE)
        val entriesJson = prefs.getString("diary_entries", null) ?: return
        
        try {
            val type = object : TypeToken<List<DiaryEntry>>() {}.type
            val entries: List<DiaryEntry> = gson.fromJson(entriesJson, type)
            editingEntry = entries.find { it.id == entryId }
            
            editingEntry?.let { entry ->
                binding.edittextTitle.setText(entry.title)
                binding.edittextContent.setText(entry.content)
                selectedMood = entry.mood
                updateMoodSelection()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun saveDiary() {
        val title = binding.edittextTitle.text.toString().trim()
        val content = binding.edittextContent.text.toString().trim()
        
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        
        val prefs = requireContext().getSharedPreferences("diary_prefs", android.content.Context.MODE_PRIVATE)
        val entriesJson = prefs.getString("diary_entries", null)
        
        val entries = if (entriesJson != null) {
            try {
                val type = object : TypeToken<MutableList<DiaryEntry>>() {}.type
                gson.fromJson<MutableList<DiaryEntry>>(entriesJson, type)
            } catch (e: Exception) {
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
        
        if (editingEntry != null) {
            val index = entries.indexOfFirst { it.id == editingEntry!!.id }
            if (index != -1) {
                entries[index] = editingEntry!!.copy(
                    title = title,
                    content = content,
                    mood = selectedMood,
                    date = Date()
                )
            }
        } else {
            val newEntry = DiaryEntry(
                id = DiaryManager.generateId(),
                title = title,
                content = content,
                mood = selectedMood,
                date = Date()
            )
            entries.add(0, newEntry)
        }

        val str = gson.toJson(entries)

        prefs.edit().putString("diary_entries", str).apply()

        Log.d("MyDiary", str)

        Toast.makeText(requireContext(), "일기가 저장되었습니다", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}