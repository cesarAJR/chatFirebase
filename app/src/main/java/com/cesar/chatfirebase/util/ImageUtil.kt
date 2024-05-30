package com.cesar.chatfirebase.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun getImageUri(bitmap: Bitmap, context: Context): Uri? {
    val byteArrayOuput = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOuput)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Image", "")
    return Uri.parse(path)
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

fun getFileOptions(context:Context): ImageCapture.OutputFileOptions {
    val FILENAME_FORMAT ="dd/MM/yyyy_HH:mm:ss"
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()
    return outputOptions
}

fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}