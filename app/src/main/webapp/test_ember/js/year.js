Scholagest.YearsRoute = Ember.Route.extend({
	model: function() {
		var years = this.store.find('year');
		//var runningYear = this.getRunningYear(years);
		
//		return {
//		    years: years,
//		    runningYear: runningYear
//		}
		return years
	},
	// Called after the init method of the controller
	setupController: function(controller, model) {
	    controller.set('model', model);
	    controller.set('runningYear', this.getRunningYear(model));
    	if (controller.get('runningYear')) {
    		controller.set('state', 'running');
    	} else {
    		controller.set('state', 'stopped');
    	}
	},
	
	getRunningYear: function(years) {
        var runningYear = null;
        years.forEach(function(year) {
            if (year.get('running') == true) {
                runningYear = year;
            }
        });
        return runningYear;
    }
});

Scholagest.YearsController = Ember.ArrayController.extend({
	// values = ['starting', 'running', 'stopped', 'stopping', 'renaming', 'classCreation']
	state: null,
    newYearName: '',
    newClassName: '',
    runningYear: null,

	isStarting: function() {
	    return this.get('state') === 'starting';
	}.property('state'),
	isNotRunning: function() {
	    return !(this.get('state') === 'running');
	}.property('state'),
    isNotStopped: function() {
	    return !(this.get('state') === 'stopped');
    }.property('state'),
    isStopping: function() {
	    return this.get('state') === 'stopping';
    }.property('state'),
    isRenaming: function() {
	    return this.get('state') === 'renaming';
    }.property('state'),
    isClassCreation: function() {
	    return this.get('state') === 'classCreation';
    }.property('state'),
    
    actions: {
        startYear: function() {
            this.set('state', 'starting');
        },
        createYear: function() {
            var newYear = this.store.createRecord('year', {
                running: true,
                name: this.get('newYearName')
            });
            newYear.save();
            
            this.set('state', 'running');
            this.set('newYearName', '');
        },
        stopYear: function() {
            var running = this.get('runningYear');
            running.set('running', false);
            running.save();
            
            this.set('runningYear', null);
            this.set('state', 'stopped');
        },
        startRenaming: function() {
            this.set('state', 'renaming');
        },
        renameYear: function() {
            this.set('state', 'running');

            this.get('runningYear').save();
        },
        startCreatingClass: function() {
        	this.set('state', 'classCreation');
        },
        createClass: function() {
        	var runningYear = this.get('runningYear');
        	var clazz = this.store.createRecord('class', { 
				name: this.get('newClassName'),
				year: runningYear
			});
        	clazz.save().then(function() {
            	runningYear.get('classes').pushObject(clazz);
            	runningYear.save();
        	}, function() {
        	  // Error callback
        	});
        	
        	this.set('state', 'running');
            this.set('newClassName', '');
        }
    }
});