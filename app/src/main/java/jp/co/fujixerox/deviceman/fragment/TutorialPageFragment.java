package jp.co.fujixerox.deviceman.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.fujixerox.deviceman.R;

public class TutorialPageFragment extends Fragment {
    public static String  ARGUMENT_KEY_LAYOUT_ID = "layout_id";

    public static TutorialPageFragment newInstance(@LayoutRes int layoutRes) {
        TutorialPageFragment fragment = new TutorialPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_KEY_LAYOUT_ID, layoutRes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getArguments().getInt(ARGUMENT_KEY_LAYOUT_ID), container, false);
        TextView titleTextView = (TextView)view.findViewById(R.id.tutorial_message);
        titleTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/light_novel_pop.ttf"));
        return view;
    }
}
