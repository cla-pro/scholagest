function callGetPeriodsInfo(periods, properties, callback) {
	sendGetRequest("../period/getPropertiesForList", { periodKeys: periods, properties: properties }, callback)
};
function getPeriodMeans(periodKey, students, callback) {
	sendGetRequest("../period/getMeans", { studentKeys: students, periodKey: periodKey }, callback);
};
function getPeriodsInfo(periods, branchKey, classKey, yearKey, divName, isBranchNumerical) {
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
	        
			getStudentsAndExamsAndGradesForClassAndPeriod(examDiv, yearKey, classKey, branchKey, periodKey, isBranchNumerical);
		}
		
        var yearMeanDiv = dojo.create("div", {});
		var yearContentPane = new dijit.layout.ContentPane({
            style: "height: 100%; width: 100%;",
            title: "Moyenne annuelle",
            selected: false,
            content: yearMeanDiv
		});
		tabContainer.addChild(yearContentPane);
		
		getBranchMean(yearMeanDiv, yearKey, classKey, branchKey, isBranchNumerical);
		
		tabContainer.placeAt(divName);
	});
};
function getBranchMean(yearMeanDiv, yearKey, classKey, branchKey, isBranchNumerical) {
    callGetClassInfo(classKey, ["pClassStudents"], function (classInfo) {
        var students = classInfo.properties["pClassStudents"].value;
        
        getStudentsInfo(students, ["pStudentFirstName", "pStudentLastName"], function(studentsInfo) {
            sendGetRequest("../branch/getMeans", { studentKeys: students, branchKey: branchKey, yearKey: yearKey }, function(means) {
                var table = dojo.create("table", {
                    style: "border: 1px black solid"
                }, yearMeanDiv);
                
                var headerRow = dojo.create("tr", {}, table);
                dojo.create("td", {}, headerRow);
                dojo.create("td", {innerHTML: "Moyenne"}, headerRow);

                var meanKey = means.key;
                var meanGrades = means.grades;
                
                for (var studentIndex in students) {
                    var student = studentsInfo[studentIndex];
                    var row = dojo.create("tr", {}, table);
                    
                    var studentKey = student.key;
                    var firstName = student.properties["pStudentFirstName"].value;
                    var lastName = student.properties["pStudentLastName"].value;
                    
                    row.studentKey = studentKey;
                    var cell = dojo.create("td", {innerHTML: firstName + " " + lastName}, row);

                    createGradeCell(meanKey, meanGrades[studentKey], row, !isBranchNumerical);
                }
                
                if (!isBranchNumerical) {
                    dojo.create("button", {
                        onclick: saveBranchMeans(table, yearKey, classKey, branchKey, isBranchNumerical),
                        innerHTML: "Enregistrer"
                    }, yearMeanDiv);
                }
            });
        });
    });
};
function saveBranchMeans(tableDom, yearKey, classKey, branchKey, isBranchNumerical) {
    return function(e) {
        var grades = {};
        var branchMeans = {};
        
        var rows = tableDom.childNodes;
        var hasErrors = false;
        for (var i = 1; i < rows.length; i++) {
            var row = rows[i];
            var studentGrades = {};
            var studentKey = row.studentKey;
            var cells = row.childNodes;
            
            var cell = cells[cells.length - 1];
            
            if (!isBranchNumerical) {
                var gradeResult = storeGradeValue(cell, isBranchNumerical);
                if (gradeResult.hasError) {
                    hasErrors = true;
                } else {
                    grades[studentKey] = {};
                    branchMeans[studentKey] = gradeResult.grade;
                }
            }
        }
        
        if (hasErrors) {
            alert('Erreurs dans les notes.');
        } else {
            sendSaveGradesRequest(yearKey, classKey, branchKey, null, grades, {}, branchMeans);
        }
    };
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
function getStudentsAndExamsAndGradesForClassAndPeriod(parentDom, yearKey, classKey, branchKey, periodKey, isBranchNumerical) {
	callGetClassInfo(classKey, ["pClassStudents"], function (classInfo) {
		var students = classInfo.properties["pClassStudents"].value;
		getExamsAndGradesForClassAndPeriodAndStudents(parentDom, yearKey, classKey, branchKey, periodKey, students, isBranchNumerical);
	});
};
function getExamsAndGradesForClassAndPeriodAndStudents(parentDom, yearKey, classKey, branchKey, periodKey, students, isBranchNumerical) {
	callGetPeriodsInfo([periodKey], ["pPeriodExams"], function (periodInfo) {
		var exams = periodInfo[periodKey].properties["pPeriodExams"].value;
		getGradesForClassAndPeriodAndStudentsAndExams(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, isBranchNumerical);
	});
};
function getGradesForClassAndPeriodAndStudentsAndExams(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, isBranchNumerical) {
	callGetStudentGrades(students, exams, yearKey, function(grades) {
		getStudentNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, grades, isBranchNumerical);
	});
};
function getStudentNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, grades, isBranchNumerical) {
	getStudentsInfo(students, ["pStudentFirstName", "pStudentLastName"], function(studentsInfo) {
		getExamNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, students, studentsInfo, exams, grades, isBranchNumerical);
	});
};
function getExamNamesForGrades(parentDom, yearKey, classKey, branchKey, periodKey, studentList, studentsInfo, exams, grades, isBranchNumerical) {
	getExamsInfo(exams, ["pExamName"], function(examsInfo) {
		getPeriodMeanForDisplay(parentDom, yearKey, classKey, branchKey, periodKey, studentList, studentsInfo, examsInfo, grades, isBranchNumerical);
	});
};
function getPeriodMeanForDisplay(parentDom, yearKey, classKey, branchKey, periodKey, studentList, studentsInfo, exams, grades, isBranchNumerical) {
	getPeriodMeans(periodKey, studentList, function(means) {
		buildGradesTable(parentDom, yearKey, classKey, branchKey, periodKey, studentsInfo, exams, grades, means, isBranchNumerical);
	});
};
function buildGradesTable(parentDom, yearKey, classKey, branchKey, periodKey, students, exams, grades, means, isBranchNumerical) {
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
	dojo.create("td", {innerHTML: "Moyenne"}, headerRow);
	
	var meanKey = means.key;
	var meanGrades = means.grades;
	
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
			
			createGradeCell(examKey, grade, row, true);
		}

		createGradeCell(meanKey, meanGrades[studentKey], row, !isBranchNumerical);
	}
	
	dojo.create("button", {
		onclick: saveGrades(table, yearKey, classKey, branchKey, periodKey, students, exams, isBranchNumerical),
		innerHTML: "Enregistrer"
	}, parentDom);
};
function createGradeCell(examKey, grade, row, isEditable) {
	var cell = dojo.create("td", {}, row);
	cell.examKey = examKey;
	cell.grade = grade;
	
	var gradeValue = '';
	if (grade != undefined && grade.properties["pGradeValue"] != undefined) {
		gradeValue = grade.properties["pGradeValue"].value;
	}
	
	if (isEditable) {
		dojo.create("input", {
			type: "text",
			value: gradeValue
		}, cell);
	} else {
		dojo.create("label", {
			innerHTML: gradeValue
		}, cell);
	}
}
function saveGrades(tableDom, yearKey, classKey, branchKey, periodKey, students, exams, isBranchNumerical) {
	return function(e) {
		var grades = {};
		var periodMeans = {};
		
		var rows = tableDom.childNodes;
		var hasErrors = false;
		for (var i = 1; i < rows.length; i++) {
			var row = rows[i];
			var studentGrades = {};
			var studentKey = row.studentKey;
			var cells = row.childNodes;
			
			for (var j = 1; j < cells.length; j++) {
				var cell = cells[j];
				
				if (j < cells.length - 1) {
					var gradeResult = storeGradeValue(cell, isBranchNumerical);
					if (gradeResult.hasError) {
						hasErrors = true;
					} else {
						studentGrades[gradeResult.examKey] = gradeResult.grade;
					}
				} else if (!isBranchNumerical) {
					var gradeResult = storeGradeValue(cell, isBranchNumerical);
					if (gradeResult.hasError) {
						hasErrors = true;
					} else {
					    periodMeans[studentKey] = gradeResult.grade;
					}
				}
			}
			
			grades[studentKey] = studentGrades;
		}
		
		if (hasErrors) {
			alert('Erreurs dans les notes.');
		} else {
			sendSaveGradesRequest(yearKey, classKey, branchKey, periodKey, grades, periodMeans, {});
		}
	};
};
function storeGradeValue(cell, isBranchNumerical) {
	var text = cell.childNodes[0];

	var examKey = cell.examKey;
	var grade = cell.grade;
	if (grade == null) {
		grade = {
			properties: {"pGradeValue": {}}
		};
	}
	
	var gradeValue = text.value;
	if (checkGradeAndSetTextColor(gradeValue, text, isBranchNumerical)) {
		return {hasError: true};
	}
	
	grade.properties["pGradeValue"].value = gradeValue;
	//grades[examKey] = grade;
	
	return { hasError: false, grade: grade, examKey: examKey };
};
function checkGradeAndSetTextColor(gradeValue, textField, isBranchNumerical) {
	if (isBranchNumerical && gradeValue != null && gradeValue != '') {
		if (isNaN(gradeValue)) {
			textField.style.color = 'red';
			return true;
		} else {
			textField.style.color = 'black';
		}
	} else {
		textField.style.color = 'black';
	}
	
	return false;
}
function sendSaveGradesRequest(yearKey, classKey, branchKey, periodKey, grades, periodMeans, branchMeans) {
	var postContent = {
			yearKey: yearKey,
			classKey: classKey,
			branchKey: branchKey,
			periodKey: periodKey,
			grades: grades,
			periodMeans: periodMeans,
			branchMeans: branchMeans
		};
	sendPostRequest("../student/setGrades", postContent, function(info) {});
};