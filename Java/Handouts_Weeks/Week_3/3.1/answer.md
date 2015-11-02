students = db.students.find(); null
while(students.hasNext()) {
	var value1, value2;
	var times = -1;
	var student = students.next();
	var score = student.scores;
	score.forEach(function(entry) {
		if(entry.type == "homework") {
			if(times == -1) {
				value1 = entry.score;
				times = 1;
			} else {
				value2 = entry.score;
				if(value1 < value2){
					db.students.update({_id : student._id},{ $pull : { scores : { score : value1 }}})
				} else {
					db.students.update({_id : student._id},{ $pull : { scores : { score : value2 }}})
				}
				times = -1;
			}
		}
	});
}