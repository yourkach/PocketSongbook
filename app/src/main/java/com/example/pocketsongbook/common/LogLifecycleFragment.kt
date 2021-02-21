package com.example.pocketsongbook.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import moxy.MvpAppCompatFragment

abstract class LogLifecycleFragment(layoutId: Int) : MvpAppCompatFragment(layoutId) {

    private val logTag = this::class.java.simpleName + hashCode() + ":Lifecycle"

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(logTag, "onViewStateRestored, $savedInstanceState")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        Log.d(logTag, "onAttach")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d(logTag, "onDetach")
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "onCreate, savedInstanceState: $savedInstanceState")
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy")
        super.onDestroy()
    }

    override fun onResume() {
        Log.d(logTag, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(logTag, "onPause")
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(logTag, "onViewCreated, savedInstanceState: $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(logTag, "onDestroyView")
    }
}