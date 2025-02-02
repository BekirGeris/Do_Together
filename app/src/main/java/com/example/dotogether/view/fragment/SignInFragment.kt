package com.example.dotogether.view.fragment

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.dotogether.BuildConfig
import com.example.dotogether.R
import com.example.dotogether.data.callback.LoginCallback
import com.example.dotogether.databinding.FragmentSignInBinding
import com.example.dotogether.model.request.ForgetPasswordRequest
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.util.SharedPreferencesUtil
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(), View.OnClickListener, LoginCallback {

    private val REQ_ONE_TAP = 1
    private var signInType: Int = 0

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignInBinding

    private var email: String = ""
    private var password: String = ""

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        dialog.hide()
        Log.d(TAG, "resultCode : ${it.resultCode}")
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data

            when(signInType) {
                REQ_ONE_TAP -> {
                    try {
                        val credential = oneTapClient.getSignInCredentialFromIntent(data)
                        val idToken = credential.googleIdToken
                        val email = credential.id
                        val password = credential.password

                        when {
                            idToken != null -> {
                                // Got an ID token from Google. Use it to authenticate
                                // with Firebase.

                                Log.d(TAG, "Got ID token." +
                                        "\n email: $email" +
                                        "\n token: $idToken" +
                                        "\n displayName : ${credential.displayName}")
                                viewModel.login(email, idToken)
                            }
                            else -> {
                                // Shouldn't happen.
                                Log.d(TAG, "No ID token!")
                                if (email.isNotEmpty() && !password.isNullOrEmpty()) {
                                    viewModel.login(email, password)
                                }
                            }
                        }
                    } catch (e: ApiException) {
                        e.printStackTrace()
                        Log.d(TAG, "e: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignInBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initViews() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()

        binding.emailEditTxt.addTextChangedListener { editTextChange(binding.emailEditTxt) }
        binding.passwordEditTxt.addTextChangedListener { editTextChange(binding.passwordEditTxt) }
        binding.topBackBtn.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        binding.forgetPasswordBtn.setOnClickListener(this)
        binding.googleBtn.setOnClickListener(this)
        binding.facebookBtn.setOnClickListener(this)
        binding.twitterBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }


    private fun initObserve() {
        viewModel.login.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    this.loginSuccess(it)
                }
                is Resource.Error -> {
                    this.loginFailed(it)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
            }
        }
        viewModel.forgetPassword.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(resource.message)
                    view?.findNavController()?.navigate(SignInFragmentDirections.actionSignInFragmentToForgetPasswordFragment(email = email))
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(resource.message)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignInFragmentDirections

        when (v) {
            binding.topBackBtn -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            binding.loginBtn -> {
                validEmail()
                validPassword()
                if (validEmail() && validPassword()) {
                    viewModel.login(email, password)
                }
            }
            binding.forgetPasswordBtn -> {
                if (validEmail()) {
                    viewModel.forgetPassword(ForgetPasswordRequest(email))
                }
                binding.passwordEditLyt.error = null
            }
            binding.googleBtn -> {
                dialog.show()
                signIn()
            }
            binding.facebookBtn -> {
                //todo: FACEBOOK ile giriş
            }
            binding.twitterBtn -> {
                //todo: TWITTER ile giriş
            }
            binding.signUpBtn -> {
                action = directions.actionSignInFragmentToSignUpFragment()
            }
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
            if (v == binding.loginBtn) {
                activity?.finish()
            }
        }
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.emailEditTxt -> {
                email = v.text.toString()
                binding.emailEditLyt.error?.let {
                    validEmail()
                }
            }
            binding.passwordEditTxt -> {
                password = v.text.toString()
                binding.passwordEditLyt.error?.let {
                    validPassword()
                }
            }
        }
    }

    private fun validEmail() : Boolean {
        when (ValidationFactory.validMail(email)) {
            is Resource.Success -> {
                binding.emailEditLyt.error = null
                return true
            }
            is Resource.Error -> {
                binding.emailEditLyt.error = getString(R.string.wrong_email)
                return false
            }
            else -> {}
        }
        return false
    }

    private fun validPassword() : Boolean {
        ValidationFactory.validPassword(password, requireContext()).let {
            when (it) {
                is Resource.Success -> {
                    binding.passwordEditLyt.error = null
                    return true
                }
                is Resource.Error -> {
                    binding.passwordEditLyt.error = it.message
                    return false
                }
                else -> {}
            }
        }
        return false
    }

    private fun signIn() {
        Log.d(TAG, "sign in")
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener {
                try {
                    signInType = REQ_ONE_TAP
                    val intentSender = IntentSenderRequest.Builder(it.pendingIntent.intentSender)
                        .build()
                    resultLauncher.launch(intentSender)
                } catch (e: IntentSender.SendIntentException) {
                    dialog.hide()
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    showToast(resources.getString(R.string.error_genel_message))
                }
            }
            .addOnFailureListener {
                dialog.hide()
                Log.d(TAG, it.localizedMessage ?: "Error: not sign in")
                showToast(it.localizedMessage)
            }
    }

    override fun loginSuccess(resource: Resource<LoginResponse>) {
        SharedPreferencesUtil.setString(requireContext(), Constants.TOKEN_KEY, resource.data?.token!!)
        SharedPreferencesUtil.setString(requireContext(), Constants.FIREBASE_TOKEN, resource.data.user?.fcm_token ?: "")
        val action = SignInFragmentDirections.actionSignInFragmentToHomeActivity()
        Navigation.findNavController(requireView()).navigate(action)
        requireActivity().finish()
    }

    override fun loginFailed(resource: Resource<LoginResponse>) {
        dialog.hide()
        showToast(resource.message)
    }
}