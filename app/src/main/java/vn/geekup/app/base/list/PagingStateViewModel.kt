package vn.geekup.app.base.list

import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.databinding.ItemPagingStateBinding

class PagingStateViewModel(override val viewBinding: ItemPagingStateBinding) :
    BaseRecyclerHolder<BaseViewItem>(viewBinding = viewBinding) {

    override fun bindData(data: BaseViewItem, position: Int) {
        super.bindData(data, position)
        viewBinding.rlParentProgressbar.visible(true)
    }

}
