package com.madhubasavanna.knowme.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.madhubasavanna.knowme.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.wifi_layout)
    LinearLayout wifiLayout;
    @BindView(R.id.mobile_layout)
    LinearLayout mobileLayout;
    @BindView(R.id.user_name_text_view)
    TextView userName;
    @BindView(R.id.favourite_list_text_view)
    TextView favouriteList;
    @BindView(R.id.setting_text_view)
    TextView settings_text_view;
    @BindView(R.id.media_quality)
    TextView mediaQuality;
    @BindView(R.id.wifi_media_quality_text_view)
    TextView wifiMediaQualityTextView;
    @BindView(R.id.wifi_media_quality_spinner)
    Spinner wifiMediaQualitySpinner;
    @BindView(R.id.mobile_data_media_quality_text_view)
    TextView mobileDataMediaQualityTextView;
    @BindView(R.id.mobile_data_media_quality_spinner)
    Spinner mobileDataMediaQualitySpinner;
    @BindView(R.id.help_text_view)
    TextView helpTextView;
    @BindView(R.id.about_us_text_view)
    TextView aboutUsTextView;
    SharedPreferences settingSharedPreference;
    SharedPreferences.Editor editor;
    static String SHARED_PREFERENCE_NAME;
    static String WIFI_VIDEO_QUALITY;
    static String MOBILE_DATA_VIDEO_Quality;
    ArrayAdapter<CharSequence> mobileadapter;
    ArrayAdapter<CharSequence> wifiadapter;
    int check = 0;

    @OnClick({R.id.setting_text_view, R.id.settings_dropdown_image_view})
    void displaySettings(){
        if(!mediaQuality.isShown()){
            mediaQuality.setVisibility(View.VISIBLE);
            wifiLayout.setVisibility(View.VISIBLE);
            mobileLayout.setVisibility(View.VISIBLE);
        }else {
            mediaQuality.setVisibility(View.INVISIBLE);
            wifiLayout.setVisibility(View.INVISIBLE);
            mobileLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setUpSpinners();
    }



    private void setUpSpinners(){
        SHARED_PREFERENCE_NAME = getResources().getString(R.string.shared_preference);
        WIFI_VIDEO_QUALITY = getResources().getString(R.string.wifi_video_quality);
        MOBILE_DATA_VIDEO_Quality = getResources().getString(R.string.mobile_data_video_quality);
        settingSharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Create an ArrayAdapter using the string array and a default spinner layout
        wifiadapter = ArrayAdapter.createFromResource(this,
                R.array.media_quality_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        wifiadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        wifiMediaQualitySpinner.setAdapter(wifiadapter);
        //set the default value to spinner
        if (settingSharedPreference.getString(WIFI_VIDEO_QUALITY, "144p") != null) {
            int spinnerPosition = wifiadapter.getPosition(settingSharedPreference.getString(WIFI_VIDEO_QUALITY, "144p"));
            wifiMediaQualitySpinner.setSelection(spinnerPosition);
        }
        wifiMediaQualitySpinner.setOnItemSelectedListener(this);

        mobileadapter = ArrayAdapter.createFromResource(this,
                R.array.media_quality_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        mobileadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mobileDataMediaQualitySpinner.setAdapter(mobileadapter);
        //set the default value to spinner
        if (settingSharedPreference.getString(MOBILE_DATA_VIDEO_Quality, "144p") != null) {
            int spinnerPosition = mobileadapter.getPosition(settingSharedPreference.getString(MOBILE_DATA_VIDEO_Quality, "144p"));
            mobileDataMediaQualitySpinner.setSelection(spinnerPosition);
        }
        mobileDataMediaQualitySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(++check > 1){
            Object selectedVideoQuality = parent.getItemAtPosition(position);
            String choice = parent.getAdapter().equals(wifiadapter) ? "wifi": "mobile";
            switch(choice){
                case "wifi": editor = settingSharedPreference.edit();
                    editor.putString(WIFI_VIDEO_QUALITY, selectedVideoQuality.toString());
                    editor.commit();
                case "mobile": editor = settingSharedPreference.edit();
                    editor.putString(MOBILE_DATA_VIDEO_Quality, selectedVideoQuality.toString());
                    editor.commit();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
