package zc.githubtest.module.githublist.datasource

import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import zc.githubtest.module.githublist.data.RepoResultData
import zc.githubtest.module.networkservice.NetworkService

class UserRepo(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) {

    fun getUsers(): RepoResultData {

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setPrefetchDistance(10)
            .build()

        val factory = UserDataSourceFactory(config.pageSize, networkService, compositeDisposable)

        val livePagedList = LivePagedListBuilder(factory, config).build()

        return RepoResultData(
            pagedList = livePagedList,
            networkState = factory.dataSource.switchMap { it.networkState },
            retry = { factory.dataSource.value?.executeRetry() },
            initLoadState = factory.dataSource.switchMap { it.initialLoad }
        )
    }
}