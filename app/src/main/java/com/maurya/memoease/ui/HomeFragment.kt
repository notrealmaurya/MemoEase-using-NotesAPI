package com.maurya.memoease.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.maurya.memoease.AdapterNotes
import com.maurya.memoease.R
import com.maurya.memoease.utils.HelperSharedPreference
import com.maurya.memoease.databinding.FragmentHomeBinding
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.NetworkResult
import com.maurya.memoease.utils.checkInternet
import com.maurya.memoease.utils.showConfirmationDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val noteViewModel by viewModels<NoteViewModel>()

    //    private lateinit var adapterNotesDelete: AdapterNotes

//    private lateinit var deletedList: MutableList<NoteResponse>

    companion object {
        lateinit var adapterNotes: AdapterNotes
        lateinit var homeList: MutableList<NoteResponse>
    }

    @Inject
    lateinit var sharedPreferenceHelper: HelperSharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = fragmentHomeBinding.root

        fragmentHomeBinding.recyclerViewHomeFragment.isNestedScrollingEnabled = false

        homeList = mutableListOf()
//        deletedList = mutableListOf()

        listeners()

        return view
    }


    private fun listeners() {

        fragmentHomeBinding.logOutUser.setOnClickListener {
            showConfirmationDialog(
                requireContext(),
                "Logout",
                "Are you sure you want to logout?"
            ) {
                sharedPreferenceHelper.clearToken()
                Log.d("NavigationMemo", "Before navigating to signInFragment")
                navController.navigate(R.id.action_homeFragment_to_signInFragment)
                Log.d("NavigationMemo", "After navigating to signInFragment")
            }
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

        fragmentHomeBinding.recyclerViewHomeFragment.setHasFixedSize(true)
        fragmentHomeBinding.recyclerViewHomeFragment.setItemViewCacheSize(13)
        fragmentHomeBinding.recyclerViewHomeFragment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterNotes = AdapterNotes(homeList, ::onNoteClicked)
        fragmentHomeBinding.recyclerViewHomeFragment.adapter = adapterNotes

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.notesStateFLow.collect {
                    fragmentHomeBinding.progressBarHomeFragment.visibility = View.GONE
                    when (it) {
                        is NetworkResult.Success -> {
                            homeList.clear()
                            homeList.addAll(it.data!!)
                            adapterNotes.notifyDataSetChanged()
                        }

                        is NetworkResult.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        is NetworkResult.Loading -> {
                            fragmentHomeBinding.progressBarHomeFragment.visibility = View.VISIBLE
                        }

                        else -> {}
                    }
                }
            }
        }

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

