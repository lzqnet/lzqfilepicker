package com.zql.filepickerlib.picker

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
import com.zql.filepickerlib.model.FilePathModel
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

open class CurrentFolderViewModel(state: CurrentFolderState) : BaseViewModel<CurrentFolderState>(state) {
    val mListCommand = ListCommand()
    val mDefaultIconProvider = DefaultIconProvider()


    fun setCurrentVolume(volume: StorageVolumeWrapper) {
        Log.d("lzqtest", "CurrentFolderViewModel.setCurrentVolume: 79 $volume")
        setState { copy(currentVolume = volume) }

    }

    fun setCurrentPath(path: String?) {
        Log.d("lzqtest", "CurrentFolderViewModel.setCurrentPath: 84 $path")
        setState { copy(currentPath = path) }

    }

    fun resetBreadCrumbList() {
        Log.d("lzqtest", "CurrentFolderViewModel.resetBreadCrumbList: 43 ")
        withState {
            val filePathModels: ArrayList<FilePathModel> = ArrayList<FilePathModel>()
            val currentVolumePath = it.currentVolume?.path
            val currentFolderPath = it.currentPath
            Log.d("lzqtest", "CurrentFolderViewModel.resetBreadCrumbList: currentVolumePath=$currentVolumePath currentFolderPath=$currentFolderPath ")
            if (currentVolumePath.isNullOrEmpty() || currentFolderPath.isNullOrEmpty()) {
                return@withState
            }
            val relativePath = currentFolderPath.removePrefix(currentVolumePath)

            //            if (!relativePath.isNullOrEmpty()) {
            val pathArray = relativePath.split("/").toList()

            Log.d("lzqtest", "CurrentFolderViewModel.resetBreadCrumbList: pathArray=${pathArray.toString()} ")
            for (i in pathArray.indices) {
                val filePathModel = FilePathModel()
                if (i == 0) {
                    filePathModel.setName("Internal Storage")
                } else {
                    filePathModel.setName(pathArray[i])
                }
                val path: StringBuilder = StringBuilder(currentVolumePath)
                for (j in 0..i) {
                    if(!path.endsWith('/')) {
                        path.append('/')
                    }
                    path.append(pathArray[j])
                }
                Log.d("lzqtest", "CurrentFolderViewModel.resetBreadCrumbList: path=$path ")
                filePathModel.setPath(path.toString())
                filePathModels.add(filePathModel)
            }
            Log.d("lzqtest", "CurrentFolderViewModel.resetBreadCrumbList: setState ")
            setState { copy(filePathModels = filePathModels) }
            
        }
    }


    companion object : MvRxViewModelFactory<CurrentFolderViewModel, CurrentFolderState> {

        override fun create(
                viewModelContext: ViewModelContext,
                state: CurrentFolderState
        ): CurrentFolderViewModel {
            return CurrentFolderViewModel(state)
        }
    }
}