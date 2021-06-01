package com.akiramenaide.capstoneproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.akiramenaide.core.domain.model.Fruit
import com.akiramenaide.core.domain.usecase.FruitUseCase

class HomeViewModel(private val fruitUseCase: FruitUseCase): ViewModel() {
    fun getAllFruits() = fruitUseCase.getAllFruits().asLiveData()

    fun insertFruit(fruit: Fruit) = fruitUseCase.insertFruit(fruit)

    fun updateFruitInfo(fruit: Fruit) = fruitUseCase.updateFruitInfo(fruit)

    fun getPredict(predict: String) = fruitUseCase.getPredict(predict).asLiveData()
}