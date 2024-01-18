package com.maurya.memoease.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.maurya.memoease.AdapterNotes
import com.maurya.memoease.models.NoteViewModel
import com.maurya.memoease.R
import com.maurya.memoease.SharedPreferenceHelper
import com.maurya.memoease.databinding.FragmentHomeBinding
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.Constants
import com.maurya.memoease.utils.NetworkResult
import com.maurya.memoease.utils.checkInternet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val noteViewModel by viewModels<NoteViewModel>()


    private lateinit var adapterNotes: AdapterNotes
//    private lateinit var adapterNotesDelete: AdapterNotes
//    private lateinit var homeList: MutableList<NoteResponse>
//    private lateinit var deletedList: MutableList<NoteResponse>


    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = fragmentHomeBinding.root

        fragmentHomeBinding.recyclerViewHomeFragment.isNestedScrollingEnabled = false

//        homeList = mutableListOf()
//        deletedList = mutableListOf()


        listeners()

        return view
    }


    private fun listeners() {

        fragmentHomeBinding.logOutUser.setOnClickListener {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Logout")
            alertDialogBuilder.setMessage("Are you sure you want to logout?")
            alertDialogBuilder.setPositiveButton("Logout") { _, _ ->
                sharedPreferenceHelper.clearToken()
                Log.d("NavigationMemo", "Before navigating to signInFragment")
                navController.navigate(R.id.action_homeFragment_to_signInFragment)
                Log.d("NavigationMemo", "After navigating to signInFragment")

            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        fragmentHomeBinding.addNoteHomeFragment.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_notesFragment)
        }

    }


    private fun onNoteClicked(noteResponse: NoteResponse) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        navController.navigate(R.id.action_homeFragment_to_notesFragment, bundle)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.getNotes()
        navController = Navigation.findNavController(view)
        adapterNotes = AdapterNotes(::onNoteClicked)


        fragmentHomeBinding.recyclerViewHomeFragment.setHasFixedSize(true)
        fragmentHomeBinding.recyclerViewHomeFragment.setItemViewCacheSize(13)
        fragmentHomeBinding.recyclerViewHomeFragment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        fragmentHomeBinding.recyclerViewHomeFragment.adapter = adapterNotes

        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            fragmentHomeBinding.progressBarHomeFragment.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    adapterNotes.submitList(it.data)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {
                    fragmentHomeBinding.progressBarHomeFragment.visibility = View.VISIBLE
                }

                else -> {}
            }

        })


    }

    override fun onResume() {
        super.onResume()

        if (!checkInternet(requireContext())) {
            Snackbar.make(
                fragmentHomeBinding.root,
                "Internet is Not Connected \uD83D\uDE12", Snackbar.LENGTH_LONG
            ).show()
        }

    }

}

