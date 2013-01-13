function infoSetterTxtClosure(txtBox, label) {
	return function(newValue) {
		//TODO check for the "update" info.
		
		txtBox.value = newValue;
	};
};

function infoSetterListClosure(htmlList, label) {
	return function(newList) {
		//TODO check for the "update" info.
		
		//Clear the existing list.
		clearDOM(htmlList);
		
		htmlList.values = newList;
		for (var i = 0; i < newList.length; i++) {
			var txt = dojo.create("li", {
				innerHTML : newList[i].name
			}, htmlList);
		}
	};
};

function infoGetterTxtClosure(originalInfo, txtBox) {
	return function() {
		return {
			value: txtBox.value,
			type: originalInfo.type
		};
	};
};

function infoGetterForSaveListClosure(originalInfo, htmlList) {
	return function() {
		var elements = [];
		var values = htmlList.values;
		for (var index in values) {
			elements.push(values[index].info.key);
		}
		return {
			value: elements,
			type: originalInfo.type
		};
	};
};

function infoGetterListClosure(originalInfo, htmlList) {
	return function() {
		var elements = [];
		var values = htmlList.values;
		for (var index in values) {
			elements.push(values[index]);
		}
		return {
			value: elements,
			type: originalInfo.type
		};
	};
};

function prepareTree(rawdata, divName) {
	var store = dojo.store.Observable(new dojo.store.Memory({data: rawdata}));
	store.myKey = divName;
	store.getChildren = function(object) {
		return this.query({parent: object.id});
	};
	dojo.aspect.around(store, "put", function(originalPut) {
		return function(obj, options){
			if(options && options.parent){
				obj.parent = options.parent.id;
			}
			return originalPut.call(store, obj, options);
		};
	});
	
	var treeModel = new dijit.tree.ObjectStoreModel({store: store, query: {id: 'root'}});
    var treeControl = new dijit.Tree({
        model: treeModel,
        showRoot: false
    });
    dojo.byId(divName).appendChild(treeControl.domNode);
    
	return treeControl;
};

function moveElements(sourceTree, destTree) {
	return function(e) {
		var sourceStore = sourceTree.model.store;
		var destStore = destTree.model.store;
		
		var selectedItems = sourceTree.selectedItems;
		for (var itemKey in selectedItems) {
			var movingItem = selectedItems[itemKey];
			sourceStore.remove(movingItem.id);
			destStore.add(movingItem);
		}
	};
};

function createLIItem(id, contentAsString, onClickFunction, cssStyle) {
	var li = dojo.create("li", { id: id, innerHTML: contentAsString, className: cssStyle });
	li.onclick = onClickFunction;
	return li;
};

function createAndFillUL(ulId, liItemsList, cssStyle, base) {
	var ul = dojo.create("ul", {
		id: ulId,
		className: cssStyle}, base);
	
	for (var i = 0; i < liItemsList.length; i++) {
		ul.appendChild(liItemsList[i]);
	}
};

function buildListItemTextClosure(propertyNames) {
	return function(element, elementId) {
		var text = '';
		var properties = element.properties;
		
		var seen = false;
		for (var i = 0; i < propertyNames.length; i++) {
			var property = propertyNames[i];
			if (properties[property] != null) {
				if (text != '') {
					text += ' ';
				}
				text += properties[property].value;
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
		var key = t.key;
		var text = buildTextFunction(t);

		dojo.create("li",
				{ innerHTML: text,
			className: 'search-list-item',
			onclick: onclickClosure(key)}, ul);
	}
};

function createHtmlBaseGroup(parentDOM, title, domId) {
	createHtmlGroupTitleBar(parentDOM, title, domId + "-title");
	createHtmlGroupContent(parentDOM, domId + "-content");
};

function createHtmlGroupTitleBar(parentDOM, title, domId) {
	var titleBar = dojo.create("div", {className: "person-info-part-title"}, parentDOM);
	titleBar.id = domId;
	dojo.create("a", {innerHTML: title}, titleBar);
	dojo.create("button", {className: "person-info-part-button", innerHTML: "Fermer"}, titleBar);
};

function createHtmlGroupContent(parentDOM, domId) {
	var div = dojo.create("div", {className: "person-info-part-content"}, parentDOM);
	div.id = domId;
	var table = dojo.create("table", {style: "width: 100%"}, div);
	table.id = domId + "-table";
};

function createHtmlLabelText(parentTable, propertyName, data) {
	var tr = dojo.create("tr", {id: 'tr' + propertyName}, parentTable);
	
	if (data.value == undefined) {
		data.value = "";
	}

	var label = dojo.create("td", {
		innerHTML: data.displayText,
		style: "width: 150px"
	}, tr);
	var cell = dojo.create("td", {}, tr);
	var txt = dojo.create("input", {
		style: "width: 100%",
		type: "text",
		value: data.value,
	}, cell);
	txt.key = propertyName;
	
	tr.info = {};
	tr.info.propertyName = propertyName;
	tr.info.originalData = data.value;
	tr.info.infoSetter = infoSetterTxtClosure(txt, label);
	tr.info.infoGetter = infoGetterTxtClosure(data, txt);
	tr.info.infoGetterForSave = infoGetterTxtClosure(data, txt);
};

function createHtmlList(parentTable, propertyName, data, createListButtonsClosure, listGetterClosure) {
	var tr = dojo.create("tr", {id: 'tr' + propertyName}, parentTable);

	var label = dojo.create("td", {
		innerHTML: data.displayText,
		style: "width: 150px"
	}, tr);
	var cell = dojo.create("td", {}, tr);
	
	var htmlList = dojo.create("ul", {
		style: "width: 100%"
	}, cell);
	htmlList.id = propertyName;
	
	tr.info = {};
	tr.info.propertyName = propertyName;
	tr.info.originalData = data.value;
	tr.info.infoSetter = infoSetterListClosure(htmlList, label);
	tr.info.infoGetter = infoGetterListClosure(data, htmlList);
	tr.info.infoGetterForSave = infoGetterForSaveListClosure(data, htmlList);
	
	var values = data.value;
	if (listGetterClosure == null) {
		for (var i = 0; i < values.length; i++) {
			var txt = dojo.create("li", {
				innerHTML : values[i]
			}, htmlList);
		}
	}
	else {
		listGetterClosure(values, tr);
	}
	
	if (createListButtonsClosure != null) {
		createListButtonsClosure(propertyName, tr, cell);
	}
};

function createInfoHtmlTable(parentDOM, info, createListButtonsClosure, listGetterClosure) {
	for (var i in info) {
		var data = info[i];
		var value = data.value;
		
		//Does not display a property without name.
		if (data.displayText != null) {
			if (data.isHtmlList != null && data.isHtmlList == true) {
				createHtmlList(parentDOM, i, data, createListButtonsClosure, listGetterClosure);
			}
			else {
				createHtmlLabelText(parentDOM, i, data);
			}
		}
	}
};
//Method used to get the values from the different dialogs. DO NOT REMOVE.
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