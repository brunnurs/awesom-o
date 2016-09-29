'use strict';

describe('home', function () {

    var workFrom = element(by.id('field_workFrom'));
    var workTo = element(by.id('field_workTo'));
    var projects = element(by.id('field_project'));
    var saveWorkLog = element(by.css('button[type=submit]'));

    var homeMenu = element(by.id('home-menu'));

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));


    beforeAll(function () {
        browser.get('/');

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should create work log with correct inputs', function () {
        homeMenu.click();
        expect(element.all(by.css('h1')).first().getText()).toMatch(/The awesome time tracking tool!/);

        workFrom.sendKeys('2016-09-22 03:00');
        workTo.sendKeys('2016-09-22 05:00');

        element(by.model('vm.newWorkLog.project')).$('[value="1"]').click();

        saveWorkLog.click();

        expect(element(by.css('.alert-success')).getText()).toMatch(/Your time has been logged successfully!/);
    });


    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
