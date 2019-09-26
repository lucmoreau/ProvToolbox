(function( $ ){
	
	var methods = {
		init : function( options ) {
			var $wrapper = this; //expecting a wrapper in selector
			
			//default options, overriden by the options param
			var settings = $.extend({
				selector : this.selector,
				offset: 0, //multiplier; not a css unit
				overflow: "hidden"
			}, options);
			
			//FYI: Math.abs() always returns a positive number
			settings.offset = Math.abs(settings.offset);
			
			/*
				first element will be this many items to the left, and the rest will be to the right of it
				For example, if #wrapper is 500px and settings.offset is 1 then "#wrapper > div:first" will be "left: -500px;"
			*/
			var lastposition = (settings.offset - (settings.offset * 2));
			//how much each slide should move. This is the 500px mentioned in the example.
			var increase = $wrapper.width();
			var $children = $wrapper.children();
			this.children().each(function(){
				jQuery(this).css({left: (lastposition * increase)+"px", position: "absolute", "width": "100%"});
				if((lastposition * increase) === 0) jQuery(this).addClass("slidetoActive");
				lastposition++;
			});
			this.css({
				//because the children are absolute, the height of #wrapper will be 0px unless we set it explicitly
				"min-height": $children.eq(settings.offset).height() + "px",
				"overflow": settings.overflow,
				"position": "relative"
			}).addClass("slidetoWrapper");
			//ready to slide.
		},
		slide : function( options ) {
			var $elem = this; //expecting a slideto-child in selector
			
			var settings = $.extend({
				instant : false, //animate movement or just css it
				selector : this.selector,
				speed: "fast"
			}, options);
			
			var $wrapper = $elem.parents(".slidetoWrapper").first();
			//set wrapper height to fit $elem, and change the slidetoActive
			$wrapper.css("min-height", $elem.height() + "px").children(".slidetoActive").removeClass("slidetoActive");
			$elem.addClass("slidetoActive");
			var currentleft = $elem.css("left");
			//slide all until we get there.
			$wrapper.children().each(function(){
				if(!settings.instant){
					jQuery(this).animate({left: "-="+currentleft}, {duration: settings.speed, queue: "slideto"});
				}else{
					jQuery(this).css({left: "-="+currentleft});
				}
			});
			return $elem;
		},
		next : function( options ){
			var $wrapper = this; //expecting a wrapper in selector
			//using one function for both next() and prev()
			methods.nextprev($wrapper, 1, options);
		},
		prev : function( options ){
			var $wrapper = this; //expecting a wrapper in selector
			methods.nextprev($wrapper, -1, options);
		},
		nextprev : function( $wrapper, direction, options ){
			//loop through children and see which one is active
			var i = 0;
			var current;
			$wrapper.children().each(function(){
				if(jQuery(this).hasClass("slidetoActive")) current = i;
				i++;
			});
			//now either add 1 or -1 to that, and slide there!
			$wrapper.children().eq(current + direction).slideto(options);
		}
	};
	
	$.fn.slideto = function( method ) {
		
		// Method calling logic
		if ( methods[method] ) {
			return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
		} else if ( typeof method === 'object' || ! method ) {
			//simply calling jQuery("#some-element").slideto() will simply slide to that one
			return methods.slide.apply( this, arguments );
		} else {
			$.error( 'Method ' +  method + ' does not exist in jQuery.slideto' );
		}
		

	};
	
})( jQuery );