var solr = {
		q : "",
		url : "ui" // RequestHandler
}

$.fn.load_sync = function (url, params, callback) { 
	$.ajaxSetup({async : false});
	this.load(url, params, callback)
    $.ajaxSetup({async : true});
};

function get_lens() {
	return $("#lens").attr("href");
}

function load_results(query) {
	if (query == null) query = "";
	$("#results").fadeTo(0,0.5);
	$("#results").load_sync(query, {
		"v.template" : "ui/results",
	}, function() {
		process_results();
		$("#results").fadeTo(0,1);
	});
}

function load_full_facet(facet_name) {
	var facet = $("#facet-" + facet_name);
	var count = $("li",facet).length;
	$(".more",facet).before($("<div class='tmp'>"));
	var div = $("div.tmp",facet).hide();
	div.load(get_lens() + " ul", {
		"v.template" : "ui/facet_fields",
		"facet.field" : facet_name,
		"facet.sort" : "count",
		"facet.offset" : count,
		rows: 0
	}, function() {
		process_facets();
		div.slideDown();
		$("ul",div).unwrap();
		var new_count = $("li",facet).length;
		if (new_count == count) {
			$("a.more",facet).fadeOut();
		}
	});
	
}

function submit_query() {
	solr.q = $("#query_input").val();
	load_results("?q=" + solr.q);
}

var timeout_instant_search;

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

function setup_query_form() {
	/*
	$("#query_form").submit(function() {
		clearTimeout(timeout_instant_search);
		submit_query();
		return false;
	});

	$("#query_input").inputChange(function() {
		clearTimeout(timeout_instant_search);
		timeout_instant_search = setTimeout(submit_query,500);
	});
	*/
	$("#query_input").focus();
}

var loading_next_page = false;

function load_next_page(async) {
	if (async == null)
		async = false;
	
	if (loading_next_page)
		return
	loading_next_page = true;
	
	$.ajax({
		type  : "GET",
		url : solr.url,
		data :  {
			"v.template" : "ui/docs",
			q : solr.q,
			fq : filter_to_fq(solr.filter),
			facet: "off",
			start : $(".doc").length
		}, 
		cache: false,
		async: async,
		success: function(data) {
			if (data.length == 0) {
				$("#next_page").hide();
			} else {
				$("#docs").append(data);
				process_docs();
			}
			loading_next_page = false;
		}
	});
}

function load_next_page_async() {
	load_next_page(true);
}

function setup_next_page_link() {
	$("#next_page a").unbind().click(function() {
		load_next_page();
		return false;
	});
}

function is_next_page_needed() {
	if ($("#next_page").is(':visible')) { 
		var advance_in_pixel = 200;
		var div_top = $("#next_page").position().top;  
		var window_bottom = $(window).scrollTop() + $(window).height();
		return div_top - window_bottom < advance_in_pixel;
	} 
	return false;
}


function setup_continous_scrolling() {
	$(window).scroll(function(data) {
		if (is_next_page_needed())
			load_next_page_async();
	});
}

function setup_debug_box() {
	var div = $("<div>").attr("id","debug").hide();
	$("body").prepend(div);
	
}

function debug(html) {
	$("#debug").html(html).show();
}

function hide_pagination() {
	$(".pagination").hide();
}

function process_filters() {
	$("#filters a").unbind().click(function() {
		url = $(this).attr("href");
		load_results(url);
		return false;
	});
}

function process_results() {
	process_docs();
	process_facets();
	process_filters();
	hide_pagination();
	while (is_next_page_needed())
		load_next_page();

	setup_next_page_link();
}

function load_detail(doc) {
	var doi = $(".info .doi", doc).text();
	var detail = $(".full",doc);
	if (detail.text().length == 0) { // not already loaded
		detail.load(solr.url + "-detail", {
			q : 'doi:"' + doi + '"'
		});
	};
	
}
	
function process_docs() {
	$(".doc a").unbind();
	$(".doc .title a").click(function() {
		var doc = this.parentNode.parentNode;
		var detail = $(".full", doc);
		load_detail(doc);
		$(".short", doc).slideToggle();
		$(".full", doc).slideToggle();
		return false;
	});

	$(".doc .score a").click(function() {
		var score = this.parentNode;
		$(".exp", score).slideToggle();
		return false;
	});
	$("a").attr("target", "_blank");
}

var timeout_preview_filter;

function process_facets() {
	$(".facet").each(function() {
		var id = $(this).attr("id");
		var facet = id.replace(/facet-/,"");
		
		var data = $(".facet_data", this);
		$("h3",this).unbind().click(function() {
			data.slideToggle();
		});
		$("a.more",this).show().unbind().click(function() {
			load_full_facet(facet);
			return false;
		});
		$("li", this).unbind().each(function() {
			var url = $("a",this).attr("href");
			var value = $("span.value",this).text();
			$(this).click(function() {
				load_results(url);
				$(window).scrollTop(0);
				return false;
			});
			$(this).hover(function() {
				clearTimeout(timeout_preview_filter);
				timeout_preview_filter = setTimeout(function() {
					preview_filter(url);
				}, 250);
			}, function() {
				clearTimeout(timeout_preview_filter);
				$(".doc").fadeTo(0,1);
			});
		});
		var hasVisibleElements = $("li:visible",this).length != 0;
		$(this).toggle(hasVisibleElements);
	});
	
	var hasVisibleElements = $(".facet:visible").length != 0;
	$("#facets").toggle(hasVisibleElements);
	
}

function preview_filter(query) {
	$.ajax({
		type  : "GET",
		url : "ui-ids" + query,
		data :  {
			rows : $(".doc").length
		}, 
		dataType : "json",
		cache: false,
		async: false,
		success: function(data) {
			var ids = data.response.docs.map(function(elem){
				return elem.dataset_id;
			});
			$(".doc").each(function() {
				var id = $(this).attr("id");
				id = id.replace(/^result-/,"");
				if (ids.indexOf(id) == -1) {
					$(this).fadeTo(0,0.5);
				}
			});
		}
	});
}

$(document).ready(function() {
	jQuery.ajaxSettings.traditional = true; // use correct array serialization
											// for solr
	setup_debug_box();
	setup_next_page_link();
	setup_continous_scrolling();
	setup_query_form();
	process_results();
});
