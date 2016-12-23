package com.tuchangwei.yora.views;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuchangwei.yora.R;
import com.tuchangwei.yora.activities.BaseActivity;

import java.util.ArrayList;

/**
 * Created by vale on 1/24/16.
 */
public class NavDrawer {

    private ArrayList<NavDrawerItem> items;
    private NavDrawerItem selectedItem;

    protected BaseActivity activity;
    protected DrawerLayout drawerLayout;
    protected ViewGroup navDrawerView;

    public NavDrawer(BaseActivity activity) {
        this.activity = activity;
        items = new ArrayList<>();
        // drawer_layout ： Inbox , Send Message , Contacts  , Profile 各有一个 drawer_layout
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);  // 整个DrawerLayout
        navDrawerView = (ViewGroup)activity.findViewById(R.id.nav_drawer);  // 整个滑动出来的那个界面
        if (drawerLayout==null&&navDrawerView==null) {
            throw new RuntimeException("To use this class, you must have views with the ids of drawer_layout and nav_drawer");
        }
        Toolbar toolbar = activity.getToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOpen(!isOpen());
            }
        });
    }
    public boolean isOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }
    public void setOpen(boolean isOpen) {
        if (isOpen) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public  void addItem(NavDrawerItem item) {
        items.add(item);
        // 注意下面这个特殊的用法
        item.navDrawer = this;
    }
    public void setSelectedItem(NavDrawerItem item) {
//        设置老的被选择的对象为取消选择状态
        if (selectedItem != null) {
            selectedItem.setSelected(false);
        }
        selectedItem = item;
        selectedItem.setSelected(true);

    }
    public void create() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        for (NavDrawerItem item :items) {
            item.inflate(layoutInflater, navDrawerView);  // navDrawerView ： 整个滑动出来的那个界面
            Log.e("NavDrawer",item.getClass().toString());
        }
    }
    public static abstract class NavDrawerItem {
//        在 ViewGroup container 的某个地方Inflate自己，并且根据自己是还被选中，改变 Appearance
        protected NavDrawer navDrawer;
        public abstract void inflate(LayoutInflater inflater, ViewGroup container);
        public abstract void setSelected(boolean isSelected);
    }
    public static class BasicNavDrawerItem extends  NavDrawerItem implements View.OnClickListener {
//        显示 text 或者 badge (如果有的话) ,还有 icon

        private String text;
        private String badge;
        private int iconDrawabel;
        private int containerId; //DrawerLayout当中的位置，像是列表位置，比如：Logout 是在最后一个

        private ImageView icon;
        private TextView textView;
        private TextView badgeTextView;
        private View view;
        private int defaultTextColor;

        public BasicNavDrawerItem(String text, String badge, int iconDrawabel, int containerId) {
            this.text = text;
            this.badge = badge;
            this.iconDrawabel = iconDrawabel;
            this.containerId = containerId;
        }
        @Override
        // 参数 ViewGroup navDrawerView ： 整个滑动出来的那个界面
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView) {
            // container Id 这个参数传进来的是： include_main_nav_drawer_topItems
            // 下一句通过 containerId 把 container 定位在了放每个 Item 的位置上
            ViewGroup container = (ViewGroup) navDrawerView.findViewById(containerId);
            if (container==null)
                throw new RuntimeException("Nav drawer item " + text + " could not be attached to ViewGroup. View not found.");
            // 下一句，如果不加最后的false，返回的View是包括每个Item的整体。
            //加了false就是每个Item，所以得用addView添加到container里面。
            view = inflater.inflate(R.layout.list_item_nav_drawer, container,false);
            container.addView(view);

            view.setOnClickListener(this);

            icon = (ImageView)view.findViewById(R.id.list_item_nav_drawer_icon);
            textView = (TextView)view.findViewById(R.id.list_item_nav_drawer_text);
            badgeTextView = (TextView)view.findViewById(R.id.list_item_nav_drawer_badge);
            defaultTextColor = textView.getCurrentTextColor();

            icon.setImageResource(iconDrawabel);
            textView.setText(text);
            if (badge != null) {
                badgeTextView.setText(badge);
            } else {
                badgeTextView.setVisibility(View.GONE);
            }

        }

        @Override
        public void setSelected(boolean isSelected) {
            if (isSelected) {
                view.setBackgroundResource(R.drawable.list_item_nav_drawer_selected_item_background);
                textView.setTextColor(navDrawer.activity.getResources().getColor(R.color.list_item_nav_drawer_selected_item_text_color));
            } else {
                view.setBackground(null);
                textView.setTextColor(defaultTextColor);
            }
        }
        public void setText(String text) {
            // 下面这三个方法是用来 改变或者更新 元素的
            this.text = text;
            if (view != null) {
                textView.setText(text);
            }
        }
        public void setBadge(String badge) {
            this.badge = badge;
            if (view != null) {
                if (badge != null)
                    badgeTextView.setVisibility(View.VISIBLE);
                else
                    badgeTextView.setVisibility(View.GONE);
            }
        }

        public void setIcon(int iconDrawabel) {
            this.iconDrawabel = iconDrawabel;
            if (view != null) {
                icon.setImageResource(iconDrawabel);
            }
        }
        @Override
        public void onClick(View v) {
            navDrawer.setSelectedItem(this);
        }
    }
    public static class ActivityNavDrawerItem extends BasicNavDrawerItem {
        private final Class targetActivity;

        public ActivityNavDrawerItem(Class targetActivity, String text, String badge, int iconDrawabel, int containerId) {
            super(text,badge,iconDrawabel,containerId);
            this.targetActivity = targetActivity;
        }

        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView) {
            super.inflate(inflater, navDrawerView);
            if (this.navDrawer.activity.getClass() == targetActivity) {
                this.navDrawer.setSelectedItem(this);
            }
        }

        @Override
        public void onClick(View v) {
            navDrawer.setOpen(false);

            if (this.navDrawer.activity.getClass() == targetActivity) return;
            super.onClick(v);

            final BaseActivity activity = navDrawer.activity;
            activity.fadeOut(new BaseActivity.FadeOutListener() {
                @Override
                public void onFadeOutEnd() {

                    activity.startActivity(new Intent(activity,targetActivity));
                    activity.finish();
                }
            });
        }
    }
}
