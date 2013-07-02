function changeYearsButtonChange(yearActive) {
	dojo.byId('btnStartYear').disabled = yearActive;
	dojo.byId('btnStopYear').disabled = !yearActive;
	dojo.byId('btnRenameYear').disabled = !yearActive;
	dojo.byId('btnYearNewClass').disabled = !yearActive;
	dojo.byId('btnYearRemoveClass').disabled = !yearActive;
}
function startYear(closeId, txtYearNameId) {
	var yearName = dojo.byId(txtYearNameId).value;
	
	sendGetRequest("../year/start", { name: yearName }, function(info) {
		changeYearsButtonChange(true);
		dijit.byId('newClassDialog').currentYearKey = info.key;
		loadYears();
	});
	
	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
}
function stopYear() {
	sendGetRequest("../year/stop", {}, function(info) { changeYearsButtonChange(false); });
}