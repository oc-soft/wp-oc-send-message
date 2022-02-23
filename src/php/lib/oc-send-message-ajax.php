<?php
require_once implode('/', [__DIR__, 'shortcode.php']);

class OcSendMessageAjax {


    /**
     * send message ajax object
     */
    static $instance;

    /**
     * reply header image urls request
     */
    function reply_verify() {

        if (!empty($_REQUEST['id'])
            && !empty($_REQUEST['type'])) {
            $query = new WP_Query([
                'post_type' => $_REQUEST['type'],
                'post__in' => (array)$_REQUEST['id']
            ]);
            if ($query->have_posts()) {
                Shortcode::$instance->register();
            }

            $res = [];
            while ($query->have_posts()) {
                $query->the_post();
                $res[] = [
                    'title' => get_the_title(),
                    'contents' => do_shortcode(get_the_content())
                ];
            }
            wp_reset_query();

            echo json_encode($res);
        }
        wp_die();
    }



    /**
     * setup
     */
    function setup() {
        $prefixes = [
            'wp_ajax',
            'wp_ajax_nopriv'
        ]; 

        $hdlr = function() {
            $this->reply_verify();
        };

        foreach($prefixes as $prefix) {
            add_action("${prefix}_oc-send-message-verify", $hdlr);
        }
    }
}

OcSendMessageAjax::$instance = new OcSendMessageAjax;

// vi: se ts=4 sw=4 et:
