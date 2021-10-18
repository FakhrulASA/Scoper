# Scoper - Access any file with zero permission required
![Fakhruls's GitHub stats](https://img.shields.io/static/v1?label=&message=Android11&color=green)![Fakhruls's GitHub stats](https://img.shields.io/static/v1?label=&message=READY&color=orange)

<h3>With this codebase you will able to access file from your mobile without requiring permission or READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE which is restricted in Android 11 and beyond for only File managing related applications.</h3>

To start with, 
Copy or clone this repository  **1.0.0 release branch** into your project,
Then just startActivityForResult or use ActivityResultContracts.StartActivityForResult()
##
```kotlin
function openGallery() {
    intentDocument.launch(getPickImageIntent(null))
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
# Request specefic filetype
You can request specefic file type according to your preference,

```kotlin
function openGallery() {
    intentDocument.launch(
                Scoper.getPickImageIntent(
                    listOf(
                        FileType.PDF,
                        FileType.IMAGE
                    )
                )
            )
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
