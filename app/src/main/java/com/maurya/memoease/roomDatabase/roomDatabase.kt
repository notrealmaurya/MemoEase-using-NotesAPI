package com.maurya.memoease.roomDatabase

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentSplashBinding
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.HelperSharedPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//
//@Database(entities = [NoteResponse::class], version = 1, exportSchema = false)
//abstract class roomDatabase : RoomDatabase() {
//
//    abstract fun noteDao(): noteDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: roomDatabase? = null
//
//        fun getDatabase(context: Context): roomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    roomDatabase::class.java,
//                    "app_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
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