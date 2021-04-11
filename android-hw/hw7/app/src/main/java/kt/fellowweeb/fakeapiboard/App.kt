package kt.fellowweeb.fakeapiboard

import android.app.Application
import io.realm.Realm
import kt.fellowweeb.fakeapiboard.service.PostService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class App: Application() {
    lateinit var okHttpClient: OkHttpClient
        private set

    lateinit var retrofit: Retrofit
        private set

    lateinit var postService: PostService
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        okHttpClient = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        postService = retrofit.create(PostService::class.java)
        Realm.init(this)
    }

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        lateinit var instance: App
            private set
    }
}
