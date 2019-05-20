import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmetList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public void addFragment (Fragment fragment,String string){
        mFragmetList.add(fragment);
        mFragmentTitleList.add(string);

    }

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmetList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmetList.size();
    }
}
