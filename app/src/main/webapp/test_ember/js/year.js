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
	setupController: function(controller, model) {
	    controller.set('model', model);
	    controller.set('runningYear', this.getRunningYear(model));
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
    isStarting: false,
    newYearName: "",
    isRenaming: false,
    runningYear: null,

    isStarted: function() {
        return !this.get('isStopped');
    }.property('isStopped', 'runningYear'),
    
    isStopped: function() {
        return !this.get('runningYear');
    }.property('runningYear'),
    
    actions: {
        startYear: function() {
            this.set('isStarting', true);
        },
        createYear: function() {
            this.set('isStarted', true);
            this.set('isStarting', false);
            this.set('isStopped', false);
            
            var newYear = this.store.createRecord('year', {
                running: true,
                name: this.get('newYearName')
            });
            newYear.save();
            
            this.set('runningYear', newYear);
        },
        stopYear: function() {
            this.set('isStarted', false);
            this.set('isStopped', true);
            this.set('isRenaming', false);
            
            var running = this.get('runningYear');
            running.set('running', false);
            running.save();
            
            this.get('model').set('runningYear', null);
        },
        startRenaming: function() {
            this.set('isRenaming', true);
        },
        renameYear: function() {
            this.set('isRenaming', false);

            this.get('runningYear').save();
        }
    }
});