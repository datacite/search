// Wrapper to use jquery's load function synchronously 
$.fn.load_sync = function(url, params, callback) { 
	$.ajaxSetup({async : false});
	this.load(url, params, callback);
    $.ajaxSetup({async : true});
    return this;
};

$.fn.inputChange = function(handler) {
	var oldVal = $(this).val();
	$(this).keyup(function() {
		var val = $(this).val();
		if (val != oldVal) {
			handler();
			oldVal = val;
		}
	});
}

$.extend({
	base64urlEncode: function(input) {
		var base64 = $.base64Encode(input);
		var base64url = base64.replace(/\+/g, "-").replace(/\//g, "_").replace(/=*$/g, "");
		return base64url;
	}
});

$.fn.modalDialog = function(title, width_auto, height_auto) {
	var height = $(window).height() * 0.8;
	var width = $(window).width() * 0.8;
	return this.dialog({
		autoOpen: false,
		width: width_auto ? "auto" : width,
		height: height_auto ? "auto" : height,
		modal: true,
		title: title
	});
}
