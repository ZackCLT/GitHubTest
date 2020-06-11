package zc.githubtest.module.networkservice

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import zc.githubtest.module.githublist.data.UserData

interface GitHubApi {

    @GET("users")
    fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Single<List<UserData>>


    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: String): Single<UserData>
}