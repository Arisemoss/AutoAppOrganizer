package com.autoapporganizer.core.model

import com.autoapporganizer.util.PrefsManager

/**
 * Factory that creates the appropriate [VisionModelService] based on the
 * user's provider preference.
 */
object VlmServiceFactory {

    fun create(prefs: PrefsManager): VisionModelService {
        return when (prefs.vlmProvider) {
            PrefsManager.PROVIDER_LOCAL -> LocalVlmService(prefs)
            else -> CloudVlmService(prefs)
        }
    }
}
