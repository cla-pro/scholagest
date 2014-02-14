Scholagest.IndexRoute = Ember.Route.extend({
	redirect: function() {
		this.transitionTo('login');
	}
});
Scholagest.IndexController = Ember.ObjectController.extend({});