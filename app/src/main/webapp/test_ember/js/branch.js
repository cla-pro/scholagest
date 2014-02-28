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
    // values = ['reading', 'branchEdition', 'examEdition', 'examCreation']
    state: null,
    newExamName: null,
    
	isBranchEditing: function() {
        return this.get('state') === 'branchEdition';
    }.property('state'),
	isExamEditing: function() {
        return this.get('state') === 'examEdition';
    }.property('state'),
	isExamCreating: function() {
        return this.get('state') === 'examCreation';
    }.property('state'),
	
	init: function() {
        this._super();
        this.set('state', 'reading');
    },
	
	actions: {
        startExamCreation: function() {
            this.set('state', 'examCreation');
        },
        createExam: function() {
            var exam = this.store.createRecord('exam', { 
                name: this.get('newExamName'),
                coeff: 1,
                branchPeriod: this.get('model')
            });
            
            exam.save();
            this.set('state', 'normal');
        },
		editBranch: function() {
			this.set('state', 'branchEdition');
		},
		saveBranch: function() {
			var model = this.get('model');
			var branch = model.get('branch');
			if (branch.get('isDirty')) {
				branch.save();
			}
			
			this.set('state', 'reading');
		},
		editExams: function() {
	        this.set('state', 'examEdition');
	    },
	    saveExams: function() {
	    	var branchPeriod = this.get('model');
	    	branchPeriod.get('exams').forEach(function(exam) {
	    		if (exam.get('isDirty')) {
	    			exam.save();
	    		}
	    	});
	        this.set('state', 'reading');
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