package com.zql.filepickerlib.picker

import android.content.Context
import android.util.Log
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.zql.filepickerlib.model.FilePathModel
import com.zql.filepickerlib.storagelist.StorageViewModel
import java.util.*


class BreadCrumbViewController(val context: Context, val viewModel: CurrentFolderViewModel) : TypedEpoxyController<List<FilePathModel>>() {
    // lateinit var listener:()->Unit
    //    fun setItemListener(callback:()->Unit){
    //        listener=callback
    //    }


    override fun buildModels(data: List<FilePathModel>?) {
        Log.d("lzqtest", "BreadCrumbViewController.buildModels: $data ")
        if (!data.isNullOrEmpty()) {
            for ((index, item) in data.withIndex()) {
                breadCrumbsItem {
                    //                    this.iconRes(item.)
                    this.mName(item.name)
                    this.id(index)
                    if (data.size == 1) {
                        this.listener(null)
                    } else {
                        this.listener(object : IClickListener<FilePathModel> {
                            override fun OnClick() {
                                Log.d("lzqtest", "BreadCrumbViewController.OnClick: item.path=${item.path} ")
                                viewModel.setCurrentPath(item.path)
                            }

                        })
                    }
                    if (index == data.size - 1) {
                        this.imageViewVisibile(View.INVISIBLE)
                        this.listener(null)
                    }
                }
            }

        }
    }
}