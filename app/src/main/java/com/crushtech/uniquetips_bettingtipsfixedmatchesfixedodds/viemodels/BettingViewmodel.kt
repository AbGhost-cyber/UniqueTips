package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.VipMatchesItem
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.repos.BettingRepos


class BettingViewmodel(
    private var repos: BettingRepos,
    private val app: Application
) : AndroidViewModel(app) {
    private var mutableData = MutableLiveData<MutableList<VipMatchesItem>>()

    fun fetchBettingDataFromRepo(path: String): LiveData<MutableList<VipMatchesItem>> {
        repos.getBettingDataFromFirebase(path).observeForever { userList ->
            mutableData.value = userList
        }
        return mutableData
    }

    fun refreshData(path: String): LiveData<MutableList<VipMatchesItem>> {
        repos = BettingRepos()
        repos.getBettingDataFromFirebase(path).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

}

