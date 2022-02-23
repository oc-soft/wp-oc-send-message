<?php

require_once implode('/', [__DIR__, 'oc-send-message-ajax.php']);
require_once implode('/', [__DIR__, 'shortcode.php']);

/**
 * manage send message
 */
class OcSendMessage {

    /**
     * classes for html element
     */
    static $dialog_classes = ['oc-send-message-viewer'];

    /**
     * domain name for gettext
     */
    static $i18n_domain = 'oc-send-message';

    /**
     * script handle
     */
    static $script_handle = 'oc-send-message';

    /**
     * javascript name
     */
    static $js_script_name = 'oc-send-message.js';

    /**
     * css style sheet name
     */
    static $css_style_name = 'oc-send-message.css';

    
    /**
     * oc send message instance
     */
    static $instance = null;


    /**
     * activate plugin
     */
    function activate_plugin() {
    } 

    /**
     * deactivate plugin 
     */
    function deactivate_plugin() {
    }

    /**
     * get inline script
     */
    function get_ajax_inline_script() {
        $ajax_url = admin_url('admin-ajax.php');
        $result = <<<EOT
window.oc = window.oc || { }
window.oc.sendMessage = window.oc.sendMessage || { }
window.oc.sendMessage.ajax = window.oc.sendMessage.ajax || { }
window.oc.sendMessage.ajax.url = '$ajax_url'
EOT;
        return $result;
    }


    /**
     * render 
     */
    function render_tag_for_viewer() {
        $classes = implode(' ', self::$dialog_classes);
        $i18n_domain = self::$i18n_domain;
        include implode('/', [__DIR__, 'verification-dialog.php']);
    }



    /**
     * setup css directory
     */
    function setup_style($css_dir) {
        wp_register_style(self::$script_handle,
            implode('/', [$css_dir, self::$css_style_name]));
        wp_enqueue_style(self::$script_handle); 
    }


    /**
     * setup script
     */
    function setup_script($js_dir) {

        wp_register_script(self::$script_handle,
            implode('/', [$js_dir, self::$js_script_name]),
            [], false);

        wp_add_inline_script(
            self::$script_handle,
            $this->get_ajax_inline_script());


        wp_enqueue_script(self::$script_handle);
     }

    /**
     * handle init event
     */
    function on_init(
        $js_dir,
        $css_dir) {

        add_action('wp', function() use($js_dir, $css_dir) {
            $this->setup_style($css_dir);
            $this->setup_script($js_dir);
            Shortcode::$instance->register();
        });
        load_plugin_textdomain(self::$i18n_domain);
    }


    /**
     * handle wp_loaded event
     */
    function on_wp_loaded() {

        // add_filter('request', post_query);
    }

    /**
     * setup plugin in
     */
    function run(
        $js_dir,
        $css_dir) {

        add_action('init', function() use($js_dir, $css_dir) {
            $this->on_init($js_dir, $css_dir);
        });

        add_action('wp_loaded', function() {
            $this->on_wp_loaded();
        });
        add_action('wp_footer', function() {
            $this->render_tag_for_viewer();
        });

        OcSendMessageAjax::$instance->setup();
    }
}

OcSendMessage::$instance = new OcSendMessage;
// vi: se ts=4 sw=4 et:
