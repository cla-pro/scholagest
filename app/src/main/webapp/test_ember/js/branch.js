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
	isBranchCreation: null,
	newBranchInfo: null,
	
	init: function() {
		this.set('isBranchCreation', false);
		this.set('newBranchInfo', {
			name: '',
			numerical: true
		})
	},
	
	actions: {
		test: function() {
			var b = this.store.find('branch', '3');
			alert(b.get('name'));
		},
		startBranchCreation: function() {
			this.set('isBranchCreation', true);
		},
		createBranch: function() {
			var clazz = this.get('model');
			var info = this.get('newBranchInfo');
			var branch = this.store.createRecord('branch', {
				name: info.name,
				numerical: info.numerical,
				clazz: clazz
			});
			
			// The server must create the branchPeriod
			branch.save();
			
			this.set('isBranchCreation', false);
		}
	}
});

Scholagest.PeriodRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('period', params.period_id);
	}
});
Scholagest.PeriodController = Ember.ObjectController.extend({
});

Scholagest.BranchPeriodRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('branchPeriod', params.branch_period_id);
	}
});
Scholagest.BranchPeriodController = Ember.ObjectController.extend({
	isBranchEditing: false,
	isExamEditing: false,
	
	actions: {
		editBranch: function() {
			this.set('isBranchEditing', true);
		},
		saveBranch: function() {
			var model = this.get('model');
			var branch = model.get('branch');
			if (branch.get('isDirty')) {
				branch.save();
			}
			
			this.set('isBranchEditing', false);
		},
		editExams: function() {
	        this.set('isExamEditing', true);
	    },
	    saveExams: function() {
	    	var branchPeriod = this.get('model');
	    	branchPeriod.get('exams').forEach(function(exam) {
	    		if (exam.get('isDirty')) {
	    			exam.save();
	    		}
	    	});
	        this.set('isExamEditing', false);
	    },
		saveGrades: function() {
			var branchPeriod = this.get('model');
			branchPeriod.get('studentResults').forEach(function(studentResult) {
				studentResult.get('results').forEach(function(result) {
					if (result.get('isDirty')) {
						result.save();
					}
				});
				
				if (!branchPeriod.get('numerical')) {
				    var mean = studentResult.get('mean');
				    if (mean.get('isDirty')) {
				        mean.save();
				    }
				}
			});
		}
	}
});