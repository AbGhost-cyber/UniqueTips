package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.VipMatchesItem
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.repos.BettingRepos
import kotlinx.coroutines.launch


class BettingViewModel(
    private var repos: BettingRepos,
    app: Application
) : AndroidViewModel(app) {

    private val _mutableData = MutableLiveData<MutableList<VipMatchesItem>>()
    val mutableData: LiveData<MutableList<VipMatchesItem>> = _mutableData


    fun fetchBettingDataFromRepo(path: String) = viewModelScope.launch {
        repos.getBettingDataFromFirebase(path).observeForever {
            _mutableData.postValue(it)
        }

    }

    fun refreshData(path: String): LiveData<MutableList<VipMatchesItem>> {
        repos = BettingRepos()
        repos.getBettingDataFromFirebase(path).observeForever {
            _mutableData.value = it
        }
        return _mutableData
    }

}

