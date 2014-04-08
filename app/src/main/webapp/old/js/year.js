function loadYears() {
	sendPostRequest("../year/getYears", { properties: ["pYearName"] }, function(info) {
		var yearActive = info.currentYear != null;
		if (yearActive && dijit.byId('newClassDialog') != undefined) {
			dijit.byId('newClassDialog').currentYearKey = info.currentYear.key;
		}
		if (yearActive && dijit.byId('renameYearDialog') != undefined) {
			dijit.byId('renameYearDialog').currentYearKey = info.currentYear.key;
		}
		
		if (typeof window.changeYearsButtonChange == 'function') {
			changeYearsButtonChange(info.currentYear != null);
		}
		
		dojo.byId('year-search-list-div').yearsList = info.years;
		
		loadClasses(info.years);
	});
}