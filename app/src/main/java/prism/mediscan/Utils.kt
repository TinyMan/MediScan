package prism.mediscan

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView

/**
 * Created by rapha on 30/01/2018.
 */
/**** Method for Setting the Height of the ListView dynamically.
 * Hack to fix the issue of not showing all the items of the ListView
 * when placed inside a ScrollView   */
fun setListViewHeightBasedOnChildren(listView: ListView) {
    val listAdapter = listView.getAdapter() ?: return

    val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED)
    var totalHeight = 0
    var view: View? = null
    for (i in 0 until listAdapter.getCount()) {
        view = listAdapter.getView(i, view, listView)
        if (i == 0)
            view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT)

        view!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
        totalHeight += view.measuredHeight
    }
    val params = listView.getLayoutParams()
    params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
    listView.setLayoutParams(params)
}

val PRESENTATION = "PRESENTATION"