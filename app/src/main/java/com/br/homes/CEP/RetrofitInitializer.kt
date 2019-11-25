package thiagocury.eti.br.exconsumindoviacepkotlinretrofit2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    fun init() {
    }

    val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun apiRetrofitService(): APIRetrofitService {
        return retrofit.create(APIRetrofitService::class.java)
    }

}