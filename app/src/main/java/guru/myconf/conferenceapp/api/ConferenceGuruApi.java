package guru.myconf.conferenceapp.api;

import guru.myconf.conferenceapp.pojos.Request.LoginRequest;
import guru.myconf.conferenceapp.pojos.Response.LoginResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface ConferenceGuruApi {
    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);
}
