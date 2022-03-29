package com.fakhrulasa.scoper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fakhrulasa.scoper.Scoper.getPickImageIntent
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var textName: TextView
    private lateinit var textType: TextView
    private lateinit var imageView: ImageView
    private lateinit var buttonAdd: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        textType = findViewById(R.id.fileType)
        textName = findViewById(R.id.fileType2)
        imageView = findViewById(R.id.imageView2)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            chooseImage()
            imageView.visibility = View.GONE

        }
        val builder = VmPolicy.Builder()

        StrictMode.setVmPolicy(builder.build())
    }


    private fun chooseImage() {
        intentDocument.launch(getPickImageIntent(listOf("image/*", "application/pdf/*")))
    }

    @SuppressLint("WrongConstant")
    private var intentDocument =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                var file: File? = FileUtil.from(this, result.data?.data!!)
                if (file?.extension.equals("pdf", ignoreCase = true)) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        textName.text = file?.absolutePath
                        textType.text = file?.extension
                        val file_size = java.lang.String.valueOf(file?.length()!! / 1024).toInt()
                        Toast.makeText(this, file_size.toString(), Toast.LENGTH_SHORT).show()


                        if (file.exists()) {
                            val target = Intent(Intent.ACTION_VIEW)
                            target.setDataAndType(Uri.fromFile(file), "application/pdf")
                            target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            val intent = Intent.createChooser(target, "Open File")
                            try {
                                startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                        }

                    }
                } else {
                    textName.text = file?.absolutePath
                    textType.text = file?.extension
                    val file_size = java.lang.String.valueOf(file?.length()!! / 1024).toInt()
                    Toast.makeText(
                        this,
                        file_size.toString() + "\n" + file.path,
                        Toast.LENGTH_SHORT
                    ).show()
                    imageView.visibility = View.VISIBLE
                    if (file.exists()) {
                        val target = Intent(Intent.ACTION_VIEW)
                        target.setDataAndType(Uri.fromFile(file), "image/*")
                        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        val intent = Intent.createChooser(target, "Open File")
                        try {
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            // Instruct the user to install a PDF reader here, or something
                        }
                    }

                }
            }

        }
}
