package com.maurya.memoease.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.maurya.memoease.R
import com.maurya.memoease.utils.HelperSharedPreference
import com.maurya.memoease.databinding.FragmentSignInBinding
import com.maurya.memoease.models.UserRequest
import com.maurya.memoease.utils.NetworkResult
import com.maurya.memoease.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var fragmentSignInBinding: FragmentSignInBinding
    private val fragmentSignInBindingNull get() = fragmentSignInBinding!!
    private lateinit var navController: NavController
    private val authViewModel by activityViewModels<AuthenticationViewmodel>()

    @Inject
    lateinit var sharedPreferenceHelper: HelperSharedPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)

        return fragmentSignInBindingNull.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        listeners()


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.userResponseStateFlow.collect {
                    loading(false)
                    if (it is NetworkResult.Loading) {
                        loading(true)
                    } else {
                        loading(false)
                    }
                    when (it) {
                        is NetworkResult.Success -> {
                            sharedPreferenceHelper.saveToken(it.data!!.token)
                            navController.navigate(R.id.action_signInFragment_to_homeFragment)
                        }

                        is NetworkResult.Error -> {
                            showToast(requireContext(), it.message.toString())
                            loading(false)
                        }

                        is NetworkResult.Loading -> {
                            loading(true)
                        }

                        else -> {}
                    }
                }

            }
        }

    }

    private fun listeners() {
        fragmentSignInBinding.dontHaveAccountSignInFragment.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        fragmentSignInBinding.loginButtonSignInFragment.setOnClickListener {
            if (isValidSignUpDetails()) {
                signIn()
            }
        }

        fragmentSignInBinding.loginGoogleButtonSignInFragment.setOnClickListener {
            showToast(requireContext(), "Feature coming soon")
        }

        fragmentSignInBinding.forgetPasswordSignInFragment.setOnClickListener {
            showToast(requireContext(), "Feature coming soon")
        }

    }

    private fun signIn() {
        val email = fragmentSignInBinding.emailSignInFragment.text.toString().trim()
        val password = fragmentSignInBinding.passwordSignInFragment.text.toString().trim()
        val userName = ""

        authViewModel.loginUser(UserRequest(email, password, userName))
    }

    private fun isValidSignUpDetails(): Boolean {
        return if (fragmentSignInBinding.emailSignInFragment.text.toString().trim().isEmpty()) {
            showToast(requireContext(), "Enter Your email ")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(
                fragmentSignInBinding.emailSignInFragment.text.toString()
            ).matches()
        ) {
            showToast(requireContext(), "Enter Valid Email ")
            false
        } else if (fragmentSignInBinding.passwordSignInFragment.text.toString().trim().isEmpty()) {
            showToast(requireContext(), "Enter Password ")
            false
        } else {
            true
        }
    }


    private fun loading(isLoading: Boolean) {
        if (!isLoading) {
            fragmentSignInBinding.progressBaSignInFragment.visibility = View.VISIBLE
            fragmentSignInBinding.loginButtonSignInFragment.visibility = View.INVISIBLE
        } else {
            fragmentSignInBinding.progressBaSignInFragment.visibility = View.GONE
            fragmentSignInBinding.loginButtonSignInFragment.visibility = View.VISIBLE
        }
    }


}