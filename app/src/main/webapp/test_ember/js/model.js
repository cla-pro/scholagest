// Valid roles: admin, teacher, helpteacher
Scholagest.Role = {
    ADMIN: "ADMIN",
    TEACHER: "TEACHER",
    HELP_TEACHER: "HELPTEACHER"
};

Scholagest.User = DS.Model.extend({
    sessionId: DS.attr(),
    role: DS.attr(),
    teacher: DS.belongsTo('teacher'),
    clazz: DS.belongsTo('class')
});

Scholagest.StudentPersonal = DS.Model.extend({
    street: DS.attr(),
    city: DS.attr(),
    postcode: DS.attr(),
    religion: DS.attr()
});
Scholagest.StudentMedical = DS.Model.extend({
    doctor: DS.attr()
});
Scholagest.Student = DS.Model.extend({
    firstName: DS.attr(),
    lastName: DS.attr(),
    personal: DS.belongsTo('studentPersonal', { async: true }),
    medical: DS.belongsTo('studentMedical', { async: true }),
    
    fullName: function() {
        return this.get('firstName') + " " + this.get('lastName');
    }.property('firstName', 'lastName')
});
Scholagest.TeacherDetail = DS.Model.extend({
    address: DS.attr(),
    email: DS.attr(),
    phone: DS.attr()
});
Scholagest.Teacher = DS.Model.extend({
    firstName: DS.attr(),
    lastName: DS.attr(),
    detail: DS.belongsTo('teacherDetail', { async: true }),
    
    fullName: function() {
        return this.get('firstName') + " " + this.get('lastName');
    }.property('firstName', 'lastName')
});

Scholagest.Year = DS.Model.extend({
    name: DS.attr(),
    running: DS.attr('boolean'),
    classes: DS.hasMany('class', { async: true, embedded: 'always' })
});

Scholagest.Class = DS.Model.extend({
    name: DS.attr(),
    year: DS.belongsTo('year', { async: true }),
    periods: DS.hasMany('period', { async: true }),
    branches: DS.hasMany('branch', { async: true }),
    students: DS.hasMany('student', { async: true, embedded: 'always' }),
    teachers: DS.hasMany('teacher', { async: true, embedded: 'always' })
});

Scholagest.Period = DS.Model.extend({
    name: DS.attr(),
    clazz: DS.belongsTo('class', { async: true }),
    branchPeriods: DS.hasMany('branchPeriod', { async: true })
});
Scholagest.Branch = DS.Model.extend({
    name: DS.attr(),
    numerical: DS.attr('boolean'),
    clazz: DS.belongsTo('class', { async: true }),
    branchPeriods: DS.hasMany('branchPeriod'),
    
    isNumerical: function() {
        if (this.get('numerical') === true) {
            return true;
        } else {
            return false;
        }
    }.property('numerical')
});
Scholagest.BranchPeriod = DS.Model.extend({
    branch: DS.belongsTo('branch'),
    period: DS.belongsTo('period', { async: true }),
    exams: DS.hasMany('exam', { embedded: 'always' }),
    studentResults: DS.hasMany('studentResult', { embedded: 'always' })
    
});
Scholagest.Exam = DS.Model.extend({
    name: DS.attr(),
    coeff: DS.attr('number'),
    branchPeriod: DS.belongsTo('branchPeriod', { async: true })
});

Scholagest.StudentResult = DS.Model.extend({
    branchPeriod: DS.belongsTo('branchPeriod'),
    student: DS.belongsTo('student', { async: true }),
    results: DS.hasMany('result'),
    mean: DS.belongsTo('mean')
});
Scholagest.Result = DS.Model.extend({
    grade: DS.attr(),
    exam: DS.belongsTo('exam'),
    studentResult: DS.belongsTo('studentResult'),
    
    // Hack required for the mean to be updated.
    changeCounter: DS.attr('number', { defaultValue: 0 }),
    coeffChange: function() {
        this.set('changeCounter', this.get('changeCounter') + 1);
    }.observes('exam.coeff')
});
Scholagest.Mean = DS.Model.extend({
    grade: DS.attr(),
    studentResult: DS.belongsTo('studentResult'),
    
    branch: function() {
        var studentRes = this.get('studentResult');
        if (Ember.isNone(studentRes)) {
            return null;
        }
        
        var branchPeriod = studentRes.get('branchPeriod');
        if (Ember.isNone(branchPeriod)) {
            return null;
        } else {
            return branchPeriod.get('branch');
        }
    }.property('studentResult.branchPeriod.branch'),
    computedMean: function() {
        var studentRes = this.get('studentResult');
        var branch = this.get('branch');
        
        if (Ember.isNone(studentRes) || Ember.isNone(branch)) {
            return "";
        }
        
        if (branch.get('numerical') === true) {
            var total = 0.0;
            var totalCoeff = 0.0;
            
            studentRes.get('results').forEach(function (result) {
                var grade = result.get('grade');
                var exam = result.get('exam');
                var coeff = exam.get('coeff');
                if (!Ember.isEmpty(grade) && !Ember.isEmpty(coeff)) {
                    total += parseFloat(coeff) * parseFloat(grade);
                    totalCoeff += parseFloat(coeff);
                }
            });
            
            return total / totalCoeff;
        } else {
            return this.get('grade');
        }
        // Invalid @each observer: 'studentResult.results.@each.exam.coeff'
    }.property('branch', 'studentResult.results.@each.grade', 'studentResult.results.@each.changeCounter')
});