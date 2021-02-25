package com.zql.filepickerlib.picker

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.model.FilePathModel
import com.zql.filepickerlib.model.FileSystemObject
import com.zql.filepickerlib.model.StorageVolumeWrapper
import java.util.ArrayList
import java.util.HashMap

data class CurrentFolderState(
        val currentVolume: StorageVolumeWrapper? = null,
        val currentPath: String? = null,
        val filePathModels: ArrayList<FilePathModel>?=null
) : MvRxState
