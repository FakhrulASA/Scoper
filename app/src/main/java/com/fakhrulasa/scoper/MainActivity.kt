package com.fakhrulasa.scoper

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fakhrulasa.scoper.lib.FileType
import com.fakhrulasa.scoper.lib.Scoper.getPickImageIntent
import com.fakhrulasa.scoper.lib.FileUtil
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
        intentDocument.launch(getPickImageIntent(listOf(FileType.PDF, FileType.IMAGE)))
    }

    @SuppressLint("WrongConstant")
    private var intentDocument =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                var file: File? = FileUtil.from(this, result.data?.data!!)
                if (file?.extension.equals("pdf", ignoreCase = true)) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        textName.text = file?.name
                        textType.text = file?.extension
                        val file_size = java.lang.String.valueOf(file?.length()!! / 1024).toInt()
                        Toast.makeText(this, file_size.toString(), Toast.LENGTH_SHORT).show()

                    }
                } else {
                    textName.text = file?.name
                    textType.text = file?.extension
                    val file_size = java.lang.String.valueOf(file?.length()!! / 1024).toInt()
                    Toast.makeText(
                        this,
                        file_size.toString() + "\n" + file.path,
                        Toast.LENGTH_SHORT
                    ).show()


                }
            }

        }
}
