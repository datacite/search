advanced = {
	text_fields : ["doi", "title", "creator", "publisher", "contributor", "format", 
	               "subject", "date", "description", "resourceType",
	               "relatedIdentifier", "alternateIdentifier"],
	range_fields : ["publicationYear"],
	init : function() {
		$("#advanced_form").submit(advanced.submit);
	},
	init_for_dialog : function() {
		var q_input = $('<input type="text" id="advanced_q"/>');
		var q_hidden = $('<input type="hidden" id="advanced_q_hidden" name="q"/>');
		$("#advanced_fieldset_search").append(q_input, q_hidden);
		this.init();
	},
	submit : function() {
		var q_input = $("#advanced_q");
		var q_hidden = $("#advanced_q_hidden");
		var q = q_input.val();
		advanced.text_fields.forEach(function(field) {
			q += advanced.parse_text_field(field);
		});
		advanced.range_fields.forEach(function(field) {
			q += advanced.parse_range_field(field);
		});
		q = $.trim(q);
		if (q == "")
			q = "*";
		q_hidden.val(q);
	},
	parse_text_field : function(field) {
		var val = $("#advanced_field_" + field).val();
		val = $.trim(val);
		if (val == "")
			return "";
		else if (!val.match(/ /))
			return " " + field + ":" + val;
		else
			return " " + field + ":(" + val + ")";
	},
	parse_range_field : function(field) {
		var from = $("#advanced_field_" + field + "_from").val();
		var to = $("#advanced_field_" + field + "_to").val();
		if (from == "" && to == "")
			return "";
		else if (to == "") 
			return " " + field + ":" + from;
		else 
			return " " + field + ":[" + from + " TO " + to + "]";
	}
}

$(document).ready(advanced.init);