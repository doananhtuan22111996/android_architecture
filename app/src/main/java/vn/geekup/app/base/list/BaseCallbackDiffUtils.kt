package vn.geekup.app.base.list

import androidx.recyclerview.widget.DiffUtil
import vn.geekup.app.base.BaseViewItem

abstract class BaseCallbackDiffUtils(
    private var mOldList: MutableList<BaseViewItem>,
    private var mNewList: MutableList<BaseViewItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].getItemId() == mNewList[newItemPosition].getItemId()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return mNewList[newItemPosition]
    }
}