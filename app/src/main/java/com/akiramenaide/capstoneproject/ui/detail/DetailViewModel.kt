package com.akiramenaide.capstoneproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.akiramenaide.core.domain.usecase.FruitUseCase

class DetailViewModel(fruitUseCase: FruitUseCase): ViewModel() {
    val fruitList = fruitUseCase.getAllFruits().asLiveData()
}