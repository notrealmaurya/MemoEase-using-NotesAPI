package com.maurya.memoease.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.maurya.memoease.OnItemClickListener
import com.maurya.memoease.R
import com.maurya.memoease.api.NotesAPI
import com.maurya.memoease.databinding.FragmentHomeBinding
import com.maurya.memoease.models.checkInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener {


    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var navController: NavController
    private var isDetailVisible = false
    private var isDateVisible = false
    private var isImportant: Boolean = false

    private var isRecyclerViewVisible = false

    companion object {
        private const val KEY_SELECTED_DATE = "KEY_SELECTED_DATE"
    }

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
        fragmentHomeBinding.recyclerViewHomeFragment.layoutManager =
            LinearLayoutManager(context)
        adapterToDoInComplete =
            AdapterToDo(requireContext(), this, inCompleteList, completeList, false)
        fragmentHomeBinding.recyclerViewInCompleteHomeFragment.adapter = adapterToDoInComplete



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

    override fun onItemClickListener(position: Int, isComplete: Boolean) {


    }


    override fun onItemCheckedChange(position: Int, isChecked: Boolean, isCompleteList: Boolean) {
//
//        val incompleteTasksRef = dataBaseRef.child("incompleteTasks")
//        val completeTasksRef = dataBaseRef.child("completeTasks")
//
//        if (isCompleteList) {
//            if (!isChecked && completeList.size > position) {
//                val completedItem = completeList[position]
//                val taskId = completedItem.id
//                completeTasksRef.child(taskId).removeValue()
//
//                val incompleteTask = DataToDo(
//                    taskId,
//                    completedItem.taskName,
//                    completedItem.taskDetails,
//                    completedItem.taskCompleteUpToDate,
//                    completedItem.isImportant,
//                    false
//                )
//                incompleteTasksRef.child(taskId).setValue(incompleteTask)
//
//                adapterToDoComplete.notifyDataSetChanged()
//                adapterToDoInComplete.notifyDataSetChanged()
//                Toast.makeText(context, "Task Marked as InComplete", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            if (isChecked && inCompleteList.size > position) {
//                val inCompletedItem = inCompleteList[position]
//
//                val taskId = inCompletedItem.id
//                incompleteTasksRef.child(taskId).removeValue()
//
//                val completeTask = DataToDo(
//                    taskId,
//                    inCompletedItem.taskName,
//                    inCompletedItem.taskDetails,
//                    inCompletedItem.taskCompleteUpToDate,
//                    inCompletedItem.isImportant,
//                    true
//                )
//                completeTasksRef.child(taskId).setValue(completeTask)
//
//                adapterToDoComplete.notifyDataSetChanged()
//                adapterToDoInComplete.notifyDataSetChanged()
//                Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show()
//            }
//        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

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

