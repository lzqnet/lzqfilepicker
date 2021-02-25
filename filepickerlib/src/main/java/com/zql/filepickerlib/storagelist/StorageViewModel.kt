package com.zql.filepickerlib.storagelist

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.zql.filepickerlib.DefaultIconProvider
import com.zql.filepickerlib.BaseViewModel
import com.zql.filepickerlib.command.ListCommand
import com.zql.filepickerlib.config.DisplayRestrictions
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.model.FileSystemObject
import com.zql.filepickerlib.model.StorageVolumeWrapper
import com.zql.filepickerlib.util.RestrictionHelper
import com.zql.filepickerlib.util.StorageHelper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*

open class StorageViewModel(state: StorageState) : BaseViewModel<StorageState>(state) {
    val mListCommand = ListCommand()
    val mDefaultIconProvider = DefaultIconProvider()

    fun fetchStorageList(context: Context, reload: Boolean) {
        Log.d("lzqtest", "StorageListViewModel.fetchStorageList: 21 ")
        Observable.just("").observeOn(Schedulers.io())
            .map { _ -> StorageHelper.getStorageVolumes(context, reload) }
            .observeOn(AndroidSchedulers.mainThread())
            .execute {
                Log.d("lzqtest", "StorageListViewModel.fetchStorageList: 25 ")
                copy(storageVolumes = it)
            }
    }

//    @SuppressLint("CheckResult")
//    fun fetchFileList(context: Context, path: String, mRestrictions: Map<DisplayRestrictions?, Any?>?) {
//        Log.d("lzqtest", "StorageViewModel.fetchFileList: 31 ")
//        Observable.just(path).observeOn(Schedulers.io())
//            .map { data ->
//                val pathString = data
//                val fileSystemObjectList = mListCommand.execute(pathString)
//                Log.d("lzqtest", "StorageViewModel.fetchFileList:fileSystemObjectList size= ${fileSystemObjectList.size} ")
//                val sortedFiles = RestrictionHelper.applyUserPreferences(fileSystemObjectList, mRestrictions, false, context)
//                Log.d("lzqtest", "StorageViewModel.fetchFileList:sortedFiles size= ${sortedFiles.size} ")
//                sortedFiles
//            }.map { list ->
//                convertFileModel(list, context)
//            }.observeOn(AndroidSchedulers.mainThread())
//            .execute {
//                copy(currentFileList = it)
//            }
//
//
//    }
//
//    private fun convertFileModel(fileSystemObjects: List<FileSystemObject>, context: Context): List<FileModel> {
//        val fileModelList: MutableList<FileModel> = ArrayList<FileModel>()
//        Log.d("lzqtest", "StorageViewModel.convertFileModel:size= ${fileSystemObjects.size} ")
//
//        withState {
//            for (fso in fileSystemObjects) {
//                val model: FileModel = FileModel(fso)
//                val resourceId = mDefaultIconProvider.getDefaultIconResId(context, fso)
//                model.setResourceIconId(resourceId)
//                val selectModel: FileModel? = it.selectedFileList?.get(fso.getFullPath())
//                if (selectModel != null) {
//                    model.setSelected(true)
//                } else {
//                    model.setSelected(false)
//                }
//                fileModelList.add(model)
//            }
//
//        }
//        Log.d("lzqtest", "StorageViewModel.convertFileModel:size ${fileModelList.size} ")
//        return fileModelList
//    }

//    fun setCurrentVolume(volume: StorageVolumeWrapper) {
//        Log.d("lzqtest", "StorageViewModel.setCurrentVolume: 79 $volume")
//        setState { copy(currentVolume = volume) }
//
//    }
//
//    fun setCurrentPath(path: String?) {
//        Log.d("lzqtest", "StorageViewModel.setCurrentPath: 84 $path")
//        setState { copy(currentPath = path) }
//
//    }

//    fun setSelectedFile(file: FileModel, isAdd: Boolean) {
//        Log.d("lzqtest", "StorageViewModel.setSelectedFile: $file is add=$isAdd")
//        if (isAdd) {
//            withState {
//                val newstate=it.copy()
//                val seclected = newstate.selectedFileList
//                seclected?.put(file.fileSystemObject.fullPath, file)
//                setState { newstate }
//            }
//        } else {
//            withState {
//                val newstate=it.copy()
//                val seclected = newstate.selectedFileList
//                seclected?.remove(file.fileSystemObject.fullPath)
//                setState { newstate }
//            }
//        }
//    }


    companion object : MvRxViewModelFactory<StorageViewModel, StorageState> {

        override fun create(
                viewModelContext: ViewModelContext,
                state: StorageState
        ): StorageViewModel {
            return StorageViewModel(state)
        }
    }
}