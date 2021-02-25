package com.zql.filepickerlib.storagelist

import android.content.Context
import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.zql.filepickerlib.picker.CurrentFolderViewModel
import com.zql.filepickerlib.model.StorageVolumeWrapper
import com.zql.filepickerlib.util.FileHelper

class StorageListViewController(val context: Context,val model:CurrentFolderViewModel) : TypedEpoxyController<List<StorageVolumeWrapper>>() {


    override fun buildModels(data: List<StorageVolumeWrapper>?) {
        Log.d("lzqtest", "StorageListViewController.buildModels: 13 $data")
        if (data != null) {
            for (item in data) {
                storageListItem {
                    this.id(item.id)
                    Log.d("lzqtest", "StorageListViewController.buildModels: item.getDescription(context)=${item.getDescription(context)}" +
                            "iconRes=${item.iconId} ")
                    this.name(item.getDescription(context))
                    this.iconRes(item.iconId)
                    val usedSizeString: String = FileHelper.getHumanReadableSize(item.getUsedSize())
                    val totalSizeString: String = FileHelper.getHumanReadableSize(item.getMaxSize())
                    this.capacity("$usedSizeString / $totalSizeString")
                    listener { model.setCurrentVolume(item) }
                }


            }
        }
    }
}