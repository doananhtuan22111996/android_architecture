package vn.geekup.app.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 10

    // The current offset index of data you have loaded
    private var currentPage = 1

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private var startingPageIndex = 1


    private var isGroupedRecyclerView = false

    fun setGroupedRecyclerView(groupedRecyclerView: Boolean) {
        isGroupedRecyclerView = groupedRecyclerView
    }

    constructor(layoutManager: LinearLayoutManager, visibleThreshold : Int = 10) {
        this.mLayoutManager = layoutManager
        this.visibleThreshold = visibleThreshold
    }

    constructor(visibleThreshold: Int, layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        this.visibleThreshold = visibleThreshold * layoutManager.spanCount
    }

    constructor(visibleThreshold: Int, layoutManager: StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        this.visibleThreshold = visibleThreshold * layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        var firstVisibleItemPosition = 0
        val totalItemCount = mLayoutManager!!.itemCount
        onScroll(dx, dy)
        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition =
                    (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                firstVisibleItemPosition =
                    (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition =
                    (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
                firstVisibleItemPosition =
                    (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
        }
        onScrolled(lastVisibleItemPosition, firstVisibleItemPosition)

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        if (isGroupedRecyclerView) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            loading = true
        } else {
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            // Log.d("Total: " + totalItemCount);
            //Log.d("Prev: " + previousTotalItemCount);
            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false
                previousTotalItemCount = totalItemCount
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++
                onLoadMore(currentPage, totalItemCount)
                loading = true
            }
        }
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    open fun onScroll(x: Int, y: Int) {}

    // Define listener for scrolling list
    open fun onScrolled(lastVisibleItemPosition: Int, firstVisibleItemPosition: Int) {}

    fun reset() {
        visibleThreshold = 10
        currentPage = 1
        previousTotalItemCount = 0
        loading = true
        startingPageIndex = 1
    }

    // Call this method whenever performing new searches
    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    fun setVisibleThreshold(visibleThreshold: Int) {
        this.visibleThreshold = visibleThreshold
    }
}