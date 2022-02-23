package net.oc_soft

import kotlinx.browser.window
import kotlinx.browser.document

import kotlin.collections.ArrayList

import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.dom.set

import org.w3c.dom.events.Event
import org.w3c.xhr.FormData
import org.w3c.fetch.RequestInit

/**
 * keep on watching form activity
 */
class Form(
    /**
     * the dialog to confirm user input.
     */
    val verificationDialog: VerificationDialog = VerificationDialog()) {

    /**
     * verification parameter
     */
    data class VerificationParam(
        val type: String,
        val id: String)

    /**
     * forms in documents
     */
    val formElements: Array<HTMLFormElement>
        get() {
            val elems = document.querySelectorAll(
                "form.oc-simple-contact")
    
            return Array<HTMLFormElement>(elems.length) {
                elems[it] as HTMLFormElement
            }
            
        }
    /**
     * elements to submit
     */
    val submitElements: Array<HTMLElement>
        get() {
            val submitElements = ArrayList<HTMLElement>()

            formElements.forEach {
                val submits = it.querySelectorAll("[type=\"submit\"]")
                for (idx in 0 until submits.length) {
                    submitElements.add(submits[idx] as HTMLElement)
                }
            }
            return submitElements.toTypedArray()
        }

    /**
     * elements to verify user input
     */
    val verifyElements: Array<HTMLElement>
        get() {
            val verifyElements = ArrayList<HTMLElement>()
            formElements.forEach {
                val validations = it.querySelectorAll(".verify")
                for (idx in 0 until validations.length) {
                    verifyElements.add(validations[idx] as HTMLElement)
                }
            }
            return verifyElements.toTypedArray()
        }
     
    /**
     * submit event handler
     */
    var submitEventHandler: ((Event)->Unit)? = null

    /**
     * verify event handler
     */
    var verifyEventHandler: ((Event)->Unit)? = null

    /**
     * bind this object into html elements
     */
    fun bind() {
        val submitElements = this.submitElements
        val verifyElements = this.verifyElements

        val submitHdlr: (Event)->Unit = { handleSubmitEvent(it) }
        val verifyHdlr: (Event)->Unit = { handleVerifyEvent(it) }
        
        submitElements.forEach {
            it.addEventListener("click", submitHdlr)
        }
        verifyElements.forEach {
            it.addEventListener("click", verifyHdlr)
        }
        this.submitEventHandler = submitHdlr
        this.verifyEventHandler = verifyHdlr
   
        this.verificationDialog.bind() 
    } 

    /**
     * detach this object from html elements
     */
    fun unbind() {
        this.verificationDialog.unbind()
        submitEventHandler?.let {
            val hdlr = it
            submitElements.forEach {
                it.removeEventListener("click", hdlr)
            }
            submitEventHandler = null
        }
        verifyEventHandler?.let {
            val hdlr = it
            verifyElements.forEach {
                it.removeEventListener("click", hdlr)
            }
            verifyEventHandler = null
        }
    }


    /**
     * handle an event to submit
     */
    fun handleSubmitEvent(event: Event) {
        println("submit")
    }

    /**
     * handle an event to verify contents
     */
    fun handleVerifyEvent(event: Event) {
        
        val target = event.target
        val currentTarget = event.currentTarget
        if (target is Element && currentTarget is HTMLElement) {
             
            getVerificationParam(currentTarget)?.let {
                val verificationParam = it
                findFormFromChild(target)?.let {
                    startVerify(it, verificationParam)
                }
            }
        }

    }

    /**
     * start verify
     */
    fun startVerify(formElement: HTMLFormElement,
        verificationParam: VerificationParam) {

        if (formElement.reportValidity()) { 
            val formData = FormData(formElement)
            formData.set("action", "oc-send-message-verify")
            formData.set("id", verificationParam.id)
            formData.set("type", verificationParam.type)
            window.fetch(Site.requestUrl, object: RequestInit {
                override var method: String? = "POST"
                override var body = formData 
            }).then({
                it.json()
            }).then {
                it?.let {
                    showVerificationData(it, formElement)
                }
            }
        }
    }

    /**
     * show verification data
     */
    fun showVerificationData(verificationData: Any, 
        formElement: HTMLFormElement) {

        if (verificationData is Array<*>) {

            if (verificationData.size > 0) {
                val elem: dynamic = verificationData[0]
                val title = elem.title
                val contents = elem.contents 
                if (title is String && contents is String) {
                    verificationDialog.showModal(title, contents).then {
                        if (it) {
                            formElement.submit()
                        }
                    }
                }
            }
        }
    }
    

    /**
     * find form element which is parent of the element.
     */
    fun findFormFromChild(element: Element): HTMLFormElement? {
        return if (element is HTMLFormElement) {
            element
        } else {
            element.parentElement?.let {
                findFormFromChild(it)
            }
        }
    }
    /**
     * get verification parameter from html element
     */
    fun getVerificationParam(element: HTMLElement): VerificationParam? {
        return element.dataset["verifyId"]?.let {
            val id = it
            element.dataset["verifyType"]?.let {
                VerificationParam(it, id)  
            }
        }
    }
}

// vi: se ts=4 sw=4 et:
