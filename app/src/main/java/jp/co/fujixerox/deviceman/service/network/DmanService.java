package jp.co.fujixerox.deviceman.service.network;

import jp.co.fujixerox.deviceman.service.dto.Device;
import jp.co.fujixerox.deviceman.service.dto.User;
import jp.co.fujixerox.deviceman.service.dto.UserList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DmanService {
    @GET("torica/api/users/")
    Call<UserList> getUsers();

    @GET("torica/api/users/{user_id}")
    Call<User> getUser(@Path("user_id") String userId);

    @GET("torica/api/devices/{device_id}")
    Call<Device> getDevice(@Path("device_id") String deviceId);

    @POST("torica/api/devices/{device_id}/checkout")
    Call<Void> checkout(
            @Path("device_id") String deviceId,
            @Query("user_id") String userId,
            @Query("due_return_time") long dueReturnTime);

    @POST("torica/api/devices/{device_id}/return")
    Call<Void> returnDevice(
            @Path("device_id") String deviceId,
            @Query("user_id") String userId);
}
