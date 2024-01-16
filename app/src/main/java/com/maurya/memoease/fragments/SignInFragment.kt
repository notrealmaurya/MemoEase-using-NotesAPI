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
import com.google.firebase.auth.FirebaseAuth
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private lateinit var fragmentSignInBinding: FragmentSignInBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private var isLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = fragmentSignInBinding.root


        listeners()
        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)



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

        fragmentSignInBinding.loginGoogleButtonSignInFragment.setOnClickListener{
            Toast.makeText(context,"Feature coming soon",Toast.LENGTH_SHORT).show()
        }

        fragmentSignInBinding.forgetPasswordSignInFragment.setOnClickListener{
            Toast.makeText(context,"Feature coming soon",Toast.LENGTH_SHORT).show()
        }

    }

    private fun signIn() {

        loading(true)

        val email = fragmentSignInBinding.emailSignInFragment.text.toString().trim()
        val password = fragmentSignInBinding.passwordSignInFragment.text.toString().trim()


    }

    private fun isValidSignUpDetails(): Boolean {
        return if (fragmentSignInBinding.emailSignInFragment.text.toString().trim().isEmpty()) {
            showToast("Enter Your email ")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(
                fragmentSignInBinding.emailSignInFragment.text.toString()
            ).matches()
        ) {
            showToast("Enter Valid Email ")
            false
        } else if (fragmentSignInBinding.passwordSignInFragment.text.toString().trim().isEmpty()) {
            showToast("Enter Password ")
            false
        } else {
            true
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            fragmentSignInBinding.loginButtonSignInFragment.visibility = View.INVISIBLE
            fragmentSignInBinding.progressBaSignInFragment.visibility = View.VISIBLE
        } else {
            fragmentSignInBinding.progressBaSignInFragment.visibility = View.INVISIBLE
            fragmentSignInBinding.loginButtonSignInFragment.visibility = View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}