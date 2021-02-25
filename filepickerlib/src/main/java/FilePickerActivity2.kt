package com.zql.filepickerlib

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.viewModel
import com.zql.filepickerlib.pick.PickerFragment
import com.zql.filepickerlib.picker.CurrentFolderState
import com.zql.filepickerlib.picker.CurrentFolderViewModel
import com.zql.filepickerlib.mimeType.MimeTypeHelper
import com.zql.filepickerlib.storagelist.StorageListFragment
import com.zql.filepickerlib.storagelist.StorageState
import com.zql.filepickerlib.storagelist.StorageViewModel
import java.io.File

class FilePickerActivity2 : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMS = 1

    }

    private val storageViewModel: StorageViewModel by viewModel(StorageViewModel::class)
    private val currentFolderViewModel by viewModel(CurrentFolderViewModel::class)

    private lateinit var mButtonBack: ImageView
    private lateinit var mButtonCancel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drivefilepicker_activity_filepicker_main)
        initSubscribe()
        init()
    }

    private fun initSubscribe() {
        storageViewModel.asyncSubscribe(this, StorageState::storageVolumes, onSuccess = {
            Log.d("lzqtest", "FilePickerActivity2.initSubscribe: 23 $it")
            // if(it.size>1){
            showStorageListFragment()
            // }
        },
                onFail = {

                })

        currentFolderViewModel.selectSubscribe(this, CurrentFolderState::currentVolume) { currentVolume ->
            Log.d("lzqtest", "FilePickerActivity2.initSubscribe: CurrentFolderState::currentVolume $currentVolume ")
            if (currentVolume != null) {
                showPickerFragment()
            }
        }
    }

    private fun showPickerFragment() {
        val fragment = PickerFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        val currentfragment=supportFragmentManager.findFragmentByTag(StorageListFragment::class.java.toString())
        if(currentfragment!=null)
            transaction.remove(currentfragment)
        transaction.replace(R.id.filepicker_content_frame, fragment, PickerFragment::class.java.toString())
        transaction.show(fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun showStorageListFragment() {
        val fragment = StorageListFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        val currentfragment=supportFragmentManager.findFragmentByTag(PickerFragment::class.java.toString())
        if(currentfragment!=null)
        transaction.remove(currentfragment)
        transaction.replace(R.id.filepicker_content_frame, fragment, StorageListFragment::class.java.toString())
        transaction.show(fragment)
        transaction.commitAllowingStateLoss()

    }

    @TargetApi(23)
    private fun requestNecessaryPermissions() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS)
    }

    private fun hasPermissions(): Boolean {
        val res = checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun init() {
        MimeTypeHelper.getMimeTypeHelperInstance().initMimeTypes(this)

        if (!hasPermissions() && getApiVersion() > 23) {
            requestNecessaryPermissions()
        } else {
            storageViewModel.fetchStorageList(this, true)
            try {
                val f = File("/storage/emulated/0 ")
                f.setReadable(true)
                f.setWritable(true)
                Log.d("lzqtest", "FilePickerActivity2.init: canRead= ${f.canRead()} ")

            }catch (e: Exception){

            }

        }

        mButtonBack = findViewById<ImageView>(R.id.btn_back)
        mButtonCancel = findViewById<TextView>(R.id.btn_cancel)

        mButtonCancel.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        mButtonBack.setOnClickListener(View.OnClickListener { v: View? -> onBackPressed() })

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("lzqtest", "FilePickerActivity2.handleOnBackPressed: 126 ")
                        if(isCurrentFragment(PickerFragment::class.java)){
                            Log.d("lzqtest", "FilePickerActivity2.handleOnBackPressed: show storagelist ")
                            showStorageListFragment()
                            return
                        }else{
                            Log.d("lzqtest", "FilePickerActivity2.handleOnBackPressed: 131 ")
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                        }


            }
        }

      onBackPressedDispatcher.addCallback(
              this,  // LifecycleOwner
              callback)
    }

    fun getApiVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grandResults: IntArray) {
        var allowed = true
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMS -> for (res in grandResults) {
                allowed = allowed && res == PackageManager.PERMISSION_GRANTED
            }
            else                       -> allowed = false
        }
        if (allowed) {
            storageViewModel.fetchStorageList(this, true)
            val f = File("/storage/emulated/0")
            Log.d("lzqtest", "FilePickerActivity2.onRequestPermissionsResult: canRead= ${f.canRead()} ")


        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        Log.d("lzqtest", "FilePickerActivity2.onBackPressed: before  super.onBackPressed()")
        super.onBackPressed()
        Log.d("lzqtest", "FilePickerActivity2.onBackPressed: after  super.onBackPressed() ")
//        if(isCurrentFragment(PickerFragment::class.java)){
//            Log.d("lzqtest", "FilePickerActivity2.onBackPressed: show storagelist ")
//            showStorageListFragment()
//            return
//        }
//        Log.d("lzqtest", "FilePickerActivity2.onBackPressed: exec super onbackPressed ")
//        super.onBackPressed()

    }
    private fun isCurrentFragment(fragmentClass: Class<out Fragment?>): Boolean {
        return supportFragmentManager.findFragmentByTag(fragmentClass.toString()) != null
    }
    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}