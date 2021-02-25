# lzqFilePicker

lzqFilePicker提供通过存储目录选择文件功能，目前支持了手机存储和SD卡
项目中使用了androidx,mvrx,epoxy
## Features
- 提供Activity形式选择文件

<img src="https://github.com/lzqnet/lzqfilepicker/blob/master/demo_image/filepicker1.png" alt="filepicker" width="50%" height="50%"  />
<img src="https://github.com/lzqnet/lzqfilepicker/blob/master/demo_image/filepicker2-140636.png" alt="filepicker" width="50%" height="50%"  />

## Usage
### 依赖
```Gradle
repositories {
        jcenter()
         google()
// add maven
        maven{
            url 'https://dl.bintray.com/lzqweb/lzqfilepicker'
        }
       

    }

dependencies {
       implementation 'com.zql.filepickerlib:filepickerlib:1.0.5'
}
```

### 接入方式
1，调起文件选择页面
 ```val intent=Intent(applicationContext, FilePickerActivity2::class.java)
        startActivityForResult(intent,CODE_GOT_STORAGE_FILES)
 ```

2，结果通过result的形式返回结调用方
``` 
class MainActivity : AppCompatActivity() {
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
               super.onActivityResult(requestCode, resultCode, data)
               if (requestCode == CODE_GOT_STORAGE_FILES && resultCode == RESULT_OK && data != null) {
                val result: List<String> = data.getStringArrayListExtra(FilePickerConstants.EXTRA_FILE_PATH_LIST)
               }
       }
    }
```
    val result: List<String> 即为选择结果


# lzqFilePicker

lzqFilePicker is a file picker sdk for android,the sdk support internal storage and sd card.
the project using androidx,mvrx,epoxy 3rd Library


## Features


<img src="https://github.com/lzqnet/lzqfilepicker/blob/master/demo_image/filepicker1.png" alt="filepicker" width="50%" height="50%"  />
<img src="https://github.com/lzqnet/lzqfilepicker/blob/master/demo_image/filepicker2-140636.png" alt="filepicker" width="50%" height="50%"  />

## Basic Usage
### Add dependency
```Gradle
repositories {
        jcenter()
         google()
// add maven
        maven{
            url 'https://dl.bintray.com/lzqweb/lzqfilepicker'
        }
       

    }
        
dependencies {
       implementation 'com.zql.filepickerlib:filepickerlib:1.0.5'
}
```

### Simple use cases 
1，launch picker page
 ```val intent=Intent(applicationContext, FilePickerActivity2::class.java)
        startActivityForResult(intent,CODE_GOT_STORAGE_FILES)
 ```

2，get result from activity callback
```
class MainActivity : AppCompatActivity() {
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
               super.onActivityResult(requestCode, resultCode, data)
               if (requestCode == CODE_GOT_STORAGE_FILES && resultCode == RESULT_OK && data != null) {
                val result: List<String> = data.getStringArrayListExtra(FilePickerConstants.EXTRA_FILE_PATH_LIST)
               }
       }
    }
```
    val result: List<String> is the result
    
    Please feel free to contact me if you have any questions: lzqnet@163.com

### License


```
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
