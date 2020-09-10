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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.R
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.adapter.VipMatchesItemAdapter
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.VipMatchesItem
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.BettingMainActivity
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.viemodels.BettingViewmodel
import com.muddzdev.styleabletoastlibrary.StyleableToast
import kotlinx.android.synthetic.main.vip_matches_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VipMatchesFragment : Fragment(R.layout.vip_matches_layout){
    private var vipTipName = ""
    private lateinit var bettingViewmodel: BettingViewmodel
    private lateinit var vipMatchesItemsAdapter: VipMatchesItemAdapter
    private val args: VipMatchesFragmentArgs by navArgs()
    private lateinit var path: String
    private lateinit var splitPath: List<String>
    private lateinit var sumofSplittedPath: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vipTipName = args.VipTipName
        (activity as BettingMainActivity).supportActionBar?.title = vipTipName
        path = (activity as BettingMainActivity).supportActionBar?.title.toString()

        //removes space between the tips
        sumofSplittedPath = ""
        splitPath = path.split(' ')
        for (paths in splitPath.indices) {
            sumofSplittedPath += splitPath[paths]
        }
        bettingViewmodel = (activity as BettingMainActivity).bettingViewModel
        vipMatchesItemsAdapter = VipMatchesItemAdapter()

        vipMatchesRV.apply {
            adapter = vipMatchesItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = true
        }

        // search depending on app bar title... easy yeah?
        bettingViewmodel.fetchBettingDataFromRepo(sumofSplittedPath)
            .observe(viewLifecycleOwner, Observer {
                checkForErrorInDataCall(it)
            })




        setHasOptionsMenu(true)
    }

    private fun checkForErrorInDataCall(vipMatchItems: MutableList<VipMatchesItem>) {
        if (!this.hasInternetConnection()) {
            StyleableToast.makeText(
                requireContext(),
                "No internet connection", R.style.customToast1
            ).show()
            progressivebar.visibility = View.GONE
            imageView2.visibility = View.VISIBLE
            error_txt.visibility = View.VISIBLE
        } else {
            imageView2.visibility = View.GONE
            error_txt.visibility = View.GONE
            progressivebar.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                delay(200L)
                if (vipMatchItems.isNotEmpty()) {
                    progressivebar.visibility = View.GONE
                }
                vipMatchesItemsAdapter.differ.submitList(vipMatchItems)
            }

        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_data -> {
                bettingViewmodel.refreshData(sumofSplittedPath)
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