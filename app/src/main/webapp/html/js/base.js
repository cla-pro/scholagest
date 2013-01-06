function escapeJson(json) {
	return handleJson(json, escape);
};

function unescapeJson(json) {
	return handleJson(json, unescape);
};

function handleJson(json, handleStringFunction) {
	var result = {};
	for (var key in json) {
		var value = json[key];
		var typeOfValue = typeof(value);
		
		var escaped = value;
		if (typeOfValue == "string") {
			escaped = handleStringFunction.call(this, value.replace(/\+/g, " "));
		}
		else if (typeOfValue == "object" && typeOfValue.length != null) {
			escaped = handleArray(value, handleStringFunction);
		}
		
		result[key] = escaped;
	}
	
	return result;
};

function handleArray(array, handleStringFunction) {
	var result = [];
	for (var i = 0; i < array.length; i++) {
		result.push(handleStringFunction.call(this, array[i].replace(/\+/g, " ")));
	}
	
	return result;
};

function clearDOM(domId) {
	var base = dojo.byId(domId);
	while (base.hasChildNodes()) {
		base.removeChild(base.lastChild);
	}
};

function getKeyValues(parentId) {
	var keyValues = {};
	
	var rows = dojo.byId(parentId).childNodes;
	for (var i = 0; i < rows.length; i++) {
		var row = rows.item(i);
		var info = row.info;
		if (info != null) {
			keyValues[info.propertyName] = info.infoGetterForSave();
		}
	}
	
	return keyValues;
};

function extractText(properties, propertyNames, defaultText) {
	if (properties == null || propertyNames == null) {
		return defaultText;
	}
	
	var info = [];
	for (var i = 0; i < propertyNames.length; i++) {
		var p = properties[propertyNames[i]];
		if (p != null) {
			info.push(p.value);
		}
	}
	return info.join(' ');
};

function convertList(list, propertyNames) {
	var resultList = [];
	
	for (var i = 0; i < list.length; i++) {
		var element = list[i];
		var displayText = extractText(element.properties, propertyNames, element);
		
		resultList.push({
			name: displayText,
			id: element.key,
			key: element.key,
			info: element,
			parent: 'root'
		});
	}
	
	return resultList;
};

function extractKeys(list) {
	var resultList = [];
	
	for (var i = 0; i < list.length; i++) {
		var element = list[i];
		resultList.push(element.key);
	}
	
	return resultList;
};