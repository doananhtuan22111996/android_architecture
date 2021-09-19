package vn.geekup.app.module.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentLoginBinding
import vn.geekup.app.domain.throwable.ServerErrorException
import vn.geekup.app.module.root.RootActivity
import vn.geekup.app.utils.*

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(),
    RootActivity.OnBackPressListener {

    override val viewModel: LoginViewModel by viewModels()

    override fun provideViewBinding(parent: ViewGroup): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        (baseActivity as? RootActivity)?.setOnBackPressListener(this)
        baseActivity.setAppColorStatusBar()
        fragmentBinding.fragment = this
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.login.observe(this, {
            Timber.d("Start Main Fragment")
            if (it) {
                (baseActivity as? RootActivity)?.redirectMain()
            }
        })

        viewModel.isLoading.observe(this, {
            fragmentBinding.vLoading.root.visible(it)
        })

    }

    override fun handleServerErrorState(serverErrorException: ServerErrorException) {
        super.handleServerErrorState(serverErrorException)
        fragmentBinding.btnLoginOTable.visible(true)
    }

    override fun onBackPress() {
        if (fragmentBinding.wvLogin.isVisible) {
            redirectLoginOTable(false)
            viewModel.setIsLoading(false)
            return
        }
        baseActivity.finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webView = fragmentBinding.wvLogin
        webView.settings.javaScriptEnabled = true // enable javascript
        webView.webViewClient = object : WebViewOTableClient() {
            override fun setIsLoading(isLoading: Boolean) {
                viewModel.setIsLoading(isLoading)
            }

            override fun loginSuccess(otableToken: String) {
                requestLoginOTable(otableToken)
                redirectLoginOTable(false)
            }
        }
        webView.loadUrl("${vn.geekup.app.data.BuildConfig.MAIN_DOMAIN}${vn.geekup.app.data.BuildConfig.OTABLE_ENDPOINT}")
    }

    fun onClickLoginOTable() {
        redirectLoginOTable(true)
        initWebView()
    }

    private fun requestLoginOTable(otableToken: String) {
        viewModel.loginOTable(otableToken)
    }

    private fun redirectLoginOTable(isOTable: Boolean) {
        if (isOTable) {
            baseActivity.setAppColorStatusBar(R.color.color_white)
            fragmentBinding.btnLoginOTable.visible(false)
            fragmentBinding.wvLogin.visible(true)
        } else {
            baseActivity.setAppColorStatusBar()
            fragmentBinding.btnLoginOTable.visible(true)
            fragmentBinding.wvLogin.visible(false)
        }

    }


}