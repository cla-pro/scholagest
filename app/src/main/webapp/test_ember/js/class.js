Scholagest.ClassRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('class', params.class_id);
	}
});
Scholagest.ClassController = Ember.ObjectController.extend({});