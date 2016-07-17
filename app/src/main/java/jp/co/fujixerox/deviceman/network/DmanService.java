package jp.co.fujixerox.deviceman.network;

import jp.co.fujixerox.deviceman.dto.Device;
import jp.co.fujixerox.deviceman.dto.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public interface DmanService {
    @GET("api/users/{user_id}")
    Call<User> getUser(@Path("user_id") String userId);

    @GET("api/devices/{device_id}")
    Call<Device> getDevice(@Path("device_id") String deviceId);

    @POST("api/devices/{device_id}/checkout")
    Call<Void> checkout(
            @Path("device_id") String deviceId,
            @Query("user_id") String userId,
            @Query("due_return_time") long dueReturnTime);

    @PUT("api/devices/{device_id}/checkout")
    Call<Void> updateCheckout(
            @Path("device_id") String deviceId,
            @Query("user_id") String userId,
            @Query("due_return_time") long dueReturnTime);

    @POST("api/devices/{device_id}/return")
    Call<Void> returnDevice(
            @Path("device_id") String deviceId,
            @Query("user_id") String userId);
}
