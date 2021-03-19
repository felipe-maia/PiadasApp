package com.example.piadas;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PiadaService {

    @GET("{cod}/info.0.json")
    Call<PiadaModel> buscarPiada(@Path("cod") String cod);
}
