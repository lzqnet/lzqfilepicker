package com.zql.filepickerlib.picker

import android.content.Context
import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.storagelist.StorageViewModel
import com.zql.filepickerlib.util.DateUtil
import com.zql.filepickerlib.util.FileHelper

class FileListViewController(val context: Context,
val fileListViewModel:  FileListViewModel,
val currentFolderViewModel: CurrentFolderViewModel)
    : TypedEpoxyController<List<FileModel>>() {

    override fun buildModels(data: List<FileModel>?) {
        if (!data.isNullOrEmpty()) {
            Log.d("lzqtest", "FileListViewController.buildModels: enter thread=${Thread.currentThread().name} ")
            for ((index, item) in data.withIndex()) {
                if (item.isDirectory) {
                    folderItemView {
                        id(index)
                        name(item.fileSystemObject.name)
                        iconRes(item.resourceIconId)
                        clickListener(object : IClickListener<FileModel> {

                            override fun OnClick() {
                                Log.d("lzqtest", "PickerFragment.OnClick: folder click data=${item.fileSystemObject.fullPath} ")
                                currentFolderViewModel.setCurrentPath(item.fileSystemObject.fullPath)
                            }

                        })

                    }

                } else {
                    fileItemView {
                        iconRes(item.resourceIconId)
                        name(item.fileSystemObject.name)
                        date(DateUtil.getFormatTime(context, item.fileSystemObject.lastModifiedTime))
                        size(FileHelper.getHumanReadableSize(item.fileSystemObject))
                        selected(item.isSelected)
                        id(index)

                        clickListener(object : IFileClickListener<FileModel> {
                            override fun OnClick(isSelected: Boolean) {
                                Log.d("lzqtest", "PickerFragment.OnClick: file click $isSelected  data=${item.fileSystemObject.fullPath}")
                                fileListViewModel.setSelectedFile(item, isSelected)
//                                item.isSelected=isSelected
                            }
                        }
                        )


                    }
                }
            }
        }

    }
}