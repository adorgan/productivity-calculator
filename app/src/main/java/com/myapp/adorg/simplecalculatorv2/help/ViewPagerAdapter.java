package com.myapp.adorg.simplecalculatorv2.help;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new MainHelpFragment(); //ChildFragment1 at position 0
            case 1:
                return new SecondHelpFragment(); //ChildFragment2 at position 1
            case 2:
                return new ThirdHelpFrag(); //ChildFragment3 at position 2
            case 3:
                return new FourthHelpFrag(); //ChildFragment1 at position 0
            case 4:
                return new FifthHelpFrag(); //ChildFragment2 at position 1
            case 5:
                return new SixthHelpFrag(); //ChildFragment3 at position 2
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Main"; //ChildFragment1 at position 0
            case 1:
                return "Time Card"; //ChildFragment2 at position 1
            case 2:
                return "Saved"; //ChildFragment3 at position 2
            case 3:
                return "Stats"; //ChildFragment1 at position 0
            case 4:
                return "Edit"; //ChildFragment2 at position 1
            case 5:
                return "Save"; //ChildFragment3 at position 2
        }
        return null; //does not happen;
    }
}
