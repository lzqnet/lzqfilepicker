package com.zql.filepickerlib.storagelist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.zql.filepickerlib.R
import com.zql.filepickerlib.picker.CurrentFolderViewModel

class StorageListFragment : BaseMvRxFragment() {
    companion object {
        fun newInstance(b: Bundle?=null): StorageListFragment {
            val frag = StorageListFragment()
            if(b!=null) {
                frag.arguments = b
            }
            return frag
        }
    }

    private val storageListViewModel by activityViewModel(StorageViewModel::class)
    private val currentFolderViewModel by activityViewModel(CurrentFolderViewModel::class)

    lateinit var controller: StorageListViewController
    private lateinit var recyclerView: EpoxyRecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drivefilepicker_fragment_storage_list, container, false).apply {
            recyclerView = this.findViewById(R.id.storage_list_layout)
            controller = StorageListViewController(context,currentFolderViewModel)
            recyclerView.setController(controller)
            recyclerView.layoutManager = LinearLayoutManager(context)

            initSubscribe()
        }
    }


    private fun initSubscribe(){
        storageListViewModel.asyncSubscribe(this, StorageState::storageVolumes, onSuccess = {
            Log.d("lzqtest", "FilePickerActivity2.initSubscribe: 23 $it")
            controller.setData(it)
        },
                onFail = {

                })

    }

    override fun invalidate() {

    }


}