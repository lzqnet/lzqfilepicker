package com.zql.filepickerlib.picker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.zql.filepickerlib.R
import com.zql.filepickerlib.model.FilePathModel
import com.zql.filepickerlib.storagelist.StorageListItemModel

@EpoxyModelClass
abstract class BreadCrumbsItemModel : EpoxyModelWithHolder<BreadCrumbsItemModel.Holder>() {
    @EpoxyAttribute
    lateinit var mName: String

    //    @EpoxyAttribute
    //    var iconRes: Int? = null
    @EpoxyAttribute
    var imageViewVisibile: Int = View.VISIBLE

    @EpoxyAttribute
    var textColor: Int = R.color.space_kit_b500

    @EpoxyAttribute
     var listener: IClickListener<FilePathModel>?=null

    override fun getDefaultLayout(): Int {
        return R.layout.drivefilepicker_item_breadcrumbs
    }

    override fun bind(holder: Holder) {
        super.bind(holder)
        //        holder.mImageNext.setImageResource(iconRes ?: 0)
        holder.mTvName.text = mName
        holder.mRootView.setOnClickListener (object :View.OnClickListener{
            override fun onClick(v: View?) {
                listener?.OnClick()
            }
        })
        holder.mImageNext.visibility = imageViewVisibile
        holder.mTvName.setTextColor(textColor)

    }

    class Holder : EpoxyHolder() {

        lateinit var mTvName: TextView
        lateinit var mImageNext: ImageView
        lateinit var mRootView: View


        override fun bindView(itemView: View) {
            mRootView = itemView
            mTvName = itemView.findViewById(R.id.tv_folder_name)
            mImageNext = itemView.findViewById(R.id.image_next)
        }
    }

}