//$.postJSON = function(url, data, callback) {
//    return $.ajax({
//        'type': 'POST',
//        'url': url,
//        'contentType': 'application/json',
//        'data': $.toJson(data),
//        'dataType': 'json',
//        'success': callback
//    });
//};

Scholagest.SessionManager = Ember.Object.create({
    token: null,
    user: null,
    
//    tryReuseCookie: function() {
//        var token = ;
//        this.setToken(token);
//        if (!Ember.isNone(token)) {
//            var user = Scholagest.__container__.lookup('store:main').find('user', $.cookie('auth_user'));
//            this.setUser(user);
//        }
//    },
    
    isAuthenticated: function() {
        return this.get('token');
    },
    setToken: function(token) {
        this.set('token', token);
        
        $.cookie('access_token', token);
        $.ajaxSetup({
            headers: {
                'Authorization': token
            }
        });
    },
    setUser: function(user) {
        this.set('user', user);
        $.cookie('auth_user', user.get('id'));
    },
    reset: function() {
        $.removeCookie('access_token');
        $.removeCookie('auth_user');
    }
});

Scholagest.LoginRoute = Ember.Route.extend({});
Scholagest.LoginController = Ember.Controller.extend({
    loginFailed: false,
    isProcessing: false,

    reset: function() {
        this.setProperties({
            loginFailed: false,
            isProcessing: false,
            username: '',
            password: ''
        });
    },
    actions: {
        login: function() {
            this.setProperties({
                loginFailed: false,
                isProcessing: true
            });
            
            var self = this;
            Ember.$.post('/scholagest-app/services/login', $.toJSON({
                username: this.get('username'),
                password: this.get('password')
            })).then(function(response) {
                self.reset();

                Scholagest.SessionManager.setToken(response.token);
                
                var user = self.store.find('user', response.user).then(function(user) {
                    Scholagest.SessionManager.setUser(user);
                    self.transitionToRoute('teachers');
                }, function() {});
            }, function() {
                self.set('isProcessing', false);
                self.set('loginFailed', true);
            }.bind(this));
        }
    }
});