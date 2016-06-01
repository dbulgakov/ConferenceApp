package guru.myconf.conferenceapp.api;

import guru.myconf.conferenceapp.entities.Conference;
import guru.myconf.conferenceapp.pojos.Request.LoginRequest;
import guru.myconf.conferenceapp.pojos.Request.RegistrationRequest;
import guru.myconf.conferenceapp.pojos.Response.ConferenceInfoResponse;
import guru.myconf.conferenceapp.pojos.Response.ConferencesResponse;
import guru.myconf.conferenceapp.pojos.Response.LoginResponse;
import guru.myconf.conferenceapp.pojos.Response.BasicResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Path;

public interface ApiUrlManager {
    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("signup")
    Call<BasicResponse> userRegister(@Body RegistrationRequest registrationRequest);

    @GET("conferences")
    Call<ConferencesResponse> getConferences();

    @GET("conferences/{id}/")
    Call<ConferenceInfoResponse> getConferenceInfo(@Path("id") int groupId);
}
