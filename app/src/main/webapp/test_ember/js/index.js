Scholagest.IndexRoute = Ember.Route.extend({
	redirect: function() {
		this.transitionTo('students');
	}
});
Scholagest.IndexController = Ember.ObjectController.extend({});