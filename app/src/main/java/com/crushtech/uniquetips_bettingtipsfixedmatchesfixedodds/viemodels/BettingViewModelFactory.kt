package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.repos.BettingRepos

@Suppress("UNCHECKED_CAST")
class BettingViewModelFactory(private val repos: BettingRepos, private val app:Application)
    :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BettingViewModel(repos, app) as T
    }
}