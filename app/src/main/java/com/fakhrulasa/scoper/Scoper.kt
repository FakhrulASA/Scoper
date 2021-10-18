package com.fakhrulasa.scoper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.util.ArrayList

object Scoper:AppCompatActivity() {

     fun getPickImageIntent(context: Context,listOfType:List<String>?): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            if(listOfType.isNullOrEmpty()){
                val mimetypes = ("*/*")
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            }else{
                val mimetypes = listOfType.toTypedArray()
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            }

        }

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(context))

        intentList = addIntentsToList(context, intentList, pickIntent)
        intentList = addIntentsToList(context, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                "Capture Image"
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }
     fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }
     fun setImageUri(context: Context): Uri {
        val folder = File("${context.getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()

        val file = File(folder, "Image_Tmp.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        var imageUri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + context.getString(R.string.file_provider_name),
            file
        )
        var imgPath = file.absolutePath
        return imageUri!!
    }

}