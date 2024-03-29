package com.maurya.memoease.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.maurya.memoease.R
import com.maurya.memoease.utils.HelperSharedPreference
import com.maurya.memoease.databinding.FragmentSignUpBinding
import com.maurya.memoease.models.UserRequest
import com.maurya.memoease.utils.NetworkResult
import com.maurya.memoease.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    private val fragmentSignUpBindingNull get() = fragmentSignUpBinding!!
    private lateinit var navController: NavController
    private val authViewModel by activityViewModels<AuthenticationViewmodel>()

    @Inject
    lateinit var sharedPreferenceHelper: HelperSharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = fragmentSignUpBinding.root

        loading(false)

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        listeners()

        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            loading(false)
            when (it) {
                is NetworkResult.Success -> {
                    sharedPreferenceHelper.saveToken(it.data!!.token)
                    navController.navigate(R.id.action_signUpFragment_to_homeFragment)
                }

                is NetworkResult.Error -> {
                    showToast(requireContext(), it.message.toString())
                    loading(false)
                }

                is NetworkResult.Loading -> {
                    loading(true)
                }
            }


        })
    }


    private fun listeners() {


        fragmentSignUpBinding.haveAccountSignUpFragment.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        fragmentSignUpBinding.signupButtonSignUpFragment.setOnClickListener {
            if (isValidSignUpDetails()) {
                signUp()
            }
        }


        fragmentSignUpBinding.loginGoogleButtonSignUpFragment.setOnClickListener {
            Toast.makeText(context, "Feature coming soon", Toast.LENGTH_SHORT).show()
        }


    }

    private fun signUp() {
        loading(true)
        val userName = fragmentSignUpBinding.userNameSignUpFragment.text.toString().trim()
        val email = fragmentSignUpBinding.emailSignUpFragment.text.toString().trim()
        val password = fragmentSignUpBinding.passwordSignUpFragment.text.toString().trim()

        authViewModel.registerUser(UserRequest(email, password, userName))
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            fragmentSignUpBinding.signupButtonSignUpFragment.visibility = View.INVISIBLE
            fragmentSignUpBinding.progressBaSignUpFragment.visibility = View.VISIBLE
        } else {
            fragmentSignUpBinding.progressBaSignUpFragment.visibility = View.INVISIBLE
            fragmentSignUpBinding.signupButtonSignUpFragment.visibility = View.VISIBLE
        }
    }


    private fun isValidSignUpDetails(): Boolean {
        return if (fragmentSignUpBinding.userNameSignUpFragment.text.toString().trim()
                .isEmpty()
        ) {
            showToast(requireContext(), "Enter Your userName ")
            false
        } else if (fragmentSignUpBinding.emailSignUpFragment.text.toString().trim()
                .isEmpty()
        ) {
            showToast(requireContext(), "Enter Your email ")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(
                fragmentSignUpBinding.emailSignUpFragment.text.toString()
            ).matches()
        ) {
            showToast(requireContext(), "Enter Valid Email ")
            false
        } else if (fragmentSignUpBinding.passwordSignUpFragment.text.toString().trim()
                .isEmpty() && fragmentSignUpBinding.passwordSignUpFragment.text.length >= 5
        ) {
            showToast(requireContext(), "Password length should be greater than 5 ")
            false
        } else if (fragmentSignUpBinding.passwordConfirmSignUpFragment.text.toString()
                .trim()
                .isEmpty()
        ) {
            showToast(requireContext(), "Confirm your Password")
            false
        } else if (fragmentSignUpBinding.passwordSignUpFragment.text.toString() != fragmentSignUpBinding.passwordConfirmSignUpFragment.text.toString()
        ) {
            showToast(requireContext(), "Your Password doesn't Match ")
            false
        } else {
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }


}