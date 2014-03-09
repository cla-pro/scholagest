Scholagest.PeriodsRoute = Scholagest.AuthenticatedRoute.extend({
    model: function() {
        var user = Scholagest.SessionManager.get('user');
        if (Ember.isNone(user)) {
            return null;
        } else {
            var userClass = user.get('clazz');
            // Force load of periods
            userClass.get('periods').forEach(function(period) {
                period.get('clazz');
            });
            return userClass;
        }
    }
});
Scholagest.PeriodsController = Scholagest.RoleObjectController.extend({
    isBranchCreation: null,
    newBranchInfo: null,
    
    init: function() {
        this.set('isBranchCreation', false);
        this.set('newBranchInfo', {
            name: '',
            numerical: true
        });
    },
    
    isClassTeacher: function() {
        var user = Scholagest.SessionManager.get('user');
        if (Ember.isNone(user) || this.get('isHelpTeacher')) {
            return false;
        } else {
            var myClass = user.get('clazz');
            var thisClass = this.get('model');
            return !Ember.isNone(myClass) && !Ember.isNone(thisClass) && myClass.get('id') === thisClass.get('id');
        }
    }.property('model.clazz', 'Scholagest.SessionManager.user.clazz'),
    
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

Scholagest.PeriodRoute = Scholagest.AuthenticatedRoute.extend({
    model: function(params) {
        return this.store.find('period', params.period_id);
    }
});
Scholagest.PeriodController = Scholagest.RoleObjectController.extend({
});

Scholagest.BranchPeriodRoute = Scholagest.AuthenticatedRoute.extend({
    model: function(params) {
        return this.store.find('branchPeriod', params.branch_period_id);
    }
});
Scholagest.BranchPeriodController = Scholagest.RoleObjectController.extend({
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
    
    isBranchNumerical: function() {
        return this.get('model').get('branch').get('numerical');
    }.property('model.branch.numerical'),
    
    classId: function() {
        var clazz = this.get('model').get('period').get('clazz');
        if (Ember.isNone(clazz)) {
            return null;
        } else {
            return clazz.get('id');
        }
    }.property('model.period.clazz.id'),
    isClassTeacherTest: function() {
        var user = Scholagest.SessionManager.get('user');
        if (Ember.isNone(user) || this.get('isHelpTeacher')) {
            return false;
        } else {
            var myClass = user.get('clazz');
            var classId = this.get('classId');
            return !Ember.isNone(myClass) && myClass.get('id') === classId;
        }
    }.property('isHelpTeacher', 'model', 'classId', 'Scholagest.SessionManager.user.clazz'),
    isClassTeacher: function() {
        var user = Scholagest.SessionManager.get('user');
        if (Ember.isNone(user) || this.get('isHelpTeacher')) {
            return false;
        } else {
            var myClass = user.get('clazz');
            var thisClass = this.get('model').get('period').get('clazz');
            return !Ember.isNone(myClass) && !Ember.isNone(thisClass) && myClass.get('id') === thisClass.get('id');
        }
    }.property('isHelpTeacher', 'model.period.clazz.id', 'Scholagest.SessionManager.user.clazz'),
    
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