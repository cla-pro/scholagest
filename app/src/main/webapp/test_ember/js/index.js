Scholagest.IndexRoute = Ember.Route.extend({
	redirect: function() {
		this.transitionTo('teachers');
	}
});
Scholagest.IndexController = Ember.ObjectController.extend({});