package zc.githubtest.module.githublist.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import zc.githubtest.R
import zc.githubtest.databinding.DialogUserInfoBinding
import zc.githubtest.module.common.dialog.BaseDialogFragment
import zc.githubtest.module.githublist.data.UserData

class UserInfoDialog : BaseDialogFragment() {

    companion object {
        fun newInstance(data: UserData): UserInfoDialog {
            return UserInfoDialog().apply {
                userData = data
            }
        }
    }

    private var userData: UserData? = null
    private var binding: DialogUserInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<DialogUserInfoBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_user_info,
            null,
            false
        ).also {
            it.lifecycleOwner = this
            it.userData = userData
            it.closeIconView.setOnClickListener {
                dismiss()
            }
        }
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.NormalDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            resources.displayMetrics?.let {
                var width = (it.widthPixels * 0.8).toInt()
                var height = (it.heightPixels * 0.75).toInt()
                window?.setLayout(width, height)
                window?.setGravity(Gravity.CENTER)
            }
        }
    }
}