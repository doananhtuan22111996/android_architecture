package vn.geekup.app.data.services

import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import vn.geekup.app.data.Config
import vn.geekup.app.data.Config.ErrorCode.CODE_200
import vn.geekup.app.data.Config.ErrorCode.CODE_201
import vn.geekup.app.data.Config.ErrorCode.CODE_302
import vn.geekup.app.data.Config.ErrorCode.CODE_999
import vn.geekup.app.data.Config.SharePreference.KEY_AUTH_REFRESH_TOKEN
import vn.geekup.app.data.Config.SharePreference.KEY_AUTH_TOKEN
import vn.geekup.app.data.local.PreferenceWrapper
import vn.geekup.app.data.model.general.BaseResponseVO
import vn.geekup.app.data.model.general.MetaData
import vn.geekup.app.data.remote.auth.AliaApiService
import vn.geekup.app.data.remote.auth.AuthApiService
import vn.geekup.app.domain.dto.RefreshTokenBody
import vn.geekup.app.domain.model.user.TokenModel
import vn.geekup.app.domain.throwable.ServerErrorException
import javax.inject.Inject

class MiddleWareService @Inject constructor(
    val aliaApiService: AliaApiService,
    private val authApiService: AuthApiService,
    private val preferenceWrapper: PreferenceWrapper
) {

    fun <VO> requestSingleApi(
        requestFunc: Single<BaseResponseVO<VO>>,
        flatMapFunc: (BaseResponseVO<VO>) -> Single<Any>
    ): Single<Any> {
        return requestFunc.flatMap {
            if (it.meta?.statusCode != CODE_200 && it.meta?.statusCode != CODE_201) {
                Single.error(ServerErrorException(it.meta?.statusCode, it.meta?.message))
            } else {
                flatMapFunc.invoke(it)
            }
        }.onErrorResumeNext {
            if (it is HttpException) {
                val errorBody = it.response()?.errorBody()?.string()
                if (getMetaDataObj(errorBody).statusCode == Config.ErrorCode.CODE_401) {
                    return@onErrorResumeNext this.refreshToken().flatMap {
                        return@flatMap requestFunc
                    }
                } else {
                   return@onErrorResumeNext Single.error(ServerErrorException(getMetaDataObj(errorBody).statusCode, getMetaDataObj(errorBody).message))
                }
            }
            return@onErrorResumeNext Single.error(ServerErrorException(CODE_302, it.message))
        }
    }

    fun <VO> requestObservableApi(
        requestFunc: Observable<BaseResponseVO<VO>>,
        flatMapFunc: (BaseResponseVO<VO>) -> Observable<Any>
    ): Observable<Any> {
        return requestFunc.flatMap {
            if (it.meta?.statusCode != CODE_200 && it.meta?.statusCode != CODE_201) {
                Observable.error(ServerErrorException(it.meta?.statusCode, it.meta?.message))
            } else {
                flatMapFunc.invoke(it)
            }
        }.onErrorResumeNext {
            if (it is HttpException) {
                val errorBody = it.response()?.errorBody()?.string()
                if (getMetaDataObj(errorBody).statusCode == Config.ErrorCode.CODE_401) {
                    return@onErrorResumeNext this.refreshToken().flatMapObservable {
                        return@flatMapObservable requestFunc
                    }
                } else {
                    return@onErrorResumeNext Observable.error(
                        ServerErrorException(
                            getMetaDataObj(
                                errorBody
                            ).statusCode, getMetaDataObj(errorBody).message
                        )
                    )
                }
            }
            return@onErrorResumeNext Observable.never()
        }
    }

    fun requestCompletableApi(
        requestFunc: Completable,
    ): Completable {
        return requestFunc.onErrorResumeNext {
            if (it is HttpException) {
                val errorBody = it.response()?.errorBody()?.string()
                if (getMetaDataObj(errorBody).statusCode == Config.ErrorCode.CODE_401) {
                    return@onErrorResumeNext this.refreshToken().flatMapCompletable {
                        return@flatMapCompletable requestFunc
                    }
                } else {
                    return@onErrorResumeNext Completable.error(
                        ServerErrorException(
                            getMetaDataObj(
                                errorBody
                            ).statusCode, getMetaDataObj(errorBody).message
                        )
                    )
                }
            }
            return@onErrorResumeNext Completable.complete()
        }
    }

    private fun refreshToken(): Single<TokenModel> {
        val token = preferenceWrapper.getString(KEY_AUTH_REFRESH_TOKEN)
        return authApiService.refreshToken(RefreshTokenBody(refreshToken = token)).flatMap {
            if (it.meta?.statusCode != CODE_200) {
                Single.error(ServerErrorException(CODE_999, it.meta?.message))
            } else {
                preferenceWrapper.saveString(KEY_AUTH_TOKEN, it.data?.token ?: "")
                Single.just(it.data?.vo2Model() ?: TokenModel())
            }.onErrorResumeNext {
                return@onErrorResumeNext Single.error(
                    ServerErrorException(
                        CODE_999,
                        "Refresh Token Failure"
                    )
                )
            }
        }
    }

    private fun getMetaDataObj(errorBody: String? = ""): MetaData {
        return Gson().fromJson(errorBody, BaseResponseVO::class.java).meta ?: MetaData()
    }

}
