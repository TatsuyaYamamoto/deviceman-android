package jp.co.fujixerox.deviceman.network;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import jp.co.fujixerox.deviceman.BuildConfig;
import jp.co.fujixerox.deviceman.dto.Device;
import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.dto.UserList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class Apiclient {
    private static final String TAG = Apiclient.class.getName();
    private static final String BASE_URL = BuildConfig.APP_SERVER_ORIGIN;

    public interface ResponseListener<T> {
        void onHttpSuccess(T body);

        void onHttpError(int code, T body);

        void failure();
    }

    @Inject
    Context mContext;

    /**
     * ユーザー情報をすべて取得する
     *
     * @param responseListener
     */
    public void getAllUser(final ResponseListener<UserList> responseListener) {

        Call<UserList> task = getService().getUsers();
        task.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.isSuccessful()) {
                    responseListener.onHttpSuccess(response.body());
                } else {
                    responseListener.onHttpError(response.code(), response.body());
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d(TAG, "couse: t" + t.getCause() + ", message: " + t.getMessage());

                responseListener.failure();
            }
        });
    }

    /**
     * ユーザー情報を取得する
     *
     * @param userId
     * @param responseListener
     */
    public void getUserById(String userId, final ResponseListener<User> responseListener) {

        Call<User> task = getService().getUser(userId);
        task.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 200:
                        responseListener.onHttpSuccess(response.body());
                        break;
                    default:
                        responseListener.onHttpError(response.code(), response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "couse: t" + t.getCause() + ", message: " + t.getMessage());

                responseListener.failure();
            }
        });
    }

    /**
     * 端末情報を取得する
     *
     * @param deviceId
     * @param responseListener
     */
    public void getDeviceId(String deviceId, final ResponseListener<Device> responseListener) {

        Call<Device> task = getService().getDevice(deviceId);

        final Device responseUser;

        task.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                switch (response.code()) {
                    case 200:
                        responseListener.onHttpSuccess(response.body());
                        break;
                    default:
                        responseListener.onHttpError(response.code(), response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.d(TAG, "couse: t" + t.getCause() + ", message: " + t.getMessage());

                responseListener.failure();
            }
        });
    }

    /**
     * 端末貸出処理を行う
     *
     * @param deviceId
     * @param userId
     * @param responseListener
     */
    public void checkout(String deviceId, String userId, long returnTime, final ResponseListener<Void> responseListener) {
        Call<Void> task = getService().checkout(deviceId, userId, returnTime);
        task.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        responseListener.onHttpSuccess(response.body());
                        break;
                    default:
                        responseListener.onHttpError(response.code(), response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responseListener.failure();
            }
        });
    }

    /**
     * 端末返却処理を行う
     *
     * @param deviceId
     * @param userId
     * @param responseListener
     */
    public void returnDevice(String deviceId, String userId, final ResponseListener<Void> responseListener) {
        Call<Void> task = getService().returnDevice(deviceId, userId);
        task.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        responseListener.onHttpSuccess(response.body());
                        break;
                    default:
                        responseListener.onHttpError(response.code(), response.body());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responseListener.failure();
            }
        });
    }


    private DmanService getService() {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                // ロギング
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(DmanService.class);
    }
}
