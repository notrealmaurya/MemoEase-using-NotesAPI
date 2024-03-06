package com.maurya.memoease.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.maurya.memoease.R
import com.maurya.memoease.utils.HelperSharedPreference
import com.maurya.memoease.databinding.FragmentSignInBinding
import com.maurya.memoease.models.UserRequest
import com.maurya.memoease.utils.NetworkResult
import com.maurya.memoease.utils.hideKeyboard
import com.maurya.memoease.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
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

        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            isLoading(false)
            when (it) {
                is NetworkResult.Success -> {
                    sharedPreferenceHelper.saveToken(it.data!!.token)
                    navController.navigate(R.id.action_signInFragment_to_homeFragment)
                }

                is NetworkResult.Error -> {
                    showToast(requireContext(), it.message.toString())
                    isLoading(false)
                }
                is NetworkResult.Loading -> {
                    isLoading(true)
                }
            }
        })


    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            fragmentSignInBinding.loginButtonSignInFragment.visibility = View.INVISIBLE
            fragmentSignInBinding.progressBaSignInFragment.visibility = View.VISIBLE
        } else {
            fragmentSignInBinding.loginButtonSignInFragment.visibility = View.VISIBLE
            fragmentSignInBinding.progressBaSignInFragment.visibility = View.INVISIBLE

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
        isLoading(true)

        fragmentSignInBinding.progressBaSignInFragment.isVisible = true
        fragmentSignInBinding.loginButtonSignInFragment.isVisible = false

        view?.let { hideKeyboard(it) }

        val email = fragmentSignInBinding.emailSignInFragment.text.toString().trim()
        val password = fragmentSignInBinding.passwordSignInFragment.text.toString().trim()
        val userName = ""

        authViewModel.loginUser(UserRequest(email, password, userName))


        fragmentSignInBinding.progressBaSignInFragment.isVisible = false
        fragmentSignInBinding.loginButtonSignInFragment.isVisible = true
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


}