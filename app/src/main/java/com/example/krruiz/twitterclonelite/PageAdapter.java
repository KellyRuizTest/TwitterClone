package com.example.krruiz.twitterclonelite;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.krruiz.twitterclonelite.Model.LikeFragment;
import com.example.krruiz.twitterclonelite.Model.MediaFragment;
import com.example.krruiz.twitterclonelite.Model.RepliesFragment;
import com.example.krruiz.twitterclonelite.Model.TweetsFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int numofTabs;
    private String id;

    public PageAdapter(@NonNull FragmentManager fm, int numofTabs, String id) {
        super(fm, numofTabs);
        this.numofTabs = numofTabs;
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TweetsFragment(id);
            case 1:
                return new RepliesFragment(id);

            case 2:
                return new MediaFragment(id);

            case 3:
                return new LikeFragment(id);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numofTabs;
    }
}
