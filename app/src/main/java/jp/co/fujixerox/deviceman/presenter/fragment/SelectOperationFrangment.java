//package jp.co.fujixerox.deviceman.presenter.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import jp.co.fujixerox.deviceman.R;
//import jp.co.fujixerox.deviceman.service.dto.User;
//import jp.co.fujixerox.deviceman.presenter.activity.QRScanActivity;
//
//public class SelectOperationFrangment extends BaseFragment {
//    public interface ReqestHnadler {
//
//    }
//
//
//    private static final String TAG = SelectOperationFrangment.class.getName();
//
//    private static final String BUNDLE_PARAM_USER = "bundle_param_user";
//
//    private static final int REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID = 9002;
//    private static final int REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID = 9003;
//
//    public static String EXTRA_KEY_USER = "user";
//
//    @BindView(R.id.text_user_id)
//    TextView userIdText;
//
//    @BindView(R.id.button_lending)
//    Button lendButton;
//
//    @BindView(R.id.button_return)
//    Button returnButton;
//
//    @BindView(R.id.button_finish)
//    Button finishButton;
//
//    /**
//     * Goes to the user list screen.
//     */
//    @OnClick(R.id.button_lending)
//    void handleClickLendingButton() {
//        Intent intent = new Intent(getActivity(), QRScanActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID);
//    }
//
//    @OnClick(R.id.button_return)
//    void handleClick() {
//        Intent intent = new Intent(getActivity(), QRScanActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID);
//    }
//
//    @OnClick(R.id.button_finish)
//    void handleClickFisish() {
//        getActivity().finish();
//    }
//
//    private User mUser;
//
//    private Unbinder mUnbinder;
//
//
//    public SelectOperationFrangment() {
//    }
//
//    public static SelectOperationFrangment newInstance(User user) {
//        final SelectOperationFrangment frangment = new SelectOperationFrangment();
//        final Bundle arguments = new Bundle();
//        arguments.putSerializable(BUNDLE_PARAM_USER, user);
//        frangment.setArguments(arguments);
//
//        return frangment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        userIdText.setText(mUser.getName());
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_select_operation, container, false);
//        mUnbinder = ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (savedInstanceState == null) {
//            mUser = (User) getArguments().getSerializable(BUNDLE_PARAM_USER);
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mUnbinder.unbind();
//    }
//}
