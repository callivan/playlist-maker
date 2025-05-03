package com.example.playlistmaker.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomGridLayoutItemDecoration {
    companion object {
        fun createGridSpacingItemDecoration(
            spanCount: Int,
            columnSpacing: Int,
            rowSpacing: Int,
        ): RecyclerView.ItemDecoration =
            GridSpacingItemDecoration(
                spanCount = spanCount,
                columnSpacing = columnSpacing,
                rowSpacing = rowSpacing
            )
    }
}

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val columnSpacing: Int,
    private val rowSpacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * columnSpacing / spanCount
        outRect.right = columnSpacing - (column + 1) * columnSpacing / spanCount

        if (position >= spanCount) {
            outRect.top = rowSpacing
        }
    }
}