package app.extra_clases;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataService {

    @POST("login")
    Call<Modelo> login(@Body Modelo modelo);

    @POST("clases")
    Call<Modelo> agregar(@Body Modelo modelo);

    @POST("register")
    Call<Modelo> register(@Body Modelo modelo);

    @GET("clases")
    Call<Modelo> getClases();

    @PUT("clases/update/{id}")
    Call<Modelo> update(@Path("id") int id, @Body Modelo modelo);

    @PUT("clases/del/{id}")
    Call<Modelo> eliminar(@Path("id") int id);









}
