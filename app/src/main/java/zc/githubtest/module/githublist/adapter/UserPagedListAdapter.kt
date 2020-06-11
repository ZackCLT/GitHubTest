package zc.githubtest.module.githublist.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import zc.githubtest.R
import zc.githubtest.databinding.ItemGithubBinding
import zc.githubtest.module.common.adapter.BasePagedListAdapter
import zc.githubtest.module.githublist.data.UserData
import zc.githubtest.module.githublist.viewmodel.GitHubListViewModel

class UserPagedListAdapter(private val viewModel: GitHubListViewModel) :
    BasePagedListAdapter<UserData>(itemCallback = object : DiffUtil.ItemCallback<UserData>() {
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean =
            oldItem == newItem
    }) {

    override fun getBindingReference(parent: ViewGroup, viewType: Int): Int {
        return R.layout.item_github
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val data = getItem(position)
        when(binding){
            is ItemGithubBinding -> {
                binding.viewModel = viewModel
                binding.userData = data
            }
        }
    }

}


