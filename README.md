# Scoper - Access any file with zero permission required
![Fakhruls's GitHub stats](https://img.shields.io/static/v1?label=&message=Android11&color=green)![Fakhruls's GitHub stats](https://img.shields.io/static/v1?label=&message=READY&color=orange)

<h3>With this library you will able to access file from your mobile without requiring any permissions including READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE which are restricted in Android 11 and beyond for only File managing related applications.</h3>

To start with, 
Copy or clone this repository  **1.0.0 release branch** into your project,
Then just call openFileIntent() for accessing documents and manage your documents, files in startActivityForResult or use ActivityResultContracts.StartActivityForResult()
##
```kotlin
function openGallery() {
    intentDocument.launch(openFileIntent(null))
}
```
To store the file into your app cache and use it later anywhere,
```
var file:File? =  FileUtil.from(this,result.data?.data!!)
```
Then manage it according to your need,
```kotlin
    private var intentDocument =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                var file:File? =  FileUtil.from(this,result.data?.data!!)
	
                    if (result.resultCode == Activity.RESULT_OK) {
                       /**
                       * Your code here for managing the file
                       */
                }
            }
        }
```
# Request specefic filetype
You can request specefic file type according to your preference,

```kotlin
    private fun getDocument() {
        intentDocument.launch(
            openFileIntent(
                listOf(
                    FileType.PDF,
                    FileType.IMAGE
                )
            )
        )
    }
```
Then manage it according to your need,
```kotlin
    private var intentDocument =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                var file:File? =  FileUtil.from(this,result.data?.data!!)
                if(file?.extension.equals("pdf",ignoreCase = true)){
                    if (result.resultCode == Activity.RESULT_OK) {
                       /**
                       * Your code here for file type
                       */
                    }
                }else{
                     /**
                       * Your code here for image type
                       */
                }

            }
        }
```
#File operation's
To get the cached file name,
```kotlin
file.name
```
To get the cached file path,
```kotlin
file.path
```
To get the cached file absolute path,
```kotlin
file.absolutePath
```
To get the cached file Uri,
```kotlin
file.toURI()
```
To get the cached file size in KB,
```kotlin
file?.length()!! / 1024
```

# License
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
```
Copyright (C) 2021 Fakhrul Siddiqei

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
