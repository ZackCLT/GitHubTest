package zc.githubtest

import androidx.multidex.MultiDexApplication
import org.koin.core.context.startKoin
import zc.githubtest.di.gitHubListModule

class GitHubTestApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(gitHubListModule))
        }
    }
}