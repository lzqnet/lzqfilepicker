package com.zql.filepickerlib.picker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.zql.filepickerlib.R
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.model.FilePathModel

class FilePickerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var breadCrumb: EpoxyRecyclerView
    private lateinit var fileList: EpoxyRecyclerView

    //    private lateinit var mEmptyImageView: ImageView
    //    private lateinit var mEmptyTextView: TextView
    private lateinit var mEmptyView: LinearLayout

    private lateinit var mBreadCrumbViewController: BreadCrumbViewController
    private lateinit var mFileListViewController: FileListViewController

    init {
        LayoutInflater.from(context).inflate(R.layout.drivefilepicker_file_select_view, this)
        breadCrumb = findViewById(R.id.bread_crumb)
        fileList = findViewById(R.id.file_list)
        //        mEmptyImageView = findViewById(R.id.empty_view)
        //        mEmptyTextView = findViewById(R.id.empty_text)
        mEmptyView = findViewById(R.id.empty_view_ll)
        //        mBreadCrumbViewController= BreadCrumbViewController(context)
        //        mFileListViewController= FileListViewController(context)

    }

    fun setBreadCrumbController(controller: BreadCrumbViewController) {
        mBreadCrumbViewController = controller
        breadCrumb.setController(mBreadCrumbViewController)
        breadCrumb.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setFileListController(controller: FileListViewController) {
        mFileListViewController = controller
        fileList.setController(mFileListViewController)
        fileList.layoutManager = LinearLayoutManager(context)
    }

    fun resetBreadCrumb(list: List<FilePathModel>) {
        Log.d("lzqtest", "FilePickerView.updateBreadCrumb: 34 ")
        mBreadCrumbViewController.setData(list)
    }

    fun showEmptyView(isShow: Boolean) {
        if (isShow) {
            mEmptyView.visibility = VISIBLE
        } else {
            mEmptyView.visibility = GONE
        }
    }

    //    fun resetFileList(list: List<FileModel>) {
    //        Log.d("lzqtest", "FilePickerView.resetFileList: 44 ")
    //        mFileListViewController.setData(list)
    //
    //    }

}