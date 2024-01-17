package com.maurya.memoease.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.maurya.memoease.AdapterNotes
import com.maurya.memoease.NoteViewModel
import com.maurya.memoease.OnItemClickListener
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentHomeBinding
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.models.checkInternet
import com.maurya.memoease.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var adapterNotes: AdapterNotes
    private lateinit var adapterNotesDelete: AdapterNotes
    private lateinit var homeList: MutableList<NoteResponse>
    private lateinit var deletedList: MutableList<NoteResponse>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = fragmentHomeBinding.root


        fragmentHomeBinding.recyclerViewHomeFragment.isNestedScrollingEnabled = false

        fetchDataFromDatabase()
        displayItems()
        listeners()

        return view
    }

    private fun fetchDataFromDatabase() {


    }


    private fun displayItems() {

        fragmentHomeBinding.recyclerViewHomeFragment.setHasFixedSize(true)
        fragmentHomeBinding.recyclerViewHomeFragment.setItemViewCacheSize(13)
        noteViewModel.getNotes()
        fragmentHomeBinding.recyclerViewHomeFragment.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapterNotes =
            AdapterNotes(requireContext(), ::onNoteClicked, homeList, deletedList, false)
        fragmentHomeBinding.recyclerViewHomeFragment.adapter = adapterNotes


    }

    private fun listeners() {

        //ToolbarAddTask


        fragmentHomeBinding.logOutUser.setOnClickListener {

//            val userName = auth.currentUser?.email ?: "User"
//
//            val alertDialogBuilder = AlertDialog.Builder(requireContext())
//            alertDialogBuilder.setTitle("Logout")
//            alertDialogBuilder.setMessage("Are you sure you want to logout, $userName?")
//
//            alertDialogBuilder.setPositiveButton("Logout") { _, _ ->
//                auth.signOut()
//                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
//            }
//
//            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.show()
        }


    }


    private fun onNoteClicked(noteResponse: NoteResponse) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        navController.navigate(R.id.action_homeFragment_to_notesFragment, bundle)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        listeners()

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

