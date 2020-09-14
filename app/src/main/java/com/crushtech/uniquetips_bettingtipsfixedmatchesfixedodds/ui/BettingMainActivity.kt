package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.R
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.repos.BettingRepos
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels.BettingViewModel
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels.BettingViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "BettingMainActivity"

data class VipItems(val name: String, val image: Int)

class BettingMainActivity : AppCompatActivity() {

    lateinit var bettingViewModel: BettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val bettingRepos = BettingRepos()
        val viewModelProviderFactory = BettingViewModelFactory(bettingRepos, application)
        bettingViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(BettingViewModel::class.java)

        val navController = Navigation.findNavController(this, R.id.bettingNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val appBarConfig = AppBarConfiguration(setOf(R.id.vipTipsFragment))
        setupActionBarWithNavController(bettingNavHostFragment.findNavController(), appBarConfig)

        //subscribe all device to this topic
        FirebaseMessaging.getInstance().subscribeToTopic("bettingtips")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d(TAG, msg)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = bettingNavHostFragment.findNavController()
        return navController.navigateUp()
    }

}