Scholagest.PeriodsRoute = Ember.Route.extend({
	model: function() {
        var user = Scholagest.SessionManager.get('user');
        if (user == null) {
            return null;
        } else {
            var userClass = user.get('clazz');
            // Force load of periods
            userClass.get('periods').forEach(function(period) {});
            return userClass;
        }
	}
});
Scholagest.PeriodsController = Ember.ObjectController.extend({
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
	isEditing: false,
	
	actions: {
		editExams: function() {
	        this.set('isEditing', true);
	    },
	    saveExams: function() {
	    	var branch = this.get('model');
	    	branch.get('exams').forEach(function(exam) {
	    		if (exam.get('isDirty')) {
	    			exam.save();
	    		}
	    	});
	        this.set('isEditing', false);
	    },
		saveGrades: function() {
			var branch = this.get('model');
			branch.get('studentResults').forEach(function(studentResult) {
				studentResult.get('results').forEach(function(result) {
					if (result.get('isDirty')) {
						result.save();
					}
				});
				
				if (!branch.get('numerical')) {
				    var mean = studentResult.get('mean');
				    if (mean.get('isDirty')) {
				        mean.save();
				    }
				}
			});
		}
	}
});