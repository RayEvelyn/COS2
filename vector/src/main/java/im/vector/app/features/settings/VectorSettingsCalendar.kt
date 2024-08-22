package im.vector.app.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import im.vector.app.R
import im.vector.app.core.preference.VectorPreference
import im.vector.app.features.analytics.plan.MobileScreen
import im.vector.app.features.raw.wellknown.getElementWellknown
import im.vector.app.features.raw.wellknown.isE2EByDefault
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class VectorSettingsCalendar :
    VectorSettingsBaseFragment() {

    override var titleRes = R.string.calendar
    override val preferenceXmlRes = R.layout.layout_calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analyticsScreenName = MobileScreen.ScreenName.SettingsSecurity // need to figure out how to modify this
    }

    override fun onCreateRecyclerView(inflater: LayoutInflater, parent: ViewGroup, savedInstanceState: Bundle?): RecyclerView {
        return super.onCreateRecyclerView(inflater, parent, savedInstanceState).also {
            // Insert animation are really annoying the first time the list is shown
            // due to the way preference fragment is done, it's not trivial to disable it for first appearance only..
            // And it's not that an issue that this list is not animated, it's pretty static
            it.itemAnimator = null
        }
    }

    override fun onResume() {
        super.onResume()
        session.liveSecretSynchronisationInfo()
            .onEach {
                refresh4SSection(it)
                refreshXSigningStatus()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            findPreference<VectorPreference>(VectorPreferences.SETTINGS_CRYPTOGRAPHY_HS_ADMIN_DISABLED_E2E_DEFAULT)?.isVisible = // this will need to be changed
                rawService
                    .getElementWellknown(session.sessionParams)
                    ?.isE2EByDefault() == false

            refreshXSigningStatus()
            // My device name may have been updated
            refreshMyDevice()
        }
    }
}