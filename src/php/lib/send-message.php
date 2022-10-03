<?php

/**
 * manage sending message 
 */
class SendMessage {

    /**
     * sendmessage instance
     */
    static $instance = null;


    /**
     * send message
     */
    function send($to, $from, $subject, $body) {

        $headers = [];
        $headers[] = 'content-type:text/html';
        if (!empty($from)) {
            $headers[] = "from:$from";
        }

        add_filter('wp_mail_from', function($from) {
            error_log("oc-send-message from: $from");
            return $from;
        });

        add_filter('wp_mail_content_type', function($content_type) {
            error_log("oc-send-message content type: $content_type");
            return $content_type;
        });
        wp_mail($to, $subject, $body, $headers);
        
    }
}


SendMessage::$instance = new SendMessage;
// vi: se ts=4 sw=4 et:
