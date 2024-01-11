package com.example.dogtypes.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.api.ApiResponseStatus
import com.example.dogtypes.api.ApiServiceInterceptor
import com.example.dogtypes.auth.LoginActivity
import com.example.dogtypes.databinding.ActivityMainBinding
import com.example.dogtypes.dogdetail.DogDetailComposeActivity
import com.example.dogtypes.doglist.DogListActivity
import com.example.dogtypes.domain.Dog
import com.example.dogtypes.domain.User
import com.example.dogtypes.machinelearning.DogRecognition
import com.example.dogtypes.settings.SettingsActivity
import com.example.dogtypes.testutils.EspressoIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalMaterialApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setupCamera()
            } else {
               Toast.makeText(this, "Necesitas aceptar los permisos para poder utilizar la camara",
                   Toast.LENGTH_LONG).show()
            }
        }

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)
        if (user == null){
            openLoginActivity()
            return
        }else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }

        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }

        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }

       /* binding.takePhotoFab.setOnClickListener {
            if (isCameraReady){
                takePhoto()
            }

        }*/

        viewModel.status.observe(this) {
                status ->

            when (status) {
                is ApiResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()
                }

                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> binding.loadingWheel.visibility = View.GONE


            }
        }

        viewModel.dog.observe(this) {
           dog ->

           if (dog != null) {
                openDogDetailActivity(dog)
           }
        }

        viewModel.dogRecognition.observe(this){
            enableTakePhotoButton(it)
        }

        requestCameraPermission()
    }


    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        intent.putExtra(DogDetailComposeActivity.MOST_PROBABLE_DOGS_IDS, ArrayList<String>(viewModel.probableDogIds))
        intent.putExtra(DogDetailComposeActivity.IS_RECOGNITION_KEY, true)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized){
            cameraExecutor.shutdown()
        }
    }

    private fun setupCamera() {
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }

    }

    private fun requestCameraPermission() {
           when {
               ContextCompat.checkSelfPermission(
                   this,
                   android.Manifest.permission.CAMERA
               ) == PackageManager.PERMISSION_GRANTED -> {
                   setupCamera()
               }

               shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                   // In an educational UI, explain to the user why your app requires this
                   // permission for a specific feature to behave as expected, and what
                   // features are disabled if it's declined. In this UI, include a
                   // "cancel" or "no thanks" button that lets the user continue
                   // using your app without granting the permission.
                   AlertDialog.Builder(this)
                       .setTitle("Aceptame por favor")
                       .setMessage("Acepta la camara o no podras utilizar la camara")
                       .setPositiveButton(android.R.string.ok){ _, _ ->
                           requestPermissionLauncher.launch(
                               android.Manifest.permission.CAMERA
                           )
                       }
                       .setNegativeButton(android.R.string.cancel){ _, _ ->
                            }.show()

               }

               else -> {
                   requestPermissionLauncher.launch(
                       android.Manifest.permission.CAMERA
                   )
               }
           }
    }

    /*private fun takePhoto(){
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException)
                {
                    Toast.makeText(this@MainActivity,
                        "Error al tomar la foto ${error.message}",
                        Toast.LENGTH_SHORT).show()
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                }
            })
    }

    private fun getOutputPhotoFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }
        return if ( mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            filesDir
        }
    }*/

    private fun startCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(this)

        EspressoIdlingResource.increment()
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            //Preview
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                EspressoIdlingResource.decrement()
                viewModel.recognizeImage(imageProxy)

            }

            cameraProvider.bindToLifecycle(
                this,cameraSelector,
                preview, imageCapture, imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun enableTakePhotoButton(dogRecognition: DogRecognition) {
        if (dogRecognition.confidence > 70.0) {
            binding.takePhotoFab.alpha = 1f
            binding.takePhotoFab.setOnClickListener {
                viewModel.getDogByMlId(dogRecognition.id)
            }
        }else{
            binding.takePhotoFab.alpha = 0.2f
            binding.takePhotoFab.setOnClickListener(null)
        }
    }


    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}