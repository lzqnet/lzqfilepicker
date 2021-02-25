package com.zql.filepickerlib.storagelist

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.model.FileSystemObject
import com.zql.filepickerlib.model.StorageVolumeWrapper
import java.util.ArrayList
import java.util.HashMap

data class StorageState(
        val storageVolumes: Async<ArrayList<StorageVolumeWrapper>> = Uninitialized,
//        val currentVolume: StorageVolumeWrapper? = null,
//        val currentPath: String? = null,
//        val currentFileList: Async<List<FileModel>> = Uninitialized,
//        val selectedFileList: HashMap<String, FileModel>? = null
) : MvRxState
