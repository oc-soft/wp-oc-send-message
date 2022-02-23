@file:JsModule("dialog-polyfill")
@file:JsNonModule
package dialog
import org.w3c.dom.HTMLElement


external fun registerDialog(dialog: HTMLElement)

// vi: se ts=4 sw=4 et:
