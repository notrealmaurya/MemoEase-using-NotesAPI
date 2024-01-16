package com.maurya.memoease.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentNotesBinding
import com.maurya.memoease.databinding.FragmentSignInBinding
import com.maurya.memoease.databinding.FragmentSplashBinding


class NotesFragment : Fragment() {

    private lateinit var fragmentNotesBinding: FragmentNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentNotesBinding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = fragmentNotesBinding.root





        return view


    }






}