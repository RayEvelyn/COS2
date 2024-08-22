package im.vector.app.features.settings

import dagger.hilt.android.AndroidEntryPoint
import im.vector.app.R

@AndroidEntryPoint
abstract class VectorSettingsCalendar :
    VectorSettingsBaseFragment() {

    override var titleRes = R.string.calendar
    override val preferenceXmlRes = R.layout.layout_calendar



}