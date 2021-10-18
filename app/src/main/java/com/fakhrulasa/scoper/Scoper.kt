package com.fakhrulasa.scoper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

object Scoper {

     fun getPickImageIntent(listOfType:List<String>?): Intent? {
         /**
          * For filepicker, where you will able to pick files
          */
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

        return pickIntent
    }

}