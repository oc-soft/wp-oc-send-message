<?php

require_once implode('/', [__DIR__, 'send-message.php']);
/**
 * register, render wordpress shortcode
 */
class Shortcode {

    /**
     *  get request contents 
     */
    function get_request_contents($attr, $content) {
        $result = '';
        $title = '';
        if (isset($attr['id'])) {
            $query = new WP_Query(['page_id' => $attr['id']]);
            if ($query->have_posts()) {
                $query->the_post();
                $title = get_the_title();
                ob_start();
                the_content();
                $result = ob_get_clean();
                wp_reset_postdata();
            }
        }
        if (isset($attr['to'])) {
            $from = null;
            if (!empty($attr['from'])) {
                $from = $attr['from'];
            }
            $contents = $result;
            $lang_attr = get_language_attributes();
            $charset = get_bloginfo('charset');
            $body =<<<EOT
<html $lang_attr >
<head>
  <meta charset="$charset" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
$contents
</body>
</html>
EOT;
            SendMessage::$instance->send($attr['to'], $from, $title, $body);
        }
        return $result;
    }


    /**
     * get request value
     */
    function get_request($attr, $content) {

        $result = '';
        if (!empty($attr['name']) && !empty($_REQUEST[$attr['name']])) {
            $result = $_REQUEST[$attr['name']];
            if (isset($attr[$result])) {
                $result = $attr[$result];
            }
            $result = nl2br(esc_html($result, false));
        } else if (!empty($attr['alt'])) {
            $result = $attr['alt'];
        }
        return $result;
    }


    /**
     * short code singleton
     */
    static $instance = null;

    /**
     * register shortcode
     */
    function register() {
        add_shortcode('oc_request_field', function(
            $attr, $contents, $tag) {
            return $this->get_request($attr, $contents, $tag);
        });

        add_shortcode('oc_request_contents', function(
            $attr, $contents, $tag) {
            return $this->get_request_contents($attr, $contents, $tag);
        });
    }
}


Shortcode::$instance = new Shortcode;


// vi: se ts=4 sw=4 et:
