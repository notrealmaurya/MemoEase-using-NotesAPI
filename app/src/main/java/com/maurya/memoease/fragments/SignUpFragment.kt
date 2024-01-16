package com.maurya.memoease.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    private val fragmentSignUpBindingNull get() = fragmentSignUpBinding!!
    private lateinit var navController: NavController
    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = fragmentSignUpBinding.root


        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        listeners()


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

        val email = fragmentSignUpBinding.emailSignUpFragment.text.toString().trim()
        val password = fragmentSignUpBinding.passwordSignUpFragment.text.toString().trim()

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
        return if (fragmentSignUpBinding.userNameSignUpFragment.text.toString().trim().isEmpty()) {
            showToast("Enter Your userName ")
            false
        } else if (fragmentSignUpBinding.emailSignUpFragment.text.toString().trim().isEmpty()) {
            showToast("Enter Your email ")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(
                fragmentSignUpBinding.emailSignUpFragment.text.toString()
            ).matches()
        ) {
            showToast("Enter Valid Email ")
            false
        } else if (fragmentSignUpBinding.passwordSignUpFragment.text.toString().trim().isEmpty()) {
            showToast("Enter Password ")
            false
        } else if (fragmentSignUpBinding.passwordConfirmSignUpFragment.text.toString().trim()
                .isEmpty()
        ) {
            showToast("Confirm your Password")
            false
        } else if (fragmentSignUpBinding.passwordSignUpFragment.text.toString() != fragmentSignUpBinding.passwordConfirmSignUpFragment.text.toString()
        ) {
            showToast("Your Password doesn't Match ")
            false
        } else {
            true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}