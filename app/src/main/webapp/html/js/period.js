function callGetPeriodsInfo(periods, properties, callback) {
	var xhrArgs = {
			url: "../period/getPropertiesForList",
			preventCache: true,
			content: {token: dojo.cookie("scholagest_token"),
				periodKeys: periods,
				properties: properties},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					var info = data.info;
					callback(info);
				}
				else {
					handleServiceError(data);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
		}

		var deferred = dojo.xhrGet(xhrArgs);
};
function getPeriodsInfo(periods, branchKey, classKey, yearKey, divName) {
	callGetPeriodsInfo(periods, ["pPeriodName"], function(periods) {
		clearDOM(divName);
		
		var baseDOM = dojo.byId(divName);
		var tabContainer = new dijit.layout.TabContainer({
			style: "height: 100%; width: 100%;",
			doLayout: false,
			controllerWidget: 'dijit.layout.TabController'
		});
		tabContainer.doLayout = false;
		tabContainer.startup();
		
		var first = true;
		for (var periodKey in periods) {
			var period = periods[periodKey];
			
			var examDiv = dojo.create("div", {});
			createAndAddExamButtons(examDiv, yearKey, classKey, branchKey, periodKey);
			
			var periodContentPane = new dijit.layout.ContentPane({
				style: "height: 100%; width: 100%;",
	            title: period.properties["pPeriodName"].value,
				selected: first,
				content: examDiv
	        });
			first = false;
	        tabContainer.addChild(periodContentPane);
	        
			getStudentsAndExamsAndGradesForClassAndPeriod(examDiv, yearKey, classKey, branchKey, periodKey);
		}
		
		tabContainer.placeAt(divName);
	});
};
function createAndAddExamButtons(parentDom, yearKey, classKey, branchKey, periodKey) {
	var buttonDiv = dojo.create("div", {}, parentDom);
	
	var createExamButton = dojo.create("button", {}, buttonDiv);
	createExamButton.innerHTML = "Creer un examen";
	createExamButton.onclick = function(e) {
		var dialog = dijit.byId("newExamDialog");
		dialog.yearKey = yearKey;
		dialog.classKey = classKey;
		dialog.branchKey = branchKey;
		dialog.periodKey = periodKey;
		
		dialog.show();
	};
};
function getStudentsAndExamsAndGradesForClassAndPeriod(parentDom, yearKey, classKey, branchKey, periodKey) {
	callGetClassInfo(classKey, ["pClassStudents"], function (classInfo) {
		var students = classInfo.properties["pClassStudents"].value;
		getExamsAndGradesForClassAndPeriodAndStudents(parentDom, yearKey, classKey, branchKey, periodKey, students);
	});
};
function getExamsAndGradesForClassAndPeriodAndStudents(parentDom, yearKey, classKey, branchKey, periodKey, students) {
	callGetPeriodsInfo([periodKey], ["pPeriodExams"], function (periodInfo) {
		var exams = periodInfo[periodKey].properties["pPeriodExams"].value;
		getGradesForClassAndPeriodAndStudentsAndExams(parentDom, yearKey, classKey, branchKey, periodKey, students, exams);
	});
};
function getGradesForClassAndPeriodAndStudentsAndExams(parentDom, yearKey, classKey, branchKey, periodKey, students, exams) {
	callGetStudentGrades(students, exams, yearKey, function(grades) {
		getStudentNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, grades);
	});
};
function getStudentNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, grades) {
	getStudentsInfo(students, ["pStudentFirstName", "pStudentLastName"], function(studentsInfo) {
		getExamNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, studentsInfo, exams, grades);
	});
};
function getExamNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, studentsInfo, exams, grades) {
	getExamsInfo(exams, ["pExamName"], function(examsInfo) {
		buildGradesTable(parentDom, yearKey, classKey, branchKey, periodKey, studentsInfo, examsInfo, grades);
	});
};
function buildGradesTable(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, grades) {
	var table = dojo.create("table", {
		style: "border: 1px black solid"
	}, parentDom);
	
	var headerRow = dojo.create("tr", {}, table);
	dojo.create("td", {}, headerRow);
	for (var examIndex in exams) {
		var exam = exams[examIndex];
		var examName = exam.properties["pExamName"].value;
		var cell = dojo.create("td", {innerHTML: examName}, headerRow);
	}
	
	for (var studentIndex in students) {
		var student = students[studentIndex];
		var row = dojo.create("tr", {}, table);
		
		var studentKey = student.key;
		var firstName = student.properties["pStudentFirstName"].value;
		var lastName = student.properties["pStudentLastName"].value;
		
		row.studentKey = studentKey;
		var cell = dojo.create("td", {innerHTML: firstName + " " + lastName}, row);
		
		for (var examIndex in exams) {
			var exam = exams[examIndex];
			var examKey = exam.key;
			var grade = grades[studentKey][examKey];
			
			var cell = dojo.create("td", {}, row);
			cell.examKey = examKey;
			cell.grade = grade;
			
			var gradeValue = '';
			if (grade != undefined && grade.properties["pGradeValue"] != undefined) {
				gradeValue = grade.properties["pGradeValue"].value;
			}
			var gradeTxt = dojo.create("input", {
				type: "text",
				value: gradeValue
			}, cell);
		}
	}
	
	dojo.create("button", {
		onclick: saveGrades(table, yearKey, classKey, branchKey, periodKey, students, exams),
		innerHTML: "Enregistrer"
	}, parentDom);
};
function saveGrades(tableDom, yearKey, classKey, branchKey, periodKey, students, exams) {
	return function(e) {
		var grades = {};
		
		var rows = tableDom.childNodes;
		for (var i = 1; i < rows.length; i++) {
			var row = rows[i];
			var studentGrades = {};
			var studentKey = row.studentKey;
			var cells = row.childNodes;
			for (var j = 1; j < cells.length; j++) {
				var cell = cells[j];
				var text = cell.childNodes[0];

				var examKey = cell.examKey;
				var grade = cell.grade;
				if (grade == null) {
					grade = {
						properties: {"pGradeValue": {}}
					};
				}
				grade.properties["pGradeValue"].value = text.value;
				studentGrades[examKey] = grade;
			}
			
			grades[studentKey] = studentGrades;
		}
		
		sendSaveGradesRequest(yearKey, classKey, branchKey, periodKey, grades);
	};
};
function sendSaveGradesRequest(yearKey, classKey, branchKey, periodKey, grades) {
	var xhrArgs = {
			url: "../student/setGrades",
			preventCache: true,
			postData: dojo.toJson({
				token: dojo.cookie("scholagest_token"),
				yearKey: yearKey,
				classKey: classKey,
				branchKey: branchKey,
				periodKey: periodKey,
				grades: grades
			}),
			handleAs: "json",
			load: function(data) {
				if (data.errorCode != null) {
					handleServiceError(data);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrPost(xhrArgs);
};