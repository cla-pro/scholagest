Scholagest.YearsRoute = Ember.Route.extend({
	model: function() {
		return this.store.find('year');
	}
});
Scholagest.YearsController = Ember.ArrayController.extend({});