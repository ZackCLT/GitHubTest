package zc.githubtest.module.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BasePagedListAdapter<T>(itemCallback: DiffUtil.ItemCallback<T>) :
    PagedListAdapter<T, BaseViewHolder>(itemCallback) {

    private val viewHolders: MutableList<BaseViewHolder> = mutableListOf()

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = createBinding(parent, viewType)
        val viewHolder = BaseViewHolder(binding)
        binding.lifecycleOwner = viewHolder
        viewHolder.markCreated()
        viewHolders.add(viewHolder)
        return viewHolder
    }

    final override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (position < itemCount) {
            bind(holder.binding, position)
            holder.binding.executePendingBindings()
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    private fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getBindingReference(parent, viewType),
            parent,
            false
        )
    }

    fun setLifecycleDestroyed() {
        viewHolders.forEach {
            it.markDestroyed()
        }
    }

    protected abstract fun bind(binding: ViewDataBinding, position: Int)
    protected abstract fun getBindingReference(parent: ViewGroup, viewType: Int): Int
}
