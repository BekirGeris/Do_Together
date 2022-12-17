package com.example.dotogether.view.fragment

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.BuildConfig
import com.example.dotogether.R
import com.example.dotogether.databinding.FragmentSignInBinding
import com.example.dotogether.util.Resource
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.view.dialog.CustomProgressDialog
import com.example.dotogether.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(), View.OnClickListener {

    private val TAG = "BEKBEK"
    private val REQ_ONE_TAP = 1
    private var signInType: Int = 0

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignInBinding
    lateinit var dialog: CustomProgressDialog

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

                        when {
                            idToken != null -> {
                                // Got an ID token from Google. Use it to authenticate
                                // with Firebase.

                                Log.d(TAG, "Got ID token." +
                                        "\n email: ${credential.id}" +
                                        "\n token: ${credential.googleIdToken}" +
                                        "\n displayName : ${credential.displayName}")
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
        binding = FragmentSignInBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        return binding.root
    }

    private fun initViews() {
        binding.emailEditTxt.addTextChangedListener { editTextChange(binding.emailEditTxt) }
        binding.passwordEditTxt.addTextChangedListener { editTextChange(binding.passwordEditTxt) }
        binding.topBackBtn.setOnClickListener(this)
        //todo: acitonlar eklenince açılacak
        binding.loginBtn.setOnClickListener(this)
        binding.forgetPasswordBtn.setOnClickListener(this)
        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }

    private fun initObserve() {
        dialog = CustomProgressDialog(requireActivity())

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
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignInFragmentDirections

        when (v) {
            binding.topBackBtn -> {
                action = directions.actionSignInFragmentToLoginFragment()
            }
            binding.loginBtn -> {
                validEmail()
                validPassword()
                if (validEmail() && validPassword()) {
                    action = directions.actionSignInFragmentToHomeActivity()
                }
            }
            binding.forgetPasswordBtn -> {
                if (validEmail()) {
                    Toast.makeText(requireContext(), "Email Sent", Toast.LENGTH_LONG).show()
                }
                binding.passwordEditLyt.error = null
            }
            binding.googleBtn -> {
                dialog.shoe()
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
                binding.emailEditLyt.error = "Wrong email"
                return false
            }
            else -> {}
        }
        return false
    }

    private fun validPassword() : Boolean {
        when (ValidationFactory.validPassword(password)) {
            is Resource.Success -> {
                binding.passwordEditLyt.error = null
                return true
            }
            is Resource.Error -> {
                binding.passwordEditLyt.error = "Wrong password"
                return false
            }
            else -> {}
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
                    Toast.makeText(requireContext(), resources.getString(R.string.error_genel_message), Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                dialog.hide()
                Log.d(TAG, it.localizedMessage ?: "Error: not sign in")
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }
}