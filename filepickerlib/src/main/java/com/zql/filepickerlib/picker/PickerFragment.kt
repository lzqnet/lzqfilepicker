package com.zql.filepickerlib.pick

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.zql.filepickerlib.R
import com.zql.filepickerlib.FilePickerConstants
import com.zql.filepickerlib.picker.*
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.model.FileSystemObject
import com.zql.filepickerlib.storagelist.StorageViewModel
import com.zql.filepickerlib.util.FileHelper
import java.io.File

class PickerFragment : BaseMvRxFragment() {
    private val storageViewModel by activityViewModel(StorageViewModel::class)
    private val filelistViewModel by activityViewModel(FileListViewModel::class)
    private val currentFolderViewModel by activityViewModel(CurrentFolderViewModel::class)

    private lateinit var mBreadCrumbViewController: BreadCrumbViewController
    private lateinit var mFileListViewController: FileListViewController
    private lateinit var mViewHolder: ViewHolder

    companion object {
        fun newInstance(b: Bundle? = null): PickerFragment {
            val frag = PickerFragment()
            if (b != null) {
                frag.arguments = b
            }
            return frag
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("lzqtest", "PickerFragment.handleOnBackPressed: 54 ")
                withState(currentFolderViewModel) {
                    if (!onBackPressed(it)) {
                        Log.d("lzqtest", "PickerFragment.handleOnBackPressed: before onBackPressed")
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        Log.d("lzqtest", "PickerFragment.handleOnBackPressed: after onBackPressed ")
                        isEnabled = true
                    }
                }


            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
                this,  // LifecycleOwner
                callback)
    }

