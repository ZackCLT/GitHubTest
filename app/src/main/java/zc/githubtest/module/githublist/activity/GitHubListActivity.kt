package zc.githubtest.module.githublist.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import zc.githubtest.R
import zc.githubtest.databinding.ActivityGithubListBinding
import zc.githubtest.module.githublist.adapter.UserPagedListAdapter
import zc.githubtest.module.githublist.data.UserData
import zc.githubtest.module.githublist.dialog.UserInfoDialog
import zc.githubtest.module.githublist.viewmodel.GitHubListViewModel
import zc.githubtest.module.networkservice.NetworkState
import zc.githubtest.module.networkservice.Status

class GitHubListActivity : AppCompatActivity() {

    private val gitHubListViewModel: GitHubListViewModel by viewModel()
    private var userDialog: DialogFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityGithubListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_github_list)
        binding.lifecycleOwner = this

        val adapter = UserPagedListAdapter(gitHubListViewModel)
        binding.listView.adapter = adapter
        gitHubListViewModel.pagedList.observe(this, Observer {
            adapter.submitList(it)
        })

        gitHubListViewModel.initLoadState.observe(this, Observer { state ->
            binding.loadingView.visibility =
                if (state == NetworkState.LOADING) View.VISIBLE else View.GONE
        })

        gitHubListViewModel.networkState.observe(this, Observer { state ->
            if (state.status == Status.FAILED) showToast(state.msg)
        })

        gitHubListViewModel.focusUser.observe(this, Observer { data ->
            showUserDialog(data)
        })

        gitHubListViewModel.getGitHubUsers()
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg ?: "", Toast.LENGTH_LONG).show()
    }

    private fun showUserDialog(data: UserData) {
        if (userDialog != null) {
            return
        }
        supportFragmentManager.let { manager ->
            userDialog = UserInfoDialog.newInstance(data).apply {
                onDismissListener = DialogInterface.OnDismissListener {
                    userDialog = null
                }
            }
            val transaction = manager.beginTransaction()
            transaction.add(userDialog!!, "UserDialog")
            try {
                transaction.commitAllowingStateLoss()
                manager.executePendingTransactions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}