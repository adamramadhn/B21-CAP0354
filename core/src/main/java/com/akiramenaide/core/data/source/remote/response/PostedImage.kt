package com.akiramenaide.core.data.source.remote.response

data class PostedImage(
    val predClass: Int,
    val className: String,
    val percentage: Float,
    val prediction: List<List<Double>>,
    val data: String
)

data class GetIndex(
    val status: Int,
    val data: String
)