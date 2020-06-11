package zc.githubtest.module.githublist.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import zc.githubtest.module.githublist.data.UserData
import zc.githubtest.module.networkservice.NetworkService

class UserDataSourceFactory(
    private val pageSize: Int,
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, UserData>() {

    val dataSource = MutableLiveData<PagedUserDataSource>()

    override fun create(): DataSource<Int, UserData> {
        return PagedUserDataSource(pageSize, networkService, compositeDisposable).apply {
            dataSource.postValue(this)
        }
    }
}