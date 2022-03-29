package com.fakhrulasa.scoper.lib

import android.content.Intent

object Scoper {

    fun getPickImageIntent(listOfType: List<String>?): Intent? {
        /**
         * For filepicker, where you will able to pick files
         */
        val pickIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            if (listOfType.isNullOrEmpty()) {
                val mimetypes = ("*/*")
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            } else {
                val mimetypes = listOfType.toTypedArray()
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            }

        }

        return pickIntent
    }

}