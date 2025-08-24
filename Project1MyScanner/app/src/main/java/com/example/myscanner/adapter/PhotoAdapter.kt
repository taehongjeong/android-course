package com.example.myscanner.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myscanner.databinding.ItemPhotoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoAdapter(
    private val onItemClick: (File) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    
    private val photos = mutableListOf<File>()
    private val dateFormat = SimpleDateFormat("MM/dd HH:mm", Locale.KOREA)
    
    @SuppressLint("NotifyDataSetChanged")
    fun setPhotos(newPhotos: List<File>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }
    
    override fun getItemCount() = photos.size
    
    inner class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(photos[position])
                }
            }
        }
        
        fun bind(photo: File) {
            // Glide를 사용한 이미지 로딩
            Glide.with(binding.ivPhoto.context)
                .load(photo)
                .centerCrop()
                .into(binding.ivPhoto)
            
            // 파일명과 날짜 설정
            binding.tvFileName.text = photo.nameWithoutExtension
            binding.tvDate.text = dateFormat.format(Date(photo.lastModified()))
        }
    }
}