package vn.geekup.app.module.moment

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import vn.geekup.app.domain.model.moment.MomentCommentModel
import vn.geekup.app.model.moment.MomentCommentModelV
import vn.geekup.app.utils.convertToCurrentTimeZone
import kotlin.collections.ArrayList

fun ArrayList<MomentCommentModel>.toArrayMomentCommentModelV(listener: ((ArrayList<MomentCommentModelV>) -> Unit)? = null) {
    Observable.create<ArrayList<MomentCommentModelV>> {
        val results: ArrayList<MomentCommentModelV> = arrayListOf()
        forEach { comment ->
            results.add(MomentCommentModelV().model2ModelV(comment))
        }
        results.sortBy { comment -> comment.createdAt?.convertToCurrentTimeZone()?.time }
        it.onNext(results)
    }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            listener?.invoke(it)
        }
}
