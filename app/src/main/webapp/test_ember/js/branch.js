Scholagest.PeriodsRoute = Ember.Route.extend({
	model: function() {
		return this.store.find('period');
	}
});
Scholagest.PeriodsController = Ember.ArrayController.extend({
});

Scholagest.PeriodRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('period', params.period_id);
	}
});
Scholagest.PeriodController = Ember.ObjectController.extend({
});

Scholagest.BranchRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('branch', params.branch_id);
	}
});
Scholagest.BranchController = Ember.ObjectController.extend({
	actions: {
		save: function() {
			var branch = this.get('model');
			branch.get('studentResults').forEach(function(studentResult) {
				studentResult.get('results').forEach(function(result) {
					if (result.get('isDirty')) {
						result.save();
					}
				});
			});
		}
	}
});