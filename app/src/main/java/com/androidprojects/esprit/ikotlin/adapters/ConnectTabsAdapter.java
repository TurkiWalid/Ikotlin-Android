package com.androidprojects.esprit.ikotlin.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.utils.MenuBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import q.rorbin.verticaltablayout.TabAdapter;
import q.rorbin.verticaltablayout.widget.QTabView;

/**
 * Created by Amal on 03/12/2017.
 */

public class ConnectTabsAdapter extends PagerAdapter implements TabAdapter {
    List<MenuBean> menus;
    Context context;

    public ConnectTabsAdapter(Context context) {
        this.context=context;
        menus = new ArrayList<>();
        Collections.addAll(menus
                , new MenuBean(R.drawable.ic_shareapp, R.drawable.ic_shareapp_unselected, null)
                , new MenuBean(R.drawable.ic_rateus, R.drawable.ic_rateus_unselected, null),
                new MenuBean(R.drawable.ic_aboutus, R.drawable.ic_aboutus_unselected, null),
                new MenuBean(R.drawable.ic_faq, R.drawable.ic_faq_unselected, null));
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getBadge(int position) {
        return 0;
    }

    @Override
    public QTabView.TabIcon getIcon(int position) {
        MenuBean menu = menus.get(position);
        return new QTabView.TabIcon.Builder()
                .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                .build();
    }

    @Override
    public QTabView.TabTitle getTitle(int position) {
        return new QTabView.TabTitle.Builder(context)
                .setContent("")
                .setTextColor(0xFF36BC9B, 0xFF757575)
                .build();
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        /** this adapter returns the connect_fragments_root empty layout
         * which content is set when we instantiate the tabLyout in connect fragment
         * depending on tab position ..
         * ( check connect fragment )
         */
        View view= LayoutInflater.from(context).inflate(R.layout.connect_fragments_root, null, false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}