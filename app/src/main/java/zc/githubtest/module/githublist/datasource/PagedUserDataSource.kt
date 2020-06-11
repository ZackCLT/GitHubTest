package zc.githubtest.module.githublist.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException
import zc.githubtest.module.githublist.data.UserData
import zc.githubtest.module.networkservice.NetworkService
import zc.githubtest.module.networkservice.NetworkState
import java.io.IOException

class PagedUserDataSource(
    private val pageSize: Int,
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, UserData>() {

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    private var retry: (() -> Any)? = null

    fun executeRetry() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            networkService.networkExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UserData>
    ) {
        initialLoad.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(networkService.getUsers(perPage = pageSize)
                .doOnError {
                    when (it) {
                        is HttpException -> {
                            initialLoad.postValue(NetworkState.error("error code: ${it.code()}"))
                            networkState.postValue(NetworkState.error("error code: ${it.code()}"))
                            retry = {
                                loadInitial(params, callback)
                            }
                        }
                    }
                }.subscribeBy(
                    onSuccess = { cells ->

                        val nextId = if (cells.isNotEmpty()) {
                            cells[cells.lastIndex].id
                        } else {
                            0
                        }
                        callback.onResult(cells, null, nextId)
                        retry = null
                        networkState.postValue(NetworkState.LOADED)
                        initialLoad.postValue(NetworkState.LOADED)
                    },
                    onError = {
                    }
                ))
        } catch (ioException: IOException) {
            initialLoad.postValue(NetworkState.error(ioException.message))
            networkState.postValue(NetworkState.error(ioException.message))
            retry = {
                loadInitial(params, callback)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UserData>
    ) {
        val id = params.key
        networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(networkService.getUsers(since = id, perPage = pageSize)
                .doOnError {
                    when (it) {
                        is HttpException -> {
                            networkState.postValue(NetworkState.error("error code: ${it.code()}"))
                            retry = {
                                loadAfter(params, callback)
                            }
                        }
                    }
                }.subscribeBy(
                    onSuccess = { cells ->
                        val nextId = if (cells.isNotEmpty()) {
                            cells[cells.lastIndex].id
                        } else {
                            0
                        }
                        callback.onResult(cells, nextId)
                        retry = null
                        networkState.postValue(NetworkState.LOADED)
                    },
                    onError = {
                    }
                ))
        } catch (ioException: IOException) {
            networkState.postValue(NetworkState.error(ioException.message))
            retry = {
                loadAfter(params, callback)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UserData>
    ) {
        // no need
    }
}