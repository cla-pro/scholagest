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
function createHtmlGroupTitleBar(parentDOM, title) {
	var titleBar = dojo.create("div", {className: "person-info-part-title"}, parentDOM);
	dojo.create("a", {innerHTML: title}, titleBar);
	dojo.create("button", {className: "person-info-part-button", innerHTML: "Fermer"}, titleBar);
};
function createHtmlGroupContent(parentDOM, groupId, contentAsJson) {
	var div = dojo.create("div", {className: "person-info-part-content"}, parentDOM);
	var table = dojo.create("table", {style: "width: 100%"}, div);
	
	createInfoHtml(table, contentAsJson);
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
function createHtmlGroup(parentDOM, title, contentAsJson) {
	createHtmlGroupTitleBar(parentDOM, title);
	createHtmlGroupContent(parentDOM, "", contentAsJson);
};
function createInfoHtml(parentDOM, info) {
	for (var i in info) {
		var data = info[i];
		
		if (data.isHtmlGroup != null && data.isHtmlGroup == true) {
			createHtmlGroup(parentDOM, data.displayText, data.value);
		}
		else {
			createHtmlLabelText(parentDOM, i, data.displayText, data.value);
		}
	}
};
