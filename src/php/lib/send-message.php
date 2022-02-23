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

        error_log(ini_get('sendmail_path'));

        wp_mail($to, $subject, $body, $headers);
        
    }
}


SendMessage::$instance = new SendMessage;
// vi: se ts=4 sw=4 et:
