package com.cesar.chatfirebase.ui.camera

import android.net.Uri
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.util.getCameraProvider
import com.cesar.chatfirebase.util.getFileOptions

@Composable
fun CameraScreen(onEditUser:(String)->Unit) {

    var lensFacing by remember {
        mutableIntStateOf(CameraSelector.LENS_FACING_BACK)
    }

    var takePhoto by remember {
        mutableStateOf(false)
    }

    var photoUri by remember {
        mutableStateOf<Uri>(Uri.parse(""))
    }

    var showImage by remember {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    LaunchedEffect(takePhoto) {
        if (takePhoto){
            val outputOptions = getFileOptions(context)
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        outputFileResults.savedUri?.let {
                            showImage = true
                            photoUri = it
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(context, "Ocurrio un error.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (showImage){
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(photoUri)
                            .build()
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription ="")
                ConstraintLayout(
                    Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .background(Color(android.graphics.Color.parseColor("#79000000")))
                        .padding(horizontal = 15.dp)
                ) {
                    val (ivCheck,ivCancel) = createRefs()
                    Image(
                        modifier = Modifier
                            .constrainAs(ivCancel) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .width(80.dp)
                            .height(80.dp)
                            .clickable {
                                takePhoto = false
                                showImage = false
                            },
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription ="cancel"
                    )
                    Image(
                        modifier = Modifier
                            .constrainAs(ivCheck) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .width(80.dp)
                            .height(80.dp)
                            .clickable {
                                       onEditUser(photoUri.toString())
                            },
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription ="check"
                    )
                }
            }else{
                AndroidView(
                    { previewView },
                    modifier = Modifier.fillMaxSize()
                )
                ConstraintLayout(
                    Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .background(Color(android.graphics.Color.parseColor("#79000000")))
                        .padding(horizontal = 15.dp)
                ) {
                    val (ivTakePhoto,ivChangeCamera) = createRefs()
                    Image(
                        modifier = Modifier
                            .constrainAs(ivChangeCamera) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .width(80.dp)
                            .height(80.dp)
                            .clickable {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                                    CameraSelector.LENS_FACING_FRONT
                                else CameraSelector.LENS_FACING_BACK
                            },
                        painter = painterResource(id = R.drawable.ic_flip_camera_android),
                        contentDescription ="change camera"
                    )
                    Image(
                        modifier = Modifier
                            .constrainAs(ivTakePhoto) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .width(80.dp)
                            .height(80.dp)
                            .clickable {
                                takePhoto = true
                            },
                        painter = painterResource(id = R.drawable.ic_camera_24_white),
                        contentDescription ="change camera"
                    )
                }
            }
        }
}
