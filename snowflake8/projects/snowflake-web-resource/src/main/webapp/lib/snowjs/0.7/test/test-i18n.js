js.i18n.I18n.onload(function(manager) {

	var table = {
		'a' : '苹果',
		'b' : '芭蕉',
		'c-hide' : '樱桃',

		'i18n-xxx' : 'the-zh-xxx',
		'i18n-yyy' : 'the-zh-yyy',
		'i18n-zzz' : 'the-zh-zzz',

	};

	manager.register('zh_CN', table);

});

js.i18n.I18n.onload(function(manager) {

	var table = {
		'a' : 'Apple',
		'b' : 'Banana',
		'c' : 'Cherry',

		'i18n-xxx' : 'the-en-xxx',
		'i18n-yyy' : 'the-en-yyy',
		'i18n-zzz' : 'the-en-zzz',

	};

	manager.register('en_US', table);

});
