/******************
 * General setup functions loaded only once
 *******************/

function init() {
	// use correct array serialization for solr
	jQuery.ajaxSettings.traditional = true; 
	jQuery.support.cors = true;
	
	setup_debug_box();
	setup_query_form();
	setup_history();
	
	options.init();
	
	pagination.init();
	
	process_results();
}

var timeout_instant_search;
function setup_query_form() {
	$("#query_form").submit(function() {
		clearTimeout(timeout_instant_search);
		submit_query();
		return false;
	});

	$("#query_input").inputChange(function() {
		clearTimeout(timeout_instant_search);
		timeout_instant_search = setTimeout(submit_query,500);
	});
	$("#query_input").focus();
}

function setup_debug_box() {
	var div = $("<div>").attr("id","debug").hide();
	$("body").prepend(div);
}

function setup_history() {
	History.Adapter.bind(window,'statechange',function() {
		if (History.skipTrigger) {
			History.skipTrigger = false;
		} else {
			var State = History.getState();
			load_results(State.url);
		}
    });
	History.pushStateWithoutTrigger = function (data, title, url) {
		History.skipTrigger = true;
		History.pushState(data, title, url);
	}
}

/******************
 * General functions
 *******************/

function debug(html) {
	$("#debug").html(html).show();
}

// returns the url of the current results
function get_lens() {
	return $("#lens").attr("href");
}

function get_lens_without_q() {
	return $("#lens_without_q").attr("href");
}

// prevent history.js from prepending '/' to url if it starts with '?'
function fixUrl(url) {
	if (url.match(/^\?/))
		url = "ui" + url;
	return url;
}

/******************
 * functions for loading content
 ******************/

function submit_query() {
	q = $("#query_input").val();
	url = get_lens_without_q() + "&q=" + escape(q);
	load_results(url);
}

function load_results(query) {
	History.pushStateWithoutTrigger(null, null, fixUrl(query));
	$("#results").fadeTo("fast",0.7);
	$("#results").load_sync(query, {
		"v.template" : "ui/results"
	}, function() {
		process_results();
		$("#results").fadeTo("fast",1);
	});
}

function load_main(query) {
	History.pushStateWithoutTrigger(null, null, fixUrl(query));
	$("#main").fadeTo("fast",0.7);
	$("#main").load_sync(query + " #main", {
		"v.template" : "ui/results",
		"facet" : "off"
	}, function() {
		$("#main", this).unwrap();
		process_docs();
		pagination.process();
		process_filters();
		$("#main").fadeTo("fast",1);
	});
}

function load_detail(doc) {
	var doi = $(".info .doi", doc).text();
	var detail = $(".full",doc);
	if (detail.text().length == 0) { // not already loaded
		detail.load("ui-detail", {
			q : 'doi:"' + doi + '"'
		});
	};
}

function load_more_facet(query, facet_field) {
	var facet = $("#facet-" + facet_field);
	var div = $(".facet_data",facet);
	div.load_sync(query + " .facet_data", {
		"v.template" : "ui/facet_fields",
		"facet.field" : facet_field,
		rows: 0
	}, function() {
		$(".facet_data", div).unwrap();
		process_facets();
	});
}

function preview_filter(query) {
	$.ajax({
		type : "GET",
		url : "ui-ids" + query,
		data : {
			rows : $(".doc").length
		}, 
		dataType : "json",
		cache: false,
		async: false,
		success: function(data) {
			var ids = data.response.docs.map(function(elem) {
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

/******************
 * functions for processing content after loading
 ******************/

function process_results() {
	process_docs();
	process_facets();
	process_filters();
	pagination.process();
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
		$("a.more",this).unbind().click(function() {
			var query = $(this).attr("href"); 
			load_more_facet(query, facet);
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

function process_filters() {
	$("#filters a").unbind().click(function() {
		url = $(this).attr("href");
		load_results(url);
		return false;
	});
}

/******************
 * Options
 ******************/

var options = {
	opts : new Array(),
	init : function() {
	},
	add : function(name, value, text, hook) {
		this.opts[name] = {
				"value" : value,
				"hook" : hook
		};
		var a = $("<a href='#'>?</a>").click(function() { options.flip(name); return false; });
		var span = $("<span>").attr("id","option-" + name).html(text + " is ").append(a);
		$("#options").append(span);
		this.refreshStatusText(name);
	},
	get : function(name) {
		return this.opts[name].value;
	},
	set : function(name, value) {
		this.opts[name].value = value;
		this.refreshStatusText(name);
		var hook = this.opts[name].hook;
		if (hook != null)
			hook();
	},
	flip : function(name) {
		this.set(name, !this.get(name));
	},
	refreshStatusText : function(name) {
		var text = this.get(name)?"enabled":"disabled";
		$("#option-" + name + " a").html(text);
	}
}

/******************
 * Pagination
 ******************/

var pagination = {
	init : function() {
		options.add("continous", true, "continous scrolling", submit_query);
		this.init_continous();
		this.process();
	},
	init_continous : function() {
		$(window).scroll(function(data) {
			if (pagination.next_page.is_needed())
				pagination.next_page.load(true);
		});
		while (pagination.next_page.is_needed())
			pagination.next_page.load();
	},
	process : function() {
		$(".pagination a").unbind().click(function() {
			url = $(this).attr("href");
			load_main(url);
			return false;
		});
		if (options.get("continous"))
			$(".pagination").hide();
		$("#next_page a").unbind().click(function() {
			pagination.next_page.load();
			return false;
		});
	}
}

pagination.next_page = {
	loading : false,
	load : function(async) {
		if (async == null)
			async = false;
		
		if (this.loading)
			return
		this.loading = true;
		
		$.ajax({
			type  : "GET",
			url : get_lens(),
			data :  {
				"v.template" : "ui/docs",
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
				pagination.next_page.loading = false;
			}
		});
	},
	is_needed : function () {
		if (options.get("continous") && $("#next_page").is(':visible')) { 
			var advance_in_pixel = 200;
			var div_top = $("#next_page").position().top;  
			var window_bottom = $(window).scrollTop() + $(window).height();
			return div_top - window_bottom < advance_in_pixel;
		} 
		return false;
	}
}

/******************
 * Startup
 ******************/

$(document).ready(init);
