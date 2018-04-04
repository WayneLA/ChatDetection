/**
 * Created by fuookami on 2017/3/21.
 */

var InputTextPlaceholderHandler = function(_input_name) {
    var input = jQuery(_input_name);
    var placeholder_text = input.attr("placeholder");

    this.init_input_text_placeholder_handler = function() {
        input.focus(function(){
            input.attr("placeholder", "");
        });

        input.blur(function(){
            input.attr("placeholder", placeholder_text);
        });
    };

    return this;
};