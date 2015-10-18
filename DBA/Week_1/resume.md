
Concepts:

Why Mongo started


* Scale -> hardware evolve to be more parallel(SCALE)
* scale up -> because of big data

* DEVELOPMENT SPEED -> make app development easier and elegant.
  * data structured, unstructured and polymorphic(complex data)

Scaling:

First approach:
  * If you need something faster, the traditional way is Vertical Scaling(buy more computers and etc)

Second approach:
  * You can use horizantal scale, add more servers. The problem is that it will be separated servers and the probability of one server going down is bigger than the big computer used for the first approach. There are ways to fix that.

JSON - JavaScript Object Notation

Example:

{ "name" : "Gabriela",
  "age" : 24
}

You can have six data types using JSON:
* numbers
* strings
* boolean
* null
* lists
* objects/documents(other JSON)

BSON - Binary JSON

Encode JSON in a binary way. This is needed for efficiency. It is used internally on the database.
More info on bsonspec.org

Two goals of using:
* fast scannability - > sometimes you can have a large document(with subdocument.) 

* data types - we want to save the following types -> date datatype, binary data and ObjectID.

* The field with underscore at the beggining is the primary key of the document.

For the client app we need a driver that can interpret the BSON. For each language there is a driver and the way that works is that this driver converts BSON directly to a native data structure of that language you're using, so for example if you're using Java, it will convert to a Java Object. It won't translate to JSON and then translate to the Java Object.


Dynamic Schema:

The schemaless and the dynamic schema of the Mongo gives you flexibility.
	iteration is easier and more agile
	data representation polymorphic


* One important observation is that, there are some properties that you may need to set on every document when using some features: sharding it needs a specific key that needs to be set on every document in order to work. *

Mongo Shell

If you want to start a mongod using a clean data folder, just do

$ mongod --dpath pathToFolder

Cool commands:

use dbName - move to a db, if that db doesnt exists mongo creates
help - shows a list of commands you can do


Mongo Import

The way to do that is:

$ mongoimport --stopOnError --db pcat --collection products < products.json 

--stopOnError - good flag for a first time importing. It will stop if you try to add something with a wrong format
--db sets the name of the db. If none is passed it will use the default(test)
--collection sets the name of the collection inside the db. If none is passed, it will receive the filename.

You can import three types of file: JSON, TSV and CSV

Mongo Shell - Cursors Introcution:

$ mongo localhost/pcat -> the to connect on the db that is on localhost and access the the pcat db

When you do a find on a collection, it will give you 10 documents at a time. If you want to access more, you can use:

$ it -> and it will give you 20 more docments.

You can use some options on the find() command. If you append for example a .limit(5) it will return the five last documents. If you add a .toArray() you get a better way to see the document and is equivalent to the .pretty()

Query Language: Basic Concepts:

are represented in BSON(JSON) - so it's a data driven declaration style.

Commands:

show dbs -> shows the databases on the mongo
show collections -> shows the collections inside the db you are currently on
db.collection.count() -> amount of documents
db.collection.find() -> returns all the documents separated by 20
db.collections.findOne() -> returns a single JSON document
db.collections.findOne( {} ) -> a query is represented by a JSON 
db.collections.find().limit(2) -> returns only two results. This is executing server side and that's really good, because is it was client side, it would be bad, because it could return a ton of data and then limiting. The query only runs when all the parameter is applied.
db.collections.find().limit(4).skip(2) -> this skips the first two documents and then returns the other 4.
db.collections.find({}, {name:1, _ id:0}) - returns all the documents but only presents the name field and omits the rest. The first parameter is a LIKE kind of thing. The second tells mongo what you want to be shown . 0 turns it off and 1 turns it on

db.collections.find({price: {$gte:200}}, {name: 1} ) -> shows all the products that have a price of greater than or equal to 200

# search an array
db.collections.find({"for" : "ac3"}) - even if the for has a lsit as value, it will interpret as a search inside the list.

# search a nested documents
db.collections.find({"x.a" : 1})


* Operators

Querying:
$gte
$gt
$lt
$lte
$in
$type
$or - when you dont add this, it will automatically gives an and search
$not
$nin - not in
$exists:true - check if a field is present in adocument

Updating:
$inc
$set
$addToSet

* Sorting

db.collections.find().sort(field : 1) -> you can use 1 for ascending and -1 descending. You can use it with multiple keys as well.

* Cursors methods
toArray() - iterates through docs and returns an array of the result
forEach( func )
map( func )
hasNext()
next()
objsLeftInBatch() - returns count of docs left in current batch
count(applySkipLimit) - returns amount of docs inside
itcount() - iterates through documents and counts them


If you want to write it as "code" inside the mongo, you can by adding {} in between the commands. And it will give a nice format like this:

{
    var cursor = db.collection.find().limit(100);
    while(cursor.hasNext()) {
       print(cursor.next().field;
    }
}
}
