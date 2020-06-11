package zc.githubtest.module.githublist.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import zc.githubtest.module.networkservice.NetworkState

data class RepoResultData(

    val pagedList: LiveData<PagedList<UserData>>,

    val networkState: LiveData<NetworkState>,

    val initLoadState: LiveData<NetworkState>,

    val retry: () -> Unit
)