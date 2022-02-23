package net.oc_soft

import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.dom.get
import org.w3c.fetch.RequestInit

/**
 * manage site related information
 */
class Site {

    /**
     * class instance
     */
    companion object {

        /**
         * request url
         */
        val requestUrl: URL 
		get() = URL(window["oc"].sendMessage.ajax.url as String)
            
    }
}

// vi: se ts=4 sw=4 et:
