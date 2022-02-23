package net.oc_soft

import kotlin.js.Promise

import kotlinx.browser.document

import org.w3c.dom.HTMLElement
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get


/**
 * dialog to show form contents
 */
class VerificationDialog {

    /**
     * dialog element to show form contents
     */
    val dialogElement: HTMLElement?
        get() {
            return document.querySelector(".oc-send-message-viewer")?.let {
                it as HTMLElement
            }
        }

    /**
     * ok elements
     */
    val okElements: Array<HTMLElement>
        get() {
            return dialogElement?.let {
                val elements = it.querySelectorAll(".ok")
                Array<HTMLElement>(elements.length) {
                    elements[it] as HTMLElement 
                }
            }?: emptyArray<HTMLElement>()
        }
    /**
     * cancel elements
     */
    val cancelElements: Array<HTMLElement>
        get() {
            return dialogElement?.let {
                val elements = it.querySelectorAll(".cancel")
                Array<HTMLElement>(elements.length) {
                    elements[it] as HTMLElement 
                }
            }?: emptyArray<HTMLElement>()
        }

    /**
     * element to show verification contents
     */
    val contentsElement: HTMLElement? 
        get() {
            return dialogElement?.let {
                it.querySelector(".contents")?.let {
                    it as HTMLElement
                }
            }
        }

    /**
     * get title element containing title
     */
    val titleElement: HTMLElement?
        get() {
            return dialogElement?.let {
                it.querySelector(".title")?.let {
                    it as HTMLElement
                }
            }
        }



    /**
     * handle ok event
     */
    var okEventHdlr: ((Event)->Unit)? = null

    /**
     * handle cancel event
     */
    var cancelEventHdlr: ((Event)->Unit)? = null


    /**
     * handle backdrop event
     */
    var backdropEventHdlr: ((Event)->Unit)? = null


    /**
     * bind this object into html elements
     */
    fun bind() {

        dialogElement?.let {
            val elem = it
            val fallBackDialog = js("!elem.showModal")
            dialog.registerDialog(elem)
            if (fallBackDialog) {
                elem.classList.add("fallback")
            }
        }
    }
    /**
     * detach this object from html elements
     */
    fun unbind() {
    }



    /**
     * attach event listeners into html elements
     */
    fun attachListeners(): Promise<Boolean> {

        return Promise<Boolean> {
            resolve, reject ->
            val okEventHdlr: (Event)->Unit = { 
                if (handleOkEvent(it)) {
                    resolve(true)
                    detachListeners()
                }
            }
            val cancelEventHdlr: (Event)->Unit = { 
                if (handleCancelEvent(it)) {
                    resolve(false)
                    detachListeners()
                }
            }

            val backdropEventHdlr: (Event)->Unit = {
                handleBackdropEvent(it)?.let {
                    resolve(it)
                    detachListeners()
                } 
            }

            okElements.forEach {
                it.addEventListener("click", okEventHdlr)
            }
            cancelElements.forEach {
                it.addEventListener("click", cancelEventHdlr)
            }
            document.body?.let {
                it.addEventListener("click", backdropEventHdlr)
            }
            this.okEventHdlr = okEventHdlr
            this.cancelEventHdlr = cancelEventHdlr
            this.backdropEventHdlr = backdropEventHdlr
        }
    } 

    /**
     * detach event listeners from html elements
     */
    fun detachListeners() {
        backdropEventHdlr?.let {
            val hdlr = it
            document.body?.let {
                it.removeEventListener("click", hdlr)
            }
            backdropEventHdlr = null
        }
        okEventHdlr?.let {
            val hdlr = it
            okElements.forEach {
                it.removeEventListener("click", hdlr)
            }
            this.okEventHdlr = null
        }
        cancelEventHdlr?.let {
            val hdlr = it
            cancelElements.forEach {
                it.removeEventListener("click", hdlr)
            }
            this.cancelEventHdlr = null
        }
    } 


    /**
     * show verified contents on modal dialog.
     */
    fun showModal(title : String, contents: String): Promise<Boolean> {
        return titleElement?.let {
            val titleElement = it
            contentsElement?.let {
                val contentsElement = it
                dialogElement?.let {
                    titleElement.innerText = title
                    contentsElement.innerHTML = contents
                    val res = attachListeners()
                    showModal(it)
                    res
                }?: Promise.resolve(false)
            }?: Promise.resolve(false)
        }?: Promise.resolve(false)
    }

    /**
     * show dialog
     */
    fun showModal(element: HTMLElement) {
        if (js("element.showModal !== void 0") as Boolean) {
            js("element.showModal()")
        }
    }

    /**
     * close dialog
     */
    fun close(element: HTMLElement) {
        if (isOpen(element)) { 
            if (js("element.close !== void 0") as Boolean) {
                js("element.close()")
            }
        }
    }

    /**
     * you get true if element is open
     */
    fun isOpen(element: HTMLElement): Boolean {
        return if (js("element.open !== void 0") as Boolean) {
            js("element.open") as Boolean
        } else {
            false
        }
    }


    /**
     * handle ok event
     */
    fun handleOkEvent(event: Event): Boolean {
        dialogElement?.let {
            close(it)
        }
        event.preventDefault()
        event.stopPropagation()
        return true
    }


    /**
     * handle cancel event
     */
    fun handleCancelEvent(event: Event): Boolean {
        dialogElement?.let {
            close(it)
        }
        event.preventDefault()
        event.stopPropagation()
        return false 
    }

    /**
     * handle backdrop event
     */
    fun handleBackdropEvent(event: Event): Boolean? {

        return if (event is MouseEvent) {
            if (!isInDialog(event.currentTarget as Element,
                event.clientX,
                event.clientY)) {
                event.preventDefault()
                event.stopPropagation()  
                dialogElement?.let {
                    close(it)
                }
                false 
            } else {
                null
            }
        } else {
            null
        }
    }

    /**
     * you get true if container coordinate is in dialog element
     */
    fun isInDialog(
        containerElement: Element,
        viewPortX: Int,
        viewPortY: Int): Boolean {

        return dialogElement?.let {
            val dialogRect = it.getBoundingClientRect()
            var res = dialogRect.left <= viewPortX 
                && viewPortX <= dialogRect.right
            if (res) {
                res = dialogRect.top <= viewPortY 
                    && viewPortY <= dialogRect.bottom
            }
            res
        }?: false
    }
}
// vi: se ts=4 sw=4 et: 
