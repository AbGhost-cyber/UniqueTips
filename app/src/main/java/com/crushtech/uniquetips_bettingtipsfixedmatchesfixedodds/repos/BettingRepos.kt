package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.VipMatchesItem
import com.google.firebase.firestore.FirebaseFirestore

class BettingRepos {
    fun getBettingDataFromFirebase(path: String): LiveData<MutableList<VipMatchesItem>> {
        val mutableData = MutableLiveData<MutableList<VipMatchesItem>>()
        FirebaseFirestore.getInstance().collection(path).get()
            .addOnSuccessListener { result ->
                val listData = mutableListOf<VipMatchesItem>()
                for (docs in result) {
                    val leagueName = docs.getString("leagueName")
                    val date = docs.getString("date")
                    val teamOne = docs.getString("teamOne")
                    val teamTwo = docs.getString("teamTwo")
                    val halfAndFullTimeScoresInOdds =
                        docs.getString("HalfAndFullTimeScoresInOdds")
                    val matchWon = docs.getBoolean("matchWon")
                    val odds = docs.getString("odds")
                    val halfTimeScore = docs.getString("HalfTimeScore")
                    val fullTimeScore = docs.getString("FullTimeScore")
                    val isMatchPlayed = docs.getBoolean("isMatchPlayed")
                    val vipName = docs.getString("vipName")
                    val isCorrectScore = docs.getBoolean("isCorrectScore")
                    val correctScore = docs.getString("correctScore")


                    try {
                        val vipMatchesItem = VipMatchesItem(
                            leagueName!!, date!!, teamOne!!,
                            teamTwo!!, halfAndFullTimeScoresInOdds!!,
                            matchWon!!, odds!!, halfTimeScore!!, fullTimeScore!!,
                            isMatchPlayed!!, vipName!!, isCorrectScore!!, correctScore!!
                        )
                        listData.add(vipMatchesItem)
                    } catch (e: KotlinNullPointerException) {
                    }
                }
                mutableData.value = listData
            }
        return mutableData
    }


}