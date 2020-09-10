package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.fragments

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.R
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.adapter.VipItemsAdapter
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.SpacesItemDecoration
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.BettingMainActivity
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.VipItems
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.android.synthetic.main.tips_home_layout.*

class VipTipsFragment : Fragment(R.layout.tips_home_layout) {

    private var vipItemsList: ArrayList<VipItems>? = null
    private lateinit var vipItemsAdapter: VipItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BettingMainActivity).supportActionBar?.hide()

        initVipItems()
        val manager = ReviewManagerFactory.create(requireContext())
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = request.result
                val flow =
                    manager.launchReviewFlow((activity as BettingMainActivity), reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            }
        }
        val vipItemsAnimation = AnimationUtils.loadAnimation(
            context,
            android.R.anim.slide_in_left
        )
        vipItemsAdapter = VipItemsAdapter()
        vipItemsAdapter.differ.submitList(vipItemsList!!)

        viprecView.apply {
            adapter = vipItemsAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
            addItemDecoration(SpacesItemDecoration(10))
        }

        vipItemsAdapter.setOnItemClickListener {
            vipItemsAnimation.start()
        }
        showSettings()
    }

    private fun showSettings() {
        show_settings.setOnClickListener {
            val dialog = Dialog(requireContext(), R.style.AppTheme)

            dialog.setContentView(R.layout.settings_layout)
            val dismissDialog = dialog.findViewById<ImageView>(R.id.imageView)
            val shareTv = dialog.findViewById<TextView>(R.id.shareAppTv)
            val privacyPolicy = dialog.findViewById<TextView>(R.id.privacyPolicyTv)
            val rateAppTv = dialog.findViewById<TextView>(R.id.rateAppTv)
            val checkOutTips = dialog.findViewById<ExtendedFloatingActionButton>(R.id.checkOutTips)
            checkOutTips.setOnClickListener {
                dialog.dismiss()
            }
            setUpTextViewsFunctions(listOf(shareTv, privacyPolicy, rateAppTv))

            dismissDialog.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }


    private fun setUpTextViewsFunctions(textViews: List<TextView>) {

        for (elements in textViews.indices) {
            textViews[elements].setOnClickListener {
                when (elements) {
                    0 -> {
                        setUpShareFunction()
                    }
                    1 -> {
                        showBrowser("http://www.crushtech.unaux.com/privacypolicy/?i=1")
                    }
                    2 -> {
                        rateFunction()
                    }
                }
            }

        }
    }


    private fun rateFunction() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + requireContext().packageName)
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireContext().packageName)
                )
            )
        }
    }



        private fun showBrowser(url: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        private fun setUpShareFunction() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val appPackageName =
                requireContext().applicationContext.packageName
            val strAppLink: String
            strAppLink = try {
                "https://play.google.com/store/apps/details?id$appPackageName"
            } catch (anfe: ActivityNotFoundException) {
                "https://play.google.com/store/apps/details?id$appPackageName"
            }
            shareIntent.type = "text/link"
            val shareBody =
                "Hey, Check out Unique-Tips, i use it to get winning odds and betting tips download for free here: \n$strAppLink"
            val shareSub = "APP NAME/TITLE"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareIntent, "Share Using"))
        }






    private fun initVipItems() {
        vipItemsList = ArrayList()
        vipItemsList!!.add(
            VipItems(
                "Special VIP",
                R.drawable.special_vip
            )
        )
        vipItemsList!!.add(
            VipItems(
                "Correct Scores",
                R.drawable.correct_scores

            )
        )
        vipItemsList!!.add(
            VipItems(
                "HT FT TIPS",
                R.drawable.halftime_vip_icon

            )
        )
        vipItemsList!!.add(
            VipItems(
                "DAILY 20 ODD",
                R.drawable.daily_20
            )
        )
        vipItemsList!!.add(
            VipItems(
                "Fixed Draw",
                R.drawable.correct_score_vip_icon

            )
        )
        vipItemsList!!.add(
            VipItems(
                "Over Under VIP",
                R.drawable.under_over

            )
        )
    }
}