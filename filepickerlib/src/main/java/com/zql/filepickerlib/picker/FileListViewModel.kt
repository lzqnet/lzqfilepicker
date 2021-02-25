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
import com.zql.filepickerlib.model.FileSystemObject
import com.zql.filepickerlib.util.RestrictionHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.HashMap

open class FileListViewModel(state: FileListState) : BaseViewModel<FileListState>(state) {
    val mListCommand = ListCommand()
    val mDefaultIconProvider = DefaultIconProvider()

    //    var atomicInteger: AtomicInteger = AtomicInteger(0)
    init {
        logStateChanges()
    }

    @SuppressLint("CheckResult")
    fun fetchFileList(context: Context, path: String, mRestrictions: Map<DisplayRestrictions?, Any?>?) {
        //        val test = atomicInteger.incrementAndGet()

        Log.d("lzqtest", "FileListViewModel.fetchFileList: enter ")
        withState {
            Observable.just(it).observeOn(Schedulers.io())
                .map { state ->
                    val fileSystemObjectList = mListCommand.execute(path)
                    Log.d("lzqtest", "FileListViewModel.fetchFileList:fileSystemObjectList size= ${fileSystemObjectList.size} get list thread=${Thread.currentThread().name} ")
                    val sortedFiles = RestrictionHelper.applyUserPreferences(fileSystemObjectList, mRestrictions, false, context)
                    Log.d("lzqtest", "FileListViewModel.fetchFileList:sortedFiles size= ${sortedFiles.size} ")
                    Log.d("lzqtest", "FileListViewModel.fetchFileList: before convertFileModel ")
                    val ret = convertFileModel(sortedFiles, context, state.selectedFileList)
                    Log.d("lzqtest", "FileListViewModel.fetchFileList: after convertFileModel ")
                    ret
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("lzqtest", "FileListViewModel.fetchFileList: subscribe ")
                    setState { copy(currentFileList = it) }
                }, {
                    Log.d("lzqtest", "FileListViewModel.fetchFileList: subscribe throwable ", it)
                    setState { copy(currentFileList = null) }

                })

        }


    }

    private fun convertFileModel(fileSystemObjects: List<FileSystemObject>, context: Context, selectedItems: HashMap<String, FileModel>?): FileList<FileModel> {
        val fileModelList = FileList<FileModel>()
        Log.d("lzqtest", "FileListViewModel.convertFileModel:size= ${fileSystemObjects.size} thread=${Thread.currentThread()}  ")

        Log.d("lzqtest", "FileListViewModel.convertFileModel: withstate thread=${Thread.currentThread().name}   ")
        for (fso in fileSystemObjects) {
            val model: FileModel = FileModel(fso)
            val resourceId = mDefaultIconProvider.getDefaultIconResId(context, fso)
            model.setResourceIconId(resourceId)
            val selectModel: FileModel? = selectedItems?.get(fso.getFullPath())
            if (selectModel != null) {
                model.setSelected(true)
            } else {
                model.setSelected(false)
            }
            fileModelList.add(model)
        }

        Log.d("lzqtest", "FileListViewModel.convertFileModel:size ${fileModelList.size}  ")
        return fileModelList
    }

    fun setSelectedAllFile(isAdd: Boolean) {
        Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: is add=$isAdd")
        withState {
            val newState = it.copy(selectedFileList = HashMap())

            val currentlist = newState.currentFileList
            if (currentlist.isNullOrEmpty()) {
                Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: currentlist is null ")
                return@withState
            }
            val itr = currentlist.iterator()
            if (isAdd) {
                Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: size=${currentlist.size} ")

                while (itr.hasNext()) {
                    val data = itr.next()
                    if(!data.isDirectory) {
                        newState.selectedFileList?.put(data.fileSystemObject.fullPath, data)
                    }else{
                        Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: is dir $data ")
                    }
                }
                it.selectedFileList?.let { it1 -> newState.selectedFileList?.putAll(it1) }

            }else{
                it.selectedFileList?.let { it1 -> newState.selectedFileList?.putAll(it1) }
                while (itr.hasNext()) {
                    val data = itr.next()
                    if(!data.isDirectory) {
                        newState.selectedFileList?.remove(data.fileSystemObject.fullPath)
                    }else{
                        Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: isadd=$isAdd is dir $data ")
                    }
                }
            }
            Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: setstate ")
            setState { newState }
        }
    }

    fun setSelectedFile(file: FileModel, isAdd: Boolean) {
        Log.d("lzqtest", "FileListViewModel.setSelectedFile: $file is add=$isAdd")
        if(file.isDirectory){
            Log.d("lzqtest", "FileListViewModel.setSelectedFile: file is directory $file ")
            return
        }
        withState {
            val newstate = it.copy(selectedFileList = if (!it.selectedFileList.isNullOrEmpty()) {
                HashMap(it.selectedFileList)
            } else {
                HashMap()
            })
            val seclectedList = newstate.selectedFileList

            if (isAdd) {
                seclectedList?.put(file.fileSystemObject.fullPath, file)
            } else {
                seclectedList?.remove(file.fileSystemObject.fullPath)
            }
            setState { newstate }

        }
    }

    fun applySelectedFiles() {
        Log.d("lzqtest", "FileListViewModel.applySelectedFiles: 130 ")
        withState {
            val newState = it.copy(currentFileList = FileList())
            val newList = newState.currentFileList
            val oldList = it.currentFileList
            if (oldList.isNullOrEmpty()) {
                Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: currentlist is null ")
                return@withState
            }
            val itr = oldList.iterator()
            Log.d("lzqtest", "FileListViewModel.setSelectedAllFile: new state selectedFileList size=${newState.selectedFileList?.size}  old select size =${it.selectedFileList?.size}")

            while (itr.hasNext()) {
                val data = itr.next()
                val newData = FileModel(data.fileSystemObject)
                newData.isSelected = newState.selectedFileList?.containsKey(data.fileSystemObject.fullPath)
                        ?: false
                newData.resourceIconId = data.resourceIconId
                newList?.add(newData)
            }
            Log.d("lzqtest", "FileListViewModel.applySelectedFiles: setState =newstate!=oldState:${newState!=it}  ")

            setState { newState }

        }
    }


    //    fun mergeSelectInfoToList(){
    //        withState {
    //            val newList=FileList<FileModel>()
    //            val newstate = it.copy()
    //            val list=newstate.currentFileList.invoke()
    //            if(list.isNullOrEmpty()){
    //                return@withState
    //            }
    //            val itr = list.iterator()
    //            while (itr.hasNext()){
    //                val data=itr.next()
    //                data.isSelected=it.selectedFileList?.containsKey(data.fileSystemObject.fullPath)?:false
    //                newList.add(data)
    //            }
    //            setState { newstate }
    //
    //        }
    //    }


    companion object : MvRxViewModelFactory<FileListViewModel, FileListState> {

        override fun create(
                viewModelContext: ViewModelContext,
                state: FileListState
        ): FileListViewModel {
            return FileListViewModel(state)
        }
    }
}