/*******************************************************************************
 * 
 * js/config/final.js
 * 
 */

snowflake.web.WebContextUtils.init(function(factory) {

	Snowflake.getContext = function() {
		return snowflake.context.Context.getInstance();
	};

});
