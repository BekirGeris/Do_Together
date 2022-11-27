package com.example.dotogether.view.callback

import androidx.viewbinding.ViewBinding
import com.example.dotogether.util.Constants.MethodType

interface HolderCallback {

    fun holderListener(binding: ViewBinding, methodType: MethodType, position: Int)
}