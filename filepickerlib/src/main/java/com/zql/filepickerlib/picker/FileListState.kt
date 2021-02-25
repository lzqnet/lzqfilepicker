package com.zql.filepickerlib.picker

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.zql.filepickerlib.model.FileModel
import java.util.HashMap

data class FileListState(
        val currentFileList: FileList<FileModel>? = null,
        val selectedFileList: HashMap<String, FileModel>? = null
) : MvRxState
