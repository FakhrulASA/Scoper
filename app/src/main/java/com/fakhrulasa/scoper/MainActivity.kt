package com.fakhrulasa.scoper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fakhrulasa.scoper.Scoper.getPickImageIntent
import kotlinx.coroutines.*
import java.io.File
import java.util.ArrayList

private const val REQ_CAPTURE = 100
private const val RES_IMAGE = 100

class MainActivity : ParentActivity(R.layout.activity_main) {
    private var queryImageUrl: String = ""
    private val tag = javaClass.simpleName
    private var imgPath: String = ""
    private var imageUri: Uri? = null
    private val permissions = arrayOf(Manifest.permission.CAMERA)

    private lateinit var progressBar:ProgressBar
    private lateinit var textName:TextView
    private lateinit var textType:TextView
    private lateinit var imageView:ImageView
    private lateinit var buttonAdd:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBar=findViewById(R.id.progressBar)
        textType=findViewById(R.id.fileType)
        textName=findViewById(R.id.fileType2)
        imageView=findViewById(R.id.imageView2)
        buttonAdd=findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            chooseImage()
            imageView.visibility=View.GONE

        }
    }


    private fun chooseImage() {
        intentDocument.launch(getPickImageIntent(this, listOf("image/*","application/pdf/*")))
    }

    private var intentDocument =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                var file:File? =  FileUtil.from(this,result.data?.data!!)
                if(file?.extension.equals("pdf",ignoreCase = true)){
                    if (result.resultCode == Activity.RESULT_OK) {
                        textName.text=file?.name
                        textType.text=file?.extension
                    }
                }else{
                    textName.text=file?.name
                    textType.text=file?.extension
                }

            }
        }

}