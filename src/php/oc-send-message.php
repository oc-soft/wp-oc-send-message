<?php
/**
 * Plugin Name: Oc Send message
 * Text Domain: oc-send-message
 */
require_once implode('/', [
    plugin_dir_path( __FILE__), 'lib', 'oc-send-message.php']);
/**
 * activate plugin
 */
function oc_send_message_activate() {

    OcSendMessage::$instance->activate_plugin();
}

/**
 * deactivate plugin
 */
function oc_send_message_deactivate() {
    OcSendMessage::$instance->deactivate_plugin();
}


register_activation_hook(__FILE__, 'oc_send_message_activate');
register_deactivation_hook(__FILE__, 'oc_send_message_deactivate');

OcSendMessage::$instance->run(
    implode('/', [plugin_dir_url(__FILE__), 'js']),
    implode('/', [plugin_dir_url(__FILE__), 'css']));




// vi: se ts=4 sw=4 et:
