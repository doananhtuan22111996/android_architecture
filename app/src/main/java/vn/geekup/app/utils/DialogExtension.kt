package vn.geekup.app.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes

fun Context.showAlert(
  title: String? = null, message: String,
  titleOk: String? = null, titleCancel: String? = null, titleNeutral: String? = null,
  action: DialogInterface.OnClickListener? = null, isCancelable: Boolean = true
) {
//  val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog)
//  title?.let { builder.setTitle(title) }
//  builder.setMessage(message)
//  builder.setCancelable(isCancelable)
//  builder.setPositiveButton(
//    if (TextUtils.isEmpty(titleOk)) getString(R.string.action_ok) else titleOk,
//    action
//      ?: DialogInterface.OnClickListener { dialog: DialogInterface, _: Int -> dialog.dismiss() })
//  titleCancel?.let { builder.setNegativeButton(it, action) }
//  titleNeutral?.let { builder.setNeutralButton(titleNeutral, action) }
//  val dialog = builder.show()
//  dialog.getButton(DialogInterface.BUTTON_POSITIVE).isAllCaps = true
//  titleCancel?.let { dialog.getButton(DialogInterface.BUTTON_NEGATIVE).isAllCaps = true }
//  titleNeutral?.let { dialog.getButton(DialogInterface.BUTTON_NEUTRAL).isAllCaps = true }
}

fun Context.initialDialog(@LayoutRes layout: Int, isCancelable: Boolean): Dialog {
  val dialog = Dialog(this)
  dialog.window?.apply {
    setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
  }
  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
  dialog.setContentView(layout)
  dialog.setCancelable(isCancelable)
  return dialog
}
