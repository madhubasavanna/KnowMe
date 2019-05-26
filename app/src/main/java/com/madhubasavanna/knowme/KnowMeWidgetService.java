package com.madhubasavanna.knowme;

import android.content.Intent;
import android.widget.RemoteViewsService;

import static com.madhubasavanna.knowme.KnowMeWidget.prefList;
public class KnowMeWidgetService extends RemoteViewsService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetListProvider(this.getApplicationContext(), prefList));
    }
}
