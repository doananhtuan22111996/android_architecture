package vn.geekup.data.model.general

import vn.geekup.domain.model.general.BaseModel

abstract class BaseVO<BM : BaseModel> {
    abstract fun vo2Model(): BM?
}
