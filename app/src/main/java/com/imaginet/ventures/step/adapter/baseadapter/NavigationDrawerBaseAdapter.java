package com.imaginet.ventures.step.adapter.baseadapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imaginet.ventures.step.R;

/**
 * Created by IM028 on 9/7/16.
 */
public class NavigationDrawerBaseAdapter extends BaseAdapter {
    private Activity activity;
    private String[] list = {"Dashboard",
            "Progress",
            "Profile",
            "Settings",
            "About STEP",
            "Logout"};
    private int[] imageList = {R.drawable.menu_dashboard,
            R.drawable.menu_progression,
            R.drawable.menu_profile,
            R.drawable.menu_settings,
            R.drawable.menu_info,
            R.drawable.logout_icon
    };

    public NavigationDrawerBaseAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i-1].toString();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = activity.getLayoutInflater().inflate(R.layout.item_list_navigation_list_view, viewGroup, false);

        TextView textView = (TextView) view.findViewById(R.id.itemListNavigationListViewTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.itemListNavigationListViewImageView);
        textView.setText(list[i]);
        imageView.setImageResource(imageList[i]);
        return view;
    }
}
