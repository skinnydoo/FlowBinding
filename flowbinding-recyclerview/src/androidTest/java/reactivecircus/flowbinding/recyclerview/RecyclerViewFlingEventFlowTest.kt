package reactivecircus.flowbinding.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import reactivecircus.flowbinding.recyclerview.fixtures.RecyclerViewFragment
import reactivecircus.flowbinding.recyclerview.test.R
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class RecyclerViewFlingEventFlowTest {

    @Test
    fun recyclerViewFlingEvents_vertical() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<FlingEvent>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter(50)
                }
            }
            recyclerView.flingEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            recyclerView.fling(0, 1000)
            val event = recorder.takeValue()
            assertThat(event.view)
                .isEqualTo(recyclerView)
            assertThat(event.velocityX)
                .isEqualTo(0)
            assertThat(event.velocityY)
                .isEqualTo(1000)

            cancelTestScope()
            recorder.clearValues()

            recyclerView.fling(0, -1000)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun recyclerViewFlingEvents_horizontal() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<FlingEvent>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter(20)
                    (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
                }
            }
            recyclerView.flingEvents().recordWith(recorder)

            recorder.assertNoMoreValues()

            recyclerView.fling(1000, 0)
            val event = recorder.takeValue()
            assertThat(event.view)
                .isEqualTo(recyclerView)
            assertThat(event.velocityX)
                .isEqualTo(1000)
            assertThat(event.velocityY)
                .isEqualTo(0)

            cancelTestScope()
            recorder.clearValues()

            recyclerView.fling(-1000, 0)
            recorder.assertNoMoreValues()
        }
    }

    @Test
    fun recyclerViewFlingEvents_notHandled() {
        launchTest<RecyclerViewFragment> {
            val recorder = FlowRecorder<FlingEvent>(testScope)
            val recyclerView = getViewById<RecyclerView>(R.id.recyclerView).apply {
                runOnUiThread {
                    adapter = TestAdapter(50)
                }
            }
            recyclerView.flingEvents { false }.recordWith(recorder)

            recorder.assertNoMoreValues()

            recyclerView.fling(1000, 0)
            recorder.assertNoMoreValues()

            cancelTestScope()
            recorder.clearValues()

            recyclerView.fling(0, 1000)
            recorder.assertNoMoreValues()
        }
    }
}
