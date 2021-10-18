package com.fakhrulasa.scoper

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import java.io.File

fun decodeResource(
    res: Resources,
    resId: Int,
    dstWidth: Int,
    dstHeight: Int,
    scalingLogic: ScalingLogic
): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)
    options.inJustDecodeBounds = false
    options.inSampleSize = calculateSampleSize(
        options.outWidth, options.outHeight, dstWidth,
        dstHeight, scalingLogic
    )

    return BitmapFactory.decodeResource(res, resId, options)
}

fun decodeFile(
    context: Context,
    uri: Uri,
    dstWidth: Int,
    dstHeight: Int,
    scalingLogic: ScalingLogic
): Bitmap? {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    context.getBitmapFromUri(uri, options)
    options.inJustDecodeBounds = false

    options.inSampleSize = calculateSampleSize(
        options.outWidth,
        options.outHeight,
        dstWidth,
        dstHeight,
        scalingLogic
    )

    return context.getBitmapFromUri(uri, options)
}


fun createScaledBitmap(
    unscaledBitmap: Bitmap, dstWidth: Int, dstHeight: Int,
    scalingLogic: ScalingLogic
): Bitmap {
    val srcRect = calculateSrcRect(
        unscaledBitmap.width, unscaledBitmap.height,
        dstWidth, dstHeight, scalingLogic
    )
    val dstRect = calculateDstRect(
        unscaledBitmap.width,
        unscaledBitmap.height,
        dstWidth,
        dstHeight,
        scalingLogic
    )
    val scaledBitmap =
        Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(scaledBitmap)
    canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))

    return scaledBitmap
}


enum class ScalingLogic {
    CROP, FIT
}


fun calculateSampleSize(
    srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
    scalingLogic: ScalingLogic
): Int {
    if (scalingLogic == ScalingLogic.FIT) {
        val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
        val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

        return if (srcAspect > dstAspect) {
            srcWidth / dstWidth
        } else {
            srcHeight / dstHeight
        }
    } else {
        val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
        val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

        return if (srcAspect > dstAspect) {
            srcHeight / dstHeight
        } else {
            srcWidth / dstWidth
        }
    }
}

fun calculateSrcRect(
    srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
    scalingLogic: ScalingLogic
): Rect {
    if (scalingLogic == ScalingLogic.CROP) {
        val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
        val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

        return if (srcAspect > dstAspect) {
            val srcRectWidth = (srcHeight * dstAspect).toInt()
            val srcRectLeft = (srcWidth - srcRectWidth) / 2
            Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight)
        } else {
            val srcRectHeight = (srcWidth / dstAspect).toInt()
            val scrRectTop = (srcHeight - srcRectHeight) / 2
            Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight)
        }
    } else {
        return Rect(0, 0, srcWidth, srcHeight)
    }
}


fun calculateDstRect(
    srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
    scalingLogic: ScalingLogic
): Rect {
    return if (scalingLogic == ScalingLogic.FIT) {
        val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
        val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

        if (srcAspect > dstAspect) {
            Rect(0, 0, dstWidth, (dstWidth / srcAspect).toInt())
        } else {
            Rect(0, 0, (dstHeight * srcAspect).toInt(), dstHeight)
        }
    } else {
        Rect(0, 0, dstWidth, dstHeight)
    }
}

fun getImageQualityPercent(file: File): Int {
    val sizeInBytes = file.length()
    val sizeInKB = sizeInBytes / 1024
    val sizeInMB = sizeInKB / 1024

    return when {
        sizeInMB <= 1 -> 80
        sizeInMB <= 2 -> 60
        else -> 40
    }
}

fun Context.getImageHgtWdt(uri: Uri): Pair<Int, Int> {
    val opt = BitmapFactory.Options()

    /* by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded.
    If you try the use the bitmap here, you will get null.*/
    opt.inJustDecodeBounds = true
    val bm = getBitmapFromUri(uri, opt)

    var actualHgt = (opt.outHeight).toFloat()
    var actualWdt = (opt.outWidth).toFloat()

    /*val maxHeight = 816.0f
    val maxWidth = 612.0f*/
    val maxHeight = 720f
    val maxWidth = 1280f
    var imgRatio = actualWdt / actualHgt
    val maxRatio = maxWidth / maxHeight

//    width and height values are set maintaining the aspect ratio of the image
    if (actualHgt > maxHeight || actualWdt > maxWidth) {
        when {
            imgRatio < maxRatio -> {
                imgRatio = maxHeight / actualHgt
                actualWdt = (imgRatio * actualWdt)
                actualHgt = maxHeight
            }
            imgRatio > maxRatio -> {
                imgRatio = maxWidth / actualWdt
                actualHgt = (imgRatio * actualHgt)
                actualWdt = maxWidth
            }
            else -> {
                actualHgt = maxHeight
                actualWdt = maxWidth
            }
        }
    }

    return Pair(actualHgt.toInt(), actualWdt.toInt())
}