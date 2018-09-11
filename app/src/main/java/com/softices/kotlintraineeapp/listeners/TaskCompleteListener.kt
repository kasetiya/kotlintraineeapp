package com.softices.kotlintraineeapp.listeners

interface TaskCompleteListener {
    fun onTaskCompleted(response: String, serviceCode: Int)
}