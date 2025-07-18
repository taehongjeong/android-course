package com.example.week1mydiary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.week1mydiary.databinding.ItemDiaryEntryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DiaryAdapter(
    private val onItemClick: (DiaryEntry) -> Unit
) : ListAdapter<DiaryEntry, DiaryAdapter.DiaryViewHolder>(DiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = ItemDiaryEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DiaryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiaryViewHolder(
        private val binding: ItemDiaryEntryBinding,
        private val onItemClick: (DiaryEntry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: DiaryEntry) {
            binding.textviewMood.text = entry.mood
            binding.textviewTitle.text = entry.title
            binding.textviewContent.text = entry.content
            
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
            binding.textviewDate.text = dateFormat.format(entry.date)
            
            binding.root.setOnClickListener {
                onItemClick(entry)
            }
        }
    }

    class DiaryDiffCallback : DiffUtil.ItemCallback<DiaryEntry>() {
        override fun areItemsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
            return oldItem == newItem
        }
    }
}