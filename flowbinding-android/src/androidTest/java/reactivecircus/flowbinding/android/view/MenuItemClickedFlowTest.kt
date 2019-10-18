package reactivecircus.flowbinding.android.view

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.android.testutil.TestMenuItem
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class MenuItemClickedFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun menuItemClicks() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val menuItem = TestMenuItem(appContext)
            menuItem.clicks().recordWith(recorder)

            recorder.assertNoMoreValues()

            menuItem.performClick()
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            menuItem.performClick()
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun menuItemClicks_notHandled() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val menuItem = TestMenuItem(appContext)
            menuItem.clicks { false }.recordWith(recorder)

            recorder.assertNoMoreValues()

            menuItem.performClick()
            recorder.assertNoMoreValues()

            cancelTestScope()

            menuItem.performClick()
            recorder.assertNoMoreValues()
        }
    }
}