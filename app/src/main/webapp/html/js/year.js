function changeYearsButtonChange(yearActive) {
	dojo.byId('btnStartYear').disabled = yearActive;
	dojo.byId('btnStopYear').disabled = !yearActive;
	dojo.byId('btnRenameYear').disabled = !yearActive;
	dojo.byId('btnYearNewClass').disabled = !yearActive;
	dojo.byId('btnYearRemoveClass').disabled = !yearActive;
}
function loadYears() {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/year/getYears",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token"),
				properties: ["pYearName"] },
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						var info = data.info;
						
						var yearActive = info.currentYear != null;
						if (yearActive) {
							dijit.byId('newClassDialog').currentYearKey = info.currentYear.key;
						}
						changeYearsButtonChange(info.currentYear != null);
						
						dojo.byId('year-search-list-div').yearsList = info.years;
						
						loadClasses(info.years);
					}
					else
						alert("Error during getCurrent: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}
function startYear(closeId, txtYearNameId) {
	var yearName = dojo.byId(txtYearNameId).value;
	
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/year/start",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token"),
				name: yearName},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						//loadYears();
						changeYearsButtonChange(true);
						dijit.byId('newClassDialog').currentYearKey = data.info.key;
						loadYears();
					}
					else
						alert("Error during startYear: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
	
	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
}
function stopYear() {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/year/stop",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token")},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						//loadYears();
						changeYearsButtonChange(false);
					}
					else
						alert("Error during stopYear: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}