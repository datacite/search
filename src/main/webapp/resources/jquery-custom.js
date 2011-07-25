// Wrapper to use jquery's load function synchronously 
$.fn.load_sync = function (url, params, callback) { 
	$.ajaxSetup({async : false});
	this.load(url, params, callback)
    $.ajaxSetup({async : true});
};

$.fn.inputChange = function (handler) {
	var oldVal = $(this).val();
	$(this).keyup(function() {
		var val = $(this).val();
		if (val != oldVal) {
			handler();
			oldVal = val;
		}
	});
}
