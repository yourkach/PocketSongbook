package com.example.pocketsongbook.feature.guitar_tuner.permissions_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.databinding.FragmentMicroPermissionsBinding
import com.example.pocketsongbook.feature.guitar_tuner.tuner_screen.TunerFragment
import timber.log.Timber

class MicroPermissionsFragment : BaseFragment(R.layout.fragment_micro_permissions) {

    private val binding by viewBinding(FragmentMicroPermissionsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivMicrophoneIcon.setOnClickListener {
            requestMicrophonePermission()
        }
        requestMicrophonePermission()
    }

    // TODO: 28.02.2021 подумать над корректностью запроса разрешений
    private fun requestMicrophonePermission() {
        val rationale = shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)
        Timber.d("requestMicrophonePermission() should show rationale: $rationale")
        requestPermissions(
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_AUDIO_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            when (grantResults.firstOrNull()) {
                PackageManager.PERMISSION_GRANTED -> onMicroPermissionGranted()
                PackageManager.PERMISSION_DENIED -> onMicroPermissionDenied()
            }
        }
    }

    private fun onMicroPermissionGranted() {
        router.replaceScreen(TunerFragment().toScreen())
    }

    private fun onMicroPermissionDenied() {
        val message = getString(R.string.permission_was_denied)
        val actionText = getString(R.string.settings)
        showActionSnackBar(message, actionText, binding.coordinatorLayout) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }
    }


    companion object {
        private const val RECORD_AUDIO_REQUEST_CODE = 201
    }

}