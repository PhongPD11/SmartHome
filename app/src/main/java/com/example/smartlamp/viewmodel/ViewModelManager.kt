package com.example.smartlamp.viewmodel

object ViewModelManager {
    private var viewModel: HomeViewModel? = null

    fun initializeViewModel(viewModel: HomeViewModel) {
        this.viewModel = viewModel
    }

    fun getViewModel(): HomeViewModel? {
        return viewModel
    }
}
