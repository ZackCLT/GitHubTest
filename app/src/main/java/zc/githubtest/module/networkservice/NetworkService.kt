package zc.githubtest.module.networkservice

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import zc.githubtest.module.githublist.data.UserData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NetworkService {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    val networkExecutor: Executor by lazy {
        Executors.newFixedThreadPool(1)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val gitHubApi: GitHubApi by lazy {
        retrofit.create(GitHubApi::class.java)
    }

    fun getUsers(since: Int = 0, perPage: Int = 20): Single<List<UserData>> {
        return gitHubApi.getUsers(since, perPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUser(userId: String): Single<UserData> {
        return gitHubApi.getUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}