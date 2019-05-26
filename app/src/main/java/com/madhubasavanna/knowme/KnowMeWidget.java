package com.madhubasavanna.knowme;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.madhubasavanna.knowme.userdata.KnowMeDatabase;
import com.madhubasavanna.knowme.userdata.UserPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class KnowMeWidget extends AppWidgetProvider {
    public static List<UserPreferences> prefList = new ArrayList<>();
    public static boolean state = false;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        getPreferenceList(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.know_me_widget);
        if (prefList != null) {
            Intent intent = new Intent(context, KnowMeWidgetService.class);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widget_preference_list, intent);
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            views.setOnClickPendingIntent(R.id.widget_preference_list, pendingIntent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else {
            views.setTextViewText(R.id.default_message, "Select your Preference in the app");
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void getPreferenceList(Context context) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                state = true;
                setData(context);
            }
        });

        setData(context);
    }

    synchronized public static void setData(Context context){
        if(state){
            KnowMeDatabase db = KnowMeDatabase.getInstance(context);
            prefList = db.userPreferencesDao().loadPreferences();
            state = false;
        }
    }
}

