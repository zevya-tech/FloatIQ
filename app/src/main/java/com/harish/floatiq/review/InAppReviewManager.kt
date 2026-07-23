package com.harish.floatiq.review

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory
import com.harish.floatiq.storage.EngagementTracker

object InAppReviewManager {

    fun requestReview(
        activity: Activity
    ) {

        val manager =
            ReviewManagerFactory.create(activity)

        val request =
            manager.requestReviewFlow()

        request.addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val reviewInfo =
                    task.result

                manager.launchReviewFlow(
                    activity,
                    reviewInfo
                ).addOnCompleteListener {

                    EngagementTracker
                        .markReviewRequested(
                            activity
                        )
                }

            } else {

                openPlayStore(
                    activity
                )

                EngagementTracker
                    .markReviewRequested(
                        activity
                    )
            }
        }
    }

    fun openPlayStore(
        activity: Activity
    ) {

        val intent = Intent(

            Intent.ACTION_VIEW,

            Uri.parse(
                "https://play.google.com/store/apps/details?id=${activity.packageName}"
            )
        )

        activity.startActivity(
            intent
        )
    }
}