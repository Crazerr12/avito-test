package ru.crazerr.avitotest

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.crazerr.avitotest.presentation.AvitoTestApp
import ru.crazerr.avitotest.utils.theme.AvitoTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            handlePermissionResult()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()
        setContent {
            AvitoTestTheme {
                AvitoTestApp()
            }
        }
    }

    private fun checkPermissions() {
        val requiredPermission = getRequiredPermission()
        when {
            ContextCompat.checkSelfPermission(
                this,
                requiredPermission
            ) == PackageManager.PERMISSION_DENIED -> {
                permissionLauncher.launch(requiredPermission)
            }
        }
    }

    private fun getRequiredPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

    private fun handlePermissionResult() {
        Toast.makeText(this, this.getString(R.string.read_media_audio_required), Toast.LENGTH_LONG)
            .show()
    }
}
