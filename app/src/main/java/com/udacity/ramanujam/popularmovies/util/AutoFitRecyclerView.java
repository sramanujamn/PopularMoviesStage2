package com.udacity.ramanujam.popularmovies.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class AutoFitRecyclerView extends RecyclerView {

    private GridLayoutManager gridLayoutManager;
    private int columnWidth = -1;

    public AutoFitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutoFitRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoFitRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        if(attributeSet != null) {
            int[] attrsArray = { android.R.attr.columnWidth };
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, attrsArray);
            columnWidth = typedArray.getDimensionPixelSize(0, -1);
            typedArray.recycle();
        }

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if(columnWidth > 0) {
            int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
            gridLayoutManager.setSpanCount(spanCount);
        }
    }
}
