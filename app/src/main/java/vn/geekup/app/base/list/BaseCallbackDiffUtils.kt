package vn.geekup.app.base.list

import androidx.recyclerview.widget.DiffUtil
import vn.geekup.app.base.BaseViewItem

abstract class BaseCallbackDiffUtils : DiffUtil.Callback {

    private var mOldList: MutableList<BaseViewItem> = arrayListOf()
    private var mNewList: MutableList<BaseViewItem> = arrayListOf()

    constructor(mOldList: MutableList<BaseViewItem>, mNewList: MutableList<BaseViewItem>) {
        this.mOldList = mOldList
        this.mNewList = mNewList
    }

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].getItemId() == mNewList[newItemPosition].getItemId()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return mNewList[newItemPosition]
    }
}