    fun onBackPressed(state: CurrentFolderState): Boolean {
        Log.d("lzqtest", "PickerFragment.onBackPressed: 75 ")
        if (TextUtils.equals(state.currentPath, state.currentVolume?.path)) {
            Log.d("lzqtest", "PickerFragment.onBackPressed: return false ")
            return false
        } else if (state.currentPath != null) {
            val fileSystemObject: FileSystemObject = FileHelper.createFileSystemObject(File(state.currentPath))
            if (fileSystemObject != null) {
                currentFolderViewModel.setCurrentPath(fileSystemObject.parent)
            }
            Log.d("lzqtest", "PickerFragment.onBackPressed: return true ")
            return true
        }
        Log.d("lzqtest", "PickerFragment.onBackPressed: 85 return false")
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drivefilepicker_fragment_file_picker, container, false).apply {
            mBreadCrumbViewController = BreadCrumbViewController(context, currentFolderViewModel)
            mFileListViewController = FileListViewController(context, filelistViewModel, currentFolderViewModel)

            mViewHolder = ViewHolder()
            mViewHolder.rootView = this
            mViewHolder.filePickerView = this.findViewById(R.id.filepicker_content_view)
            mViewHolder.filePickerView.setBreadCrumbController(mBreadCrumbViewController)
            mViewHolder.filePickerView.setFileListController(mFileListViewController)

            mViewHolder.mBtnCurSelect = mViewHolder.rootView.findViewById<TextView>(R.id.btn_cur_select)
            mViewHolder.mBtnSelectAll = mViewHolder.rootView.findViewById<LinearLayout>(R.id.btn_select_all)
            mViewHolder.mCheckBoxSelectAll = mViewHolder.rootView.findViewById<CheckBox>(R.id.checkbox_select_all)
            mViewHolder.mBtnUpload = mViewHolder.rootView.findViewById<Button>(R.id.btn_confirm)
            mViewHolder.mCheckBoxSelectAll.isClickable = false
            initView()
            initSubscribe()
            initData()
        }
    }

    //    private fun hasPermissions(): Boolean {
    //        context?.let {
    //            val res = checkCallingOrSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    //            return res == PackageManager.PERMISSION_GRANTED
    //        }
    //
    //        return false
    //    }

    private fun initData() {
        //        Log.d("lzqtest", "PickerFragment.initData: hasPermissions=${hasPermissions()} ")
        withState(currentFolderViewModel) {
            Log.d("lzqtest", "PickerFragment.initData: 90 $it ")
            currentFolderViewModel.setCurrentPath(it.currentVolume?.path)
        }
    }

    private fun initView() {

        //        mViewHolder.mCheckBoxSelectAll.setClickable(false)
        //        changeSelectCountAndConfirmButtonState()
    }

    private fun setSelectCountText(count: Int) {
        //        val text: String
        //        text = if (count <= 1) {
        //            UIHelper.mustacheFormat(mViewHolder.rootView.getContext(),
        //                    R.string.Drive_FilePicker_FilesSinglular, "select_count", count.toString())
        //        } else {
        //            UIHelper.mustacheFormat(mViewHolder.rootView.getContext(),
        //                    R.string.Drive_FilePicker_FilesPlural, "select_count", count.toString())
        //        }
        //        mViewHolder.mBtnCurSelect.setText(text)
    }


    private fun setConfirmButtonState(count: Int) {
        //        if (count == 0) {
        //            mViewHolder.mBtnUpload.setBackground(resources.getDrawable(R.drawable.drivefilepicker_shape_btn_upload_disable, null))
        //            mViewHolder.mBtnUpload.setClickable(false)
        //        } else {
        //            mViewHolder.mBtnUpload.setBackground(resources.getDrawable(R.drawable.drivefilepicker_shape_btn_upload, null))
        //            mViewHolder.mBtnUpload.setClickable(true)
        //        }
    }

    private fun changeSelectCountAndConfirmButtonState() {
        //        withState(storageViewModel) {
        //            var count: Int = it.currentFileList.invoke()?.size ?: 0
        //            setSelectCountText(count)
        //            setConfirmButtonState(count)
        //        }

    }

    private fun sendPickedFileList(fileModelList: Map<String, FileModel>?) {
        Log.d("lzqtest", "PickerFragment.sendPickedFileList: fileModelList=$fileModelList ")
        if (fileModelList.isNullOrEmpty()) {
            Log.d("lzqtest", "PickerFragment.sendPickedFileList: fileModelList is null  ")
            return
        }
        if (activity != null) {
            val data = Intent()
            data.putStringArrayListExtra(FilePickerConstants.EXTRA_FILE_PATH_LIST, covertPickedFileList(fileModelList))
            activity!!.setResult(Activity.RESULT_OK, data)
            activity!!.finish()
        }
    }

    private fun covertPickedFileList(fileModelList: Map<String, FileModel>): ArrayList<String>? {
        val list = ArrayList<String>()
        for ((_, value) in fileModelList) {
            list.add(value.getFileSystemObject().getFullPath())
        }
        return list
    }

    private fun clickSelectAll() {
        //        withState(storageViewModel) {
        //            val curFileList: List<FileModel>? = it.currentFileList.invoke()
        //            if (curFileList.isNullOrEmpty()) {
        //                return@withState
        //            }
        //            val toSelect: Boolean = !getCurSelectAllState()
        //            for (fileModel in curFileList) {
        //                (fileModel as FileModel).isSelected = toSelect
        //
        //            }
        //            mViewHolder.mCheckBoxSelectAll.isChecked = toSelect
        //            changeSelectCountAndConfirmButtonState()
        //            mFileListViewController.setData(curFileList)
        //
        //        }

    }


    //    private fun getCurSelectAllState(): Boolean {
    //        return mViewHolder.mCheckBoxSelectAll.isChecked
    //    }

    private fun resetSelectAllState() {
        withState(filelistViewModel) {
            var hasFile: Boolean = false
            var selectedAll: Boolean = true
            Log.d("lzqtest", "PickerFragment.resetSelectAllState: thread=${Thread.currentThread().name} ")
            val itr = it.currentFileList?.iterator()
            if (itr == null || it.currentFileList.size == 0 || it.selectedFileList.isNullOrEmpty()) {
                mViewHolder.mCheckBoxSelectAll.isChecked = false

                return@withState
            }

            while (itr.hasNext()) {
                val data = itr.next()
                if (data.isDirectory) {
                    continue
                }
                hasFile = true
                if (it.selectedFileList.containsKey(data.fileSystemObject.fullPath)) {
                    continue
                } else {
                    Log.d("lzqtest", "PickerFragment.resetSelectAllState:has no selected file set selectall btn to false ")
                    mViewHolder.mCheckBoxSelectAll.isChecked = false
                    selectedAll = false
                    break
                }
            }
            if (!itr.hasNext() && selectedAll&& hasFile) {
                Log.d("lzqtest", "PickerFragment.resetSelectAllState: set selectall btn to true selectedAll$selectedAll hasFile=$hasFile itr.hasNext()=${itr.hasNext()}")
                mViewHolder.mCheckBoxSelectAll.isChecked = true

            }
            if (!hasFile) {
                Log.d("lzqtest", "PickerFragment.resetSelectAllState: has not file,set selectall btn to false ")
                mViewHolder.mCheckBoxSelectAll.isChecked = false

            }
        }
    }

    private fun initSubscribe() {
        mViewHolder.mBtnUpload.setOnClickListener(View.OnClickListener { v: View? ->
            withState(filelistViewModel) {
                sendPickedFileList(it.selectedFileList)
            }
        })
        mViewHolder.mBtnSelectAll.setOnClickListener(View.OnClickListener { v: View? ->
            Log.d("lzqtest", "PickerFragment.initSubscribe: click selectall button ")
            mViewHolder.mCheckBoxSelectAll.isChecked = !mViewHolder.mCheckBoxSelectAll.isChecked
            filelistViewModel.setSelectedAllFile(mViewHolder.mCheckBoxSelectAll.isChecked)
            resetSelectAllState()
        })

        filelistViewModel.selectSubscribe(this, FileListState::currentFileList) {
            Log.d("lzqtest", "PickerFragment.initSubscribe enter:  FileListState::currentFileList size= ${it?.size} thread=${Thread.currentThread().name}")
            if (it.isNullOrEmpty()) {
                Log.d("lzqtest", "initSubscribe: storageViewModel currentFileList is null")
                mViewHolder.filePickerView.showEmptyView(true)
                mFileListViewController.setData(FileList<FileModel>())
            } else {
                mViewHolder.filePickerView.showEmptyView(false)
                mFileListViewController.setData(it)
            }
            //            changeSelectCountAndConfirmButtonState()
            resetSelectAllState()
        }


        filelistViewModel.selectSubscribe(this, FileListState::selectedFileList) {
            Log.d("lzqtest", "PickerFragment.initSubscribe: selectedFileList $it ")
            filelistViewModel.applySelectedFiles()
            resetSelectAllState()
        }


        currentFolderViewModel.selectSubscribe(CurrentFolderState::currentPath) {
            //            val filePathModels: ArrayList<FilePathModel> = ArrayList<FilePathModel>()
            //            Log.d("lzqtest", "PickerFragment.initSubscribe: CurrentFolderState::currentPath=$it")
            //            if (!it.isNullOrEmpty()) {
            //                val pathArray = it.split("/").toList()
            //                Log.d("lzqtest", "PickerFragment.initSubscribe: pathArray=${pathArray.toString()} ")
            //                for (i in pathArray.indices) {
            //                    val filePathModel: FilePathModel = FilePathModel()
            //                    filePathModel.setName(pathArray[i])
            //                    val path: StringBuilder = StringBuilder()
            //                    for (j in 0..i) {
            //                        path.append("/")
            //                        path.append(pathArray[j])
            //                    }
            //                    Log.d("lzqtest", "PickerFragment.initSubscribe: path=$path ")
            //                    filePathModel.setPath(path.toString())
            //                    filePathModels.add(filePathModel)
            //                }
            //            }
            currentFolderViewModel.resetBreadCrumbList()
            //            Log.d("lzqtest", "PickerFragment.initSubscribe: StorageState::currentPath filePathModels $filePathModels")
            //            mViewHolder.filePickerView.resetBreadCrumb(filePathModels)
            //            if (it != null) {
            //                Log.d("lzqtest", "PickerFragment.initSubscribe: StorageState::currentPath fetchFileList ")
            //                getContext()?.let { context -> filelistViewModel.fetchFileList(context, it, null) }
            //            }

        }

        currentFolderViewModel.selectSubscribe(this, CurrentFolderState::filePathModels) {
            Log.d("lzqtest", "PickerFragment.initSubscribe: 230 ")
            withState(currentFolderViewModel) {
                if (!it.filePathModels.isNullOrEmpty() && !it.currentPath.isNullOrEmpty()) {
                    Log.d("lzqtest", "PickerFragment.initSubscribe: 232 ")
                    mViewHolder.filePickerView.resetBreadCrumb(it.filePathModels)
                    getContext()?.let { context -> filelistViewModel.fetchFileList(context, it.currentPath, null) }

                }
            }

        }
    }

    override fun invalidate() {
    }

    private class ViewHolder {
        lateinit var rootView: View
        lateinit var filePickerView: FilePickerView
        lateinit var mBtnCurSelect: TextView
        lateinit var mBtnSelectAll: LinearLayout
        lateinit var mCheckBoxSelectAll: CheckBox
        lateinit var mBtnUpload: Button
    }
}