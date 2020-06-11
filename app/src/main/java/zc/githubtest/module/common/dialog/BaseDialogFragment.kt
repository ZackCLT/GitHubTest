package zc.githubtest.module.common.dialog

import android.content.DialogInterface
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment : DialogFragment() {

    var onDismissListener: DialogInterface.OnDismissListener? = null

    override fun onDismiss(dialog: DialogInterface) {
        try {
            super.onDismiss(dialog)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            onDismissListener?.onDismiss(dialog)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isShowing(): Boolean? {
        if (dialog != null) {
            return dialog!!.isShowing
        }
        return false
    }
}