package zc.githubtest.module.githublist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import io.reactivex.rxkotlin.subscribeBy
import zc.githubtest.module.common.viewmodel.BaseViewModel
import zc.githubtest.module.githublist.data.RepoResultData
import zc.githubtest.module.githublist.data.UserData
import zc.githubtest.module.githublist.datasource.UserRepo
import zc.githubtest.module.networkservice.NetworkService
import zc.githubtest.module.networkservice.NetworkState

class GitHubListViewModel(private val networkService: NetworkService) : BaseViewModel() {

    private val userRepo = UserRepo(networkService, disposables)

    private val repoResult = MutableLiveData<RepoResultData>()

    private val _focusUser = MutableLiveData<UserData>()
    val focusUser: LiveData<UserData> = _focusUser

    val pagedList = repoResult.switchMap { it.pagedList }
    val networkState = repoResult.switchMap { it.networkState }
    val initLoadState = repoResult.switchMap { it.initLoadState }

    fun getGitHubUsers() {
        repoResult.postValue(userRepo.getUsers())
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }

    fun onClickUserInfo(userId: String) {
        rxLaunch {
            networkService.getUser(userId).subscribeBy(
                onSuccess = { user ->
                    _focusUser.postValue(user)
                },
                onError = {
                }
            )
        }
    }
}