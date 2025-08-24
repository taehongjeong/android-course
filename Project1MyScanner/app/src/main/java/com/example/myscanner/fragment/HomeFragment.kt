package com.example.myscanner.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myscanner.R
import com.example.myscanner.adapter.PhotoAdapter
import com.example.myscanner.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var photoAdapter: PhotoAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        loadPhotos()
    }
    
    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter { photo ->
            showPhotoOptions(photo)
        }
        
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = photoAdapter
        }
    }
    
    private fun loadPhotos() {
        val photosDir = File(requireContext().filesDir, "photos")
        if (photosDir.exists() && photosDir.isDirectory) {
            val photos = photosDir.listFiles()
                ?.filter { it.extension == "jpg" }
                ?.sortedByDescending { it.lastModified() }
            
            if (!photos.isNullOrEmpty()) {
                photoAdapter.setPhotos(photos)
                binding.tvPhotoCount.text = getString(R.string.scan_count, photos.size)
                binding.recyclerView.isVisible = true
                binding.tvEmptyState.isVisible = false
            } else {
                showEmptyState()
            }
        } else {
            showEmptyState()
        }
    }
    
    private fun showEmptyState() {
        binding.recyclerView.isVisible = false
        binding.tvEmptyState.isVisible = true
        binding.tvPhotoCount.text = getString(R.string.scan_count, 0)
    }
    
    private fun showPhotoOptions(photo: File) {
        val options = arrayOf("열기", "공유", "삭제")
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(photo.nameWithoutExtension)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openPhoto(photo)
                    1 -> sharePhoto(photo)
                    2 -> confirmDelete(photo)
                }
            }
            .show()
    }
    
    private fun openPhoto(photo: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photo
        )
        
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/jpeg")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
    
    private fun sharePhoto(photo: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photo
        )
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        startActivity(Intent.createChooser(intent, "공유하기"))
    }
    
    private fun confirmDelete(photo: File) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("이 사진을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                deletePhoto(photo)
            }
            .setNegativeButton("취소", null)
            .show()
    }
    
    private fun deletePhoto(photo: File) {
        if (photo.delete()) {
            loadPhotos() // 목록 새로고침
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadPhotos() // 카메라에서 돌아왔을 때 목록 새로고침
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}