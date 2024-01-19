package com.maurya.memoease.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentSplashBinding
import com.maurya.memoease.utils.HelperSharedPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var fragmentSplashBinding: FragmentSplashBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var sharedPreferenceHelper: HelperSharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)
        return fragmentSplashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        Handler(Looper.myLooper()!!).postDelayed(
            {
                if (sharedPreferenceHelper.getToken() != null) {
                    navController.navigate(R.id.action_splashFragment_to_homeFragment)
                } else {
                    navController.navigate(R.id.action_splashFragment_to_signInFragment)
                }
            }, 2500
        )
    }
}