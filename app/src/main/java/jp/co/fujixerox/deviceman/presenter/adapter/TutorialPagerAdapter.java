package jp.co.fujixerox.deviceman.presenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.presenter.fragment.TutorialPageFragment;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }


//    public TutorialPagerAdapter(Context context, List<View> views) {
//        mContext = context;
//        mView = views;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TutorialPageFragment.newInstance(R.layout.fragment_tutorial_1);
            case 1:
                return TutorialPageFragment.newInstance(R.layout.fragment_tutorial_2);
            case 2:
                return TutorialPageFragment.newInstance(R.layout.fragment_tutorial_3);
        }
        return null;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        // リストからページの背景色を取得します。
////        ImageView color = mColorList.get(position);
//        // page.xmlからレイアウトを作成しています。
////        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.page, null);
////        view.setBackgroundColor(color);
//        View view = mView.get(position);
//        container.addView(view);
//        return view;
//    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }

    @Override
    public int getCount() {
        return 3;
    }

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view.equals(object);
//    }
}