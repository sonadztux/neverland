package com.neverland.capstone.viewmodel

import androidx.lifecycle.ViewModel
import com.neverland.capstone.data.CapstoneRepository
import java.io.File

class AnalyzeViewModel(private val repository: CapstoneRepository) : ViewModel() {

    fun uploadImage(file: File) = repository.uploadImage(file)
}