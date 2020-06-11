package zc.githubtest.di

import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import zc.githubtest.module.githublist.datasource.UserRepo
import zc.githubtest.module.githublist.viewmodel.GitHubListViewModel
import zc.githubtest.module.networkservice.NetworkService

val gitHubListModule = module {

    single {
        NetworkService()
    }

    viewModel {
        GitHubListViewModel(get())
    }
}