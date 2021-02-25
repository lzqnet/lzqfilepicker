package com.zql.filepickerlib.picker

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.*
import com.zql.filepickerlib.R
import com.zql.filepickerlib.model.FileModel

@EpoxyModelClass
abstract class FolderItemViewModel: EpoxyModelWithHolder<FolderItemViewModel.FolderListHolder>()  {

//
//    folderItemHolder.mIvIcon.setImageResource(dataModel.getResourceIconId());
//    folderItemHolder.mTvName.setText(dataModel.getFileSystemObject().getName());
    @EpoxyAttribute
    lateinit var clickListener: IClickListener<FileModel>

    @EpoxyAttribute
    var iconRes:Int=0

    @EpoxyAttribute
    lateinit var name:String
    override fun getDefaultLayout(): Int {
        return R.layout.drivefilepicker_item_folder
    }

    override fun bind(holder: FolderListHolder) {
        super.bind(holder)
        holder.mIvIcon.setImageResource(iconRes)
        holder.mTvName.setText(name)
        holder.mRootView.setOnClickListener ( object :View.OnClickListener{
            override fun onClick(v: View?) {
                clickListener.OnClick()
            }

        })

    }

    class FolderListHolder : EpoxyHolder() {

        lateinit var mIvIcon: ImageView
        lateinit var mTvName: TextView
//        lateinit var mTvDate: TextView
//        lateinit var mTvSize: TextView
        lateinit var mRootView:View


        override fun bindView(itemView: View) {
            mRootView = itemView
            mIvIcon = itemView.findViewById(R.id.navigation_view_item_icon)
            mTvName = itemView.findViewById(R.id.navigation_view_item_name)
//            mTvDate = itemView.findViewById(R.id.navigation_view_item_summary)
//            mTvSize = itemView.findViewById(R.id.navigation_view_item_size)
        }
    }

}