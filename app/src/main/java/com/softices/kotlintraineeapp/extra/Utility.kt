package com.softices.kotlintraineeapp.extra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

object Utility {
    // convert from bitmap to byte array
    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    // convert from byte array to bitmap
    fun getPhoto(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun getPhotosUri(context: Context, ImageFilePath: String?): Uri? {
        var photoBitmap: Bitmap?
        var rotationAngle = 0
        if (ImageFilePath != null && ImageFilePath.length > 0) {

            try {
                val mobile_width = 480
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(ImageFilePath, options)
                val outWidth = options.outWidth
                var ratio = (outWidth.toFloat() / mobile_width + 0.5f).toInt()

                if (ratio == 0) {
                    ratio = 1
                }
                val exif = ExifInterface(ImageFilePath)

                val orientString = exif
                        .getAttribute(ExifInterface.TAG_ORIENTATION)
                val orientation = if (orientString != null)
                    Integer
                            .parseInt(orientString)
                else
                    ExifInterface.ORIENTATION_NORMAL
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotationAngle = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotationAngle = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotationAngle = 270
                    ExifInterface.ORIENTATION_NORMAL -> rotationAngle = 0
                    else -> {
                    }
                }// do with default

                options.inJustDecodeBounds = false
                options.inSampleSize = ratio

                photoBitmap = BitmapFactory.decodeFile(ImageFilePath, options)
                if (photoBitmap != null) {
                    val matrix = Matrix()
                    matrix.setRotate(rotationAngle.toFloat(),
                            photoBitmap.width.toFloat() / 2,
                            photoBitmap.height.toFloat() / 2)
                    photoBitmap = Bitmap.createBitmap(photoBitmap, 0, 0,
                            photoBitmap.width,
                            photoBitmap.height, matrix, true)

                    val path = MediaStore.Images.Media.insertImage(
                            context.contentResolver, photoBitmap,
                            Calendar.getInstance().timeInMillis.toString() + ".jpg", null)

                    return Uri.parse(path)
                }
            } catch (e: OutOfMemoryError) {
            } catch (e: IOException) {
            }

        } else {
            Toast.makeText(
                    context, "Error In Image Capture", Toast.LENGTH_LONG).show()
        }
        return null
    }
}
