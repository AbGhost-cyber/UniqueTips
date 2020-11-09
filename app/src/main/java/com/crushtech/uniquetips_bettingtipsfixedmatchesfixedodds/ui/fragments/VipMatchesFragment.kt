package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.R
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.adapter.VipMatchesItemAdapter
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.VipMatchesItem
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.BettingMainActivity
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels.BettingViewModel
import com.muddzdev.styleabletoastlibrary.StyleableToast
import kotlinx.android.synthetic.main.vip_matches_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VipMatchesFragment : Fragment(R.layout.vip_matches_layout) {
    private var vipTipName = ""
    private lateinit var bettingViewModel: BettingViewModel
    private lateinit var vipMatchesItemsAdapter: VipMatchesItemAdapter
    private val args: VipMatchesFragmentArgs by navArgs()
    private lateinit var path: String
    private var toast: StyleableToast? = null
    private lateinit var splitPath: List<String>
    private lateinit var sumOfSplitPath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vipTipName = args.VipTipName

        (activity as BettingMainActivity).supportActionBar?.show()
        (activity as BettingMainActivity).supportActionBar?.title = vipTipName
        path = (activity as BettingMainActivity).supportActionBar?.title.toString()

        //removes space between the tips
        sumOfSplitPath = ""
        splitPath = path.split(' ')
        for (paths in splitPath.indices) {
            sumOfSplitPath += splitPath[paths]
        }
        bettingViewModel = (activity as BettingMainActivity).bettingViewModel
        vipMatchesItemsAdapter = VipMatchesItemAdapter()

        vipMatchesRV.apply {
            adapter = vipMatchesItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = true
        }
        //error toast
        toast = StyleableToast.makeText(
            requireContext(),
            "an error occurred",
            R.style.nullPointToast
        )

        // search depending on app bar title... easy yeah?
        bettingViewModel.fetchBettingDataFromRepo(sumOfSplitPath)

        bettingViewModel.mutableData.observe(viewLifecycleOwner, Observer { vipItemList ->
            bettingViewModel.viewModelScope.launch(Dispatchers.Main) {
                delay(150L)
                vipMatchesItemsAdapter.differ.submitList(vipItemList.reversed())
                checkForErrorInDataCall(vipItemList)
            }

            newTips.setOnClickListener {
                val listOfTimeAgos = listOf(
                    "moments", "hour", "hours",
                    "minutes", "minute", "second", "seconds", "future"
                )
                filterList(vipItemList, listOfTimeAgos)
            }

            oldTips.setOnClickListener {
                val listOfTimeAgos = listOf(
                    "day", "yesterday", "week", "days",
                    "months", "years", "weeks", "month"
                )
                filterList(vipItemList, listOfTimeAgos)
            }
        })




        setHasOptionsMenu(true)
    }


    private fun filterList(vipItemList: List<VipMatchesItem>, timeFilterList: List<String>) {
        vipMatchesRV.visibility = View.VISIBLE
        val filteredList = mutableListOf<VipMatchesItem>()
        var contains = false
        vipItemList.forEachIndexed { _, vipMatchesItem ->
            timeFilterList.forEach {
                if (vipMatchesItem.agoTime.contains(it, true)) {
                    contains = true
                }
            }
            if (contains) {
                vipMatchesRV.visibility = View.VISIBLE
                filteredList.add(vipMatchesItem)
                vipMatchesItemsAdapter.differ.submitList(filteredList)
                filter_error.visibility = View.GONE

            } else {
                vipMatchesRV.visibility = View.INVISIBLE
                filter_error.visibility = View.VISIBLE

            }
        }
    }


    private fun checkForErrorInDataCall(vipMatchItems: MutableList<VipMatchesItem>) {
        if (!this.hasInternetConnection() || vipMatchItems.isNullOrEmpty()) {
            toast!!.show()
            vipMatchesRV.visibility = View.GONE
            progressivebar.visibility = View.GONE
            imageView2.visibility = View.VISIBLE
            error_txt.visibility = View.VISIBLE
        } else {
            toast!!.cancel()
            vipMatchesRV.visibility = View.VISIBLE
            filter_error.visibility = View.GONE
            progressivebar.visibility = View.GONE
            imageView2.visibility = View.GONE
            error_txt.visibility = View.GONE
        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_data -> {
                bettingViewModel.refreshData(sumOfSplitPath)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = requireContext().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    ConnectivityManager.TYPE_VPN -> true
                    else -> false
                }
            }
        }
        return false
    }

}

fun main() {
    val list = listOf("hour", "hours ago")
    val num = "16 hours"
    var contains = false
    list.forEach { s ->
        contains = num.contains(s)
    }
    if (contains) {
        println("yes")
    } else {
        println("no")
    }
}