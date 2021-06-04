package com.neverland.capstone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neverland.capstone.data.CapstoneRepository

class ViewModelFactory constructor(val repository: CapstoneRepository) :

    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AnalyzeViewModel::class.java) -> {
                AnalyzeViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}