package uk.co.wardone.beaker.modal.api.exchange.shapeshift;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import uk.co.wardone.beaker.modal.api.exchange.shapeshift.data.Market;

public interface ShapeShiftService {

    @GET("marketinfo/{pair}")
    Call<Market> getMarket(@Path("pair") String pair);

    @GET("/validpairs")
    Call<List<String>> getValidPairs();

    static ShapeShiftService get(){

        return new Retrofit.Builder()
                .baseUrl("https://www.ShapeShift.io/")
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build()
                .create(ShapeShiftService.class);

    }

}
