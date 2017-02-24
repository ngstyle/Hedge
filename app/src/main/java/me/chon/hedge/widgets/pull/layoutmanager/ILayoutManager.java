package me.chon.hedge.widgets.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;

import me.chon.hedge.widgets.pull.BaseListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
