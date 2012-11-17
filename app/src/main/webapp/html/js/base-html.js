function createLIItem(id, contentAsString, onClickFunction, cssStyle) {
	return dojo.create("li", { id: id, innerHTML: contentAsString, className: cssStyle });
}
function createAndFillUL(ulId, liItemsList, cssStyle, base) {
	var ul = dojo.create("ul", {
		id: ulId,
		className: cssStyle}, base);
	
	for (var i = 0; i < liItemsList.length; i++) {
		ul.appendChild(liItemsList[i]);
	}
}

function buildListItemTextClosure(propertyNames) {
	return function(element, elementId) {
		var text = '';
		
		var seen = false;
		for (var i = 0; i < propertyNames.length; i++) {
			var property = propertyNames[i];
			if (element[property] != null) {
				if (text != '') {
					text += ' ';
				}
				text += element[property];
				seen = true;
			}
		}
		
		if (!seen) {
			text = elementId;
		}
		
		return text;
	};
};

function createHtmlListFromList(list, listId, base, buildTextFunction, onclickClosure) {
	var ul = dojo.create("ul", {
		id: listId,
		className: 'search-list'}, base);
	for (var id in list) {
		var t = list[id];
		var text = buildTextFunction(t);

		dojo.create("li",
				{ innerHTML: text,
			className: 'search-list-item',
			onclick: onclickClosure(id)}, ul);
	}
}

function getKeysAndValues(txtIds) {
	var keys = [];
	var values = [];
	for (var id in txtIds) {
		var node = dojo.byId(txtIds[id]);

		var value = node.value;
		var name = node.attributes.getNamedItem('propertyName').nodeValue;

		if (name != null && value != null) {
			keys.push(name);
			values.push(value);
		}
	}
	
	return {keys: keys, values: values};
};
function createHtmlGroup(parentDOM, title, contentAsJson, saveButton, domId) {
	createHtmlGroupTitleBar(parentDOM, title, domId + "-title");
	createHtmlGroupContent(parentDOM, "", contentAsJson, saveButton, domId);
};
function createHtmlGroupTitleBar(parentDOM, title, domId) {
	var titleBar = dojo.create("div", {className: "person-info-part-title"}, parentDOM);
	titleBar.id = domId;
	dojo.create("a", {innerHTML: title}, titleBar);
	dojo.create("button", {className: "person-info-part-button", innerHTML: "Fermer"}, titleBar);
};
function createHtmlGroupContent(parentDOM, groupId, contentAsJson, saveButton, domId) {
	var div = dojo.create("div", {className: "person-info-part-content"}, parentDOM);
	div.id = domId;
	var table = dojo.create("table", {style: "width: 100%"}, div);
	table.id = domId + "-table";
	
	if (saveButton != null) {
		div.appendChild(saveButton);
	}
	
	createInfoHtmlTable(table, contentAsJson);
};
function createHtmlLabelText(parentTable, propertyName, displayText, value) {
	var tr = dojo.create("tr", {}, parentTable);
	
	if (value == undefined) {
		value = "";
	}

	dojo.create("td", {
		innerHTML: displayText,
		style: "width: 150px"
	}, tr);
	var cell = dojo.create("td", {}, tr);
	var txt = dojo.create("input", {
		style: "width: 100%",
		type: "text",
		value: value,
	}, cell);
	txt.key = propertyName;
};
function createHtmlList(parentTable, propertyName, displayText, key, valuesAsArray) {
	var tr = dojo.create("tr", {}, parentTable);

	dojo.create("td", {
		innerHTML: displayText,
		style: "width: 150px"
	}, tr);
	var cell = dojo.create("td", {}, tr);
	
	var htmlList = dojo.create("ul", {
		style: "width: 100%"
	}, cell);
	htmlList.id = propertyName;
	htmlList.key = key;
	
	for (var i = 0; i < valuesAsArray.length; i++) {
		var txt = dojo.create("li", {
			innerHTML : valuesAsArray[i]
		}, htmlList);
	}
}
function createInfoHtmlTable(parentDOM, info) {
	for (var i in info) {
		var data = info[i];
		var value = data.value;
		
		if (value.isHtmlList != null && value.isHtmlList == true) {
			createHtmlList(parentDOM, i, data.displayText, value.key, value.values);
		}
		else {
			createHtmlLabelText(parentDOM, i, data.displayText, value);
		}
	}
};