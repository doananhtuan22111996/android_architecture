package vn.geekup.app.data.model.general

import vn.geekup.app.domain.model.general.BaseModel

abstract class BaseVO<BM : BaseModel> {
    abstract fun vo2Model(): BM?
}
