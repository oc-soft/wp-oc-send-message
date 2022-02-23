package net.oc_soft

import kotlinx.browser.window

/**
 * application object
 */
class App (
    /**
     * form object
     */
    val form: Form = Form()){


    /**
     * bind this object into html elements
     */
    fun bind() {
        form.bind()
    }

    /**
     * unbind this object from html elements
     */
    fun unbind() {
        form.unbind()
    }

    /**
     * run application
     */
    fun run() {

        window.addEventListener("load", {
            bind()
        }, object {
            @JsName("once")
            val once = true
        }) 
    

        window.addEventListener("unload", {
            unbind()
        }, object {
            @JsName("once")
            val once = true
        })

    }


}

// vi: se ts=4 sw=4 et:
