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
import com.example.dotogether.BuildConfig
import com.example.dotogether.data.callback.LoginCallback
import com.example.dotogether.data.callback.RegisterCallback
import com.example.dotogether.databinding.FragmentSignUpBinding
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
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
class SignUpFragment : BaseFragment(), View.OnClickListener, RegisterCallback, LoginCallback {

    private val REQ_ONE_TAP = 1
    private var signUpType: Int = 0

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignUpBinding

    private var name: String = ""
    private var userName: String = ""
    private var email: String = ""
    private var password: String = ""
    private var passwordAgain: String = ""

    private var idToken: String? = null

    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        dialog.hide()
        Log.d(TAG, "resultCode : ${it.resultCode}")
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data

            when(signUpType) {
                REQ_ONE_TAP -> {
                    try {
                        val credential = oneTapClient.getSignInCredentialFromIntent(data)
                        idToken = credential.googleIdToken ?: credential.password
                        email = credential.id
                        userName = email.split("@")[0]
                        name = credential.displayName ?: email.split("@")[0]

                        when {
                            idToken != null -> {
                                // Got an ID token from Google. Use it to authenticate
                                // with Firebase.

                                Log.d(TAG, "Got ID token." +
                                        "\n email: $email" +
                                        "\n token: $idToken" +
                                        "\n userName : $userName")

                                viewModel.login(email, idToken!!)
                            }
                            else -> {
                                // Shouldn't happen.
                                Log.d(TAG, "No ID token!")
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
        binding = FragmentSignUpBinding.inflate(layoutInflater)

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
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .build()

        binding.nameEditTxt.addTextChangedListener{ editTextChange(binding.nameEditTxt) }
        binding.usernameEditTxt.addTextChangedListener{ editTextChange(binding.usernameEditTxt) }
        binding.emailEditTxt.addTextChangedListener{ editTextChange(binding.emailEditTxt) }
        binding.passwordEditTxt.addTextChangedListener{ editTextChange(binding.passwordEditTxt) }
        binding.passwordAgainEditTxt.addTextChangedListener{ editTextChange(binding.passwordAgainEditTxt) }
        binding.topBackBtn.setOnClickListener(this)
        //todo: actionlar eklenince açılacak
        binding.signUpBtn.setOnClickListener(this)
        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signInBtn.setOnClickListener(this)
    }

    private fun initObserve() {
        viewModel.register.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    this.registerSuccess(it)
                }
                is Resource.Error -> {
                    this.registerFailed(it)
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
            }
        }

        viewModel.login.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    this.loginSuccess(it)
                }
                is Resource.Error -> {
                    this.loginFailed(it)
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignUpFragmentDirections

        when (v) {
            binding.topBackBtn -> {
                activity?.onBackPressed()
            }
            binding.signUpBtn -> {
                validName()
                validUserName()
                validEmail()
                validPassword()
                validPasswordAgain()
                if (validName() && validUserName() && validEmail() && validPassword() && validPasswordAgain()) {
                    viewModel.register(name, userName, email, password, passwordAgain)
                }
            }
            binding.googleBtn -> {
                dialog.shoe()
                signUp()
            }
            binding.facebookBtn -> {
                //todo: FACEBOOK ile giriş
            }
            binding.twitterBtn -> {
                //todo: TWITTER ile giriş
            }
            binding.signInBtn -> {
                action = directions.actionSignUpFragmentToSignInFragment()
            }
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
            if (v == binding.signUpBtn) {
                activity?.finish()
            }
        }
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.nameEditTxt -> {
                name = v.text.toString()
                validName()
            }
            binding.usernameEditTxt -> {
                userName = v.text.toString()
                validUserName()
            }
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
            binding.passwordAgainEditTxt -> {
                passwordAgain = v.text.toString()
                binding.passwordAgainEditLyt.error?.let {
                    validPasswordAgain()
                }
            }
        }
    }

    private fun signUp() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener {
                try {
                    Log.d(TAG, "sign up")
                    signUpType = REQ_ONE_TAP
                    val intentSender = IntentSenderRequest.Builder(it.pendingIntent.intentSender)
                        .build()
                    resultLauncher.launch(intentSender)
                } catch (e: IntentSender.SendIntentException) {
                    dialog.hide()
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener {
                dialog.hide()
                Log.d(TAG, "Error : ${it.localizedMessage}")
                showToast(it.localizedMessage)
            }
    }

    override fun registerSuccess(resource: Resource<RegisterResponse>) {
        SharedPreferencesUtil.setString(requireContext(), Constants.TOKEN_KEY, resource.data?.token!!)
        SharedPreferencesUtil.setString(requireContext(), Constants.FIREBASE_TOKEN, resource.data.user?.fcm_token ?: "")
        goToHomeActivity()
    }

    override fun registerFailed(resource: Resource<RegisterResponse>) {
        dialog.hide()
        Log.d(TAG, "Error: ${resource.message}")
        showToast(resource.message)
    }

    override fun loginSuccess(resource: Resource<LoginResponse>) {
        SharedPreferencesUtil.setString(requireContext(), Constants.TOKEN_KEY, resource.data?.token!!)
        SharedPreferencesUtil.setString(requireContext(), Constants.FIREBASE_TOKEN, resource.data.user?.fcm_token ?: "")
        showToast("Bu hesap zaten mevcut. Giriş yapıldı.")
        goToHomeActivity()
    }

    private fun goToHomeActivity() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToHomeActivity()
        Navigation.findNavController(requireView()).navigate(action)
        requireActivity().finish()
    }

    override fun loginFailed(resource: Resource<LoginResponse>) {
        idToken?.let { pass ->
            viewModel.register(name, userName, email, pass, pass)
        }
    }

    private fun validName() : Boolean {
        return if (name.isEmpty()) {
            binding.nameEditLyt.error = "Wrong name"
            false
        } else {
            binding.nameEditLyt.error = null
            true
        }
    }

    private fun validUserName() : Boolean {
        return if (userName.isEmpty()) {
            binding.usernameEditLyt.error = "Wrong username"
            false
        } else {
            binding.usernameEditLyt.error = null
            true
        }
    }

    private fun validEmail() : Boolean {
        when (ValidationFactory.validMail(email)) {
            is Resource.Success -> {
                binding.emailEditLyt.error = null
                return true
            }
            is Resource.Error -> {
                binding.emailEditLyt.error = "Wrong email"
                return false
            }
            else -> {}
        }
        return false
    }

    private fun validPassword() : Boolean {
        ValidationFactory.validPassword(password).let {
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

    private fun validPasswordAgain() : Boolean {
        return if (passwordAgain.isEmpty() || password != passwordAgain) {
            binding.passwordAgainEditLyt.error = "Passwords are not the same"
            false
        } else {
            binding.passwordAgainEditLyt.error = null
            true
        }
    }
}