package guru.myconf.conferenceapp.api;

import guru.myconf.conferenceapp.pojos.Request.LoginRequest;
import guru.myconf.conferenceapp.pojos.Request.RegistrationRequest;
import guru.myconf.conferenceapp.pojos.Response.LoginResponse;
import guru.myconf.conferenceapp.pojos.Response.BasicResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface ApiUrlManager {
    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("signup")
    Call<BasicResponse> userRegister(@Body RegistrationRequest registrationRequest);
}
