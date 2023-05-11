package com.example.smartlamp.model

data class ModeModel(
    val brightness: Float,
    val image: Int,
    val mode: String,
    val flicker: Int,
    var modeOn: Boolean,
)