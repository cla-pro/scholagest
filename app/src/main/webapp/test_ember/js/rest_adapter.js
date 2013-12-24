transformAttributeName = function(name) {
	return "p" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length);
};

Scholagest.TeacherSerializer = DS.RESTSerializer.extend({
	serialize: function(record, options) {
		var json = {};

		record.eachAttibute(function(name) {
			json[transformAttributeName(name)] = record.get(name);
		});

		if (options.includeId) {
			json.POST_ID_ = post.get('id');
		}

		return json;
	}
});