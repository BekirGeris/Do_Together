package com.example.dotogether.view.callback

import androidx.viewbinding.ViewBinding

interface HolderCallback {

    fun holderListener(binding: ViewBinding, methodType: Int, position: Int)
}