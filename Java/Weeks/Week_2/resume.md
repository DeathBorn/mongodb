Some operators that are supported:

- $gt - greater than
- $gte - greater than or equal
- $lt - less than
- $lte - less than or equal
- $regex - search for a pattern inside a field
    - if you want to search for a pattern that begins with something, add on the beggining of the query -> ^
    - if you want to search for a pattern that ends with something, add on the end of the query -> $
- $exists -> retrieve all documents that contains a specific field
- $type -> retrieves all the documents with a specific type. This is done by adding the number referenced by the BSON type. Look for https://docs.mongodb.org/manual/reference/bson-types/ to find the one you want



Example of usages:

They are all used in a similar way:

db.collection.find({ "field" : { $expression : "query"}})
where $expression is one of the above examples.


The $or operator

This is separated from the above, because the syntax of using is different:

This returns the results that are matched

db.people.find( { $or : [ { name : { $regex : "a$"}} , { age : {$exists : true}} ]})

The $and operator

This is equivalent to the find with multiple restriction

db.people.find( { $and : [ { name : { $regex : "a$"}} , { age : {$exists : true}} ]})


Querying inside array

just the same way. The JS can understand that if the field is an array, it needs to search inside the array:

db.collection.find({"arrayField" : "value"})

The $all operator:

Looks for the specific values from an array. It must contain all the values from the array:

db.collection.find({"field" : { $all : ["pretzels", "beer"] } } )

The $in operator:

Returns all the record that matches the options inside. Its used with a or. So, it will return the record that contains the value1 or the value2

db.collection.find({"field" : { $in : ["value1", "value2"] } } )

Queries with dot notation:

If you want to make queries in a document that has a subdocument and you want to look for an information inside the subdocument you can:

db.collection.find( { fieldDocument : { fieldSubDOcument : "value" } })  -> one important thing is that the subdocument fields must match the order and the amount of fields. THis kind of search must be precise

or

db.collection.find( { "fieldDocument.fieldSubDocument" : "value" })


Updating documents

Take two parameters, the first is equivalent to the where clause, and the second takes a document that will replace the information from the document you just found (except the id). Important: the second parameter will be entirely used for the replacement, so if you want to keep some information about the old document, you should add it in this second parameter.

db.collection.update({name : "Smith"}, {name : "Thompson", salary : 50000})o

Using the $set command:

In order to avoid the problem with having to pass the entire information from the document as the second parameter, you should use the $set operator:

db.collection.update({name : "Smith"}, {$set : {salary : 50000}})

Using the $inc operator:

increment a field. if the field does not exist, the field is created with the increment you specified in the $inc operator:

db.collection.update({name : "Smith"}, {$inc : {salary : 1}})

Using the $unset command

if you want to remove a field, you can use the $unset:

db.collection.update({name : "Smith"}, {$unset : {salary : 1}})

Using $push, $pop, $pull, $pullAll, $addToSet

These are used to update information from an array:

Example:

db.arrays.insert({_id : 0, a : [1,2,3,4]})
db.arrays.update( { _id : 0 }, {$set : {"a.2" : 5 }}) // this will make the third element from the array "a" to become the number five.

db.arrays.update( { _id : 0 }, {$push : {a : 6 }}) // add the number 6 to the end of the array
db.arrays.update( { _id : 0 }, {$pop : {a : 1 }}) // remove the last element
db.arrays.update( { _id : 0 }, {$pop : {a : -1 }}) // remove the first element
db.arrays.update( { _id : 0 }, {$pushAll : {a : [7,8,9,10] }}) // add all the elements from the array
db.arrays.update( { _id : 0 }, {$pull : {a : 5 }}) // remove the number 5 of the array. it looks for the value 5 inside the array and remove it.
db.arrays.update( { _id : 0 }, {$pullAll : {a : [7,8,9,10] }}) // remove all the elements from the array
db.arrays.update( { _id : 0 }, {$addToSet : {a : 6 }}) // add the number 5 to the end of the array. if the value already exists inside the array, it wont change anything


Upserts:

db.arrays.update( { _id : 1 }, {$push : {a : 6 }}, { upsert : true }) // this document doesnt exists, so with the upsert value to true, mongodb will create a new document with the information provided.


Multi-Update:

the multi true is required, because the default behaviour for mongo is to update just one document. in order to update multi documents, you need to use the multi : true option.

db.people.update( {}, {$set : { title : "Dr" }}, {multi : true})

Removing data:

db.people.remove({}) -> removes all data
db.people.remove({"name" : "Gabriela"}) -> removes only Gabriela
db.people.remove({name : {$gt : "M"}}) -> removes all the names that begins with "M" or more
db.people.drop() -> drops the entire collection. this is better to be used than .remove({})


Homework 2.3:

hw = db.grades.find({type : "homework"}); null
num = -1;
while(hw.hasNext()) {  next = hw.next(); if(num == next.student_id) {value2 = next.score; if(value1 < value2) { db.grades.remove({student_id : num, score : value1}) }else {db.grades.remove({student_id : num, score : value2})} }else {value1 = next.score; num = next.student_id} }

