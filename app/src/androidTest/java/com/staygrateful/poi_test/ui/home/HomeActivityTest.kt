package com.staygrateful.poi_test.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.ui.BaseInstrument
import com.staygrateful.poi_test.ui.presentation.home.view.HomeActivity
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
class HomeActivityTest : BaseInstrument() {

    @get : Rule
    val composeTestRule = createAndroidComposeRule(HomeActivity::class.java)

    override fun setUp() {

    }

    @Test
    fun title_isDisplayed() {
        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.app_name)
            )
            .assertIsDisplayed()
    }

    @Test
    fun maps_isDisplayed() {
        composeTestRule.onRoot().printToLog("MY TAG")

        composeTestRule
            .onNodeWithTag(composeTestRule.activity.getString(R.string.test_tag_maps))
            .assertIsDisplayed()
    }
}