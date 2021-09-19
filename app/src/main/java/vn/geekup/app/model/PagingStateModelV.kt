package vn.geekup.app.model

sealed class PagingState {
    object LoadingMore : PagingState()
    object Loaded : PagingState()
}