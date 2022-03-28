package com.rafiky.connect.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created By Athul on 08-11-2019.
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public ItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        boolean isLast = position == state.getItemCount()-1;
        if(isLast){
            outRect.left = mItemOffset;
            outRect.right = 0; //don't forget about recycling...
        }
        if(position == 0){
            outRect.left = 0;
            // don't recycle bottom if first item is also last
            // should keep bottom padding set above
        }else if(!isLast)
            outRect.left = mItemOffset;
        //outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}