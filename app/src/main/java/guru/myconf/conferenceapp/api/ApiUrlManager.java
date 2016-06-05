package guru.myconf.conferenceapp.api;

import guru.myconf.conferenceapp.pojos.Request.LoginRequest;
import guru.myconf.conferenceapp.pojos.Request.PostCommentRequest;
import guru.myconf.conferenceapp.pojos.Request.RegistrationRequest;
import guru.myconf.conferenceapp.pojos.Response.ConferenceCommentsResponse;
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
    Call<ConferenceInfoResponse> getConferenceInfo(@Path("id") int conferenceId);

    @GET("conferences/{id}/comments")
    Call<ConferenceCommentsResponse> getConferenceComments(@Path("id") int conferenceId);

    @POST("conferences/{id}/comments")
    Call<BasicResponse> addComment(@Path("id") int conferenceId, @Body PostCommentRequest postCommentRequest);
}
