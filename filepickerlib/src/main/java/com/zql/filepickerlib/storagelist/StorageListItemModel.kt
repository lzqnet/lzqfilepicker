package com.zql.filepickerlib.storagelist

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.zql.filepickerlib.R

@EpoxyModelClass
abstract class StorageListItemModel : EpoxyModelWithHolder<StorageListItemModel.Holder>() {

    @EpoxyAttribute
     var iconRes:Int?=null
    @EpoxyAttribute
    lateinit var name:String
    @EpoxyAttribute
    lateinit var capacity:String

    @EpoxyAttribute lateinit var listener: () -> Unit

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.mIvIcon.setImageResource(iconRes?:0)
        holder.mTvCapacity.text=capacity
        holder.mTvName.text=name
        holder.rootView.setOnClickListener { listener() }

    }

    class Holder : EpoxyHolder() {

         lateinit var mIvIcon: ImageView
         lateinit var mTvName: TextView
         lateinit var mTvCapacity: TextView
         lateinit var rootView: View

        override fun bindView(itemView: View) {
            rootView=itemView
            mIvIcon = itemView.findViewById(R.id.navigation_view_item_icon)
            mTvName = itemView.findViewById(R.id.navigation_view_item_name)
            mTvCapacity = itemView.findViewById(R.id.tv_capacity)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.drivefilepicker_item_storage
    }
}