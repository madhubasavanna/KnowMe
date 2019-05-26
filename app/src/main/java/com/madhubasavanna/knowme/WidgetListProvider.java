package com.madhubasavanna.knowme;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.madhubasavanna.knowme.userdata.UserPreferences;

import java.util.List;

class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    List<UserPreferences> list;
    public WidgetListProvider(Context applicationContext, List<UserPreferences> list) {
        this.context = applicationContext;
        this.list = list;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() { }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_item);
        remoteView.setTextViewText(R.id.widget_list_title, list.get(position).getTitle());
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
