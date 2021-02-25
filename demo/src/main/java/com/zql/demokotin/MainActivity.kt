package com.zql.demokotin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zql.filepickerlib.FilePickerActivity2
import com.zql.filepickerlib.FilePickerConstants

class MainActivity : AppCompatActivity() {
    val CODE_GOT_STORAGE_FILES = 1
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.showselected)

    }

    fun openFilePickerButton(view: View) {
        val intent=Intent(applicationContext, FilePickerActivity2::class.java)
        startActivityForResult(intent,CODE_GOT_STORAGE_FILES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_GOT_STORAGE_FILES && resultCode == RESULT_OK && data != null) {
            val result: List<String> = data.getStringArrayListExtra(FilePickerConstants.EXTRA_FILE_PATH_LIST)
            textView?.setText(listOf(result).toString())
        }
    }
}