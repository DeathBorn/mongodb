MongoDB Schema Design

It's important to keep the data consistent with the Application that uses the database.

Facts:

- Supports Rich documents -> stores array, subdocuments and etc
- Pre Join / Embed Data -> the fact that you can have subdocuments allows you to pre join. MongoDB doesnt allow joins.
- No mongo joins -> this is because is hard to scale this
- no constraints ->
- atomic operations -> dont support transactions
- no declared schema

Relational Normalization

Goals of normalization:

- free the database of modification anomalies
- minimize redesign when extending the database
- avoid bias toward any particular access pattern

Living Without Constraints:

The answer to make the data consistent is that the programmer should be responsible for it.

Embbeding helps to make the data consistent. If you try the RDBMS approach(having multiple collections )

Living Without Transactions

We use atomic operations. This means that the user will only see the changes you made in a complete way. There is no way he can see partially.

1 approach:

restructure the documents and make them all in one. Then you get the benefits of atomic operations.

2 approach

implement a locking system in your application

3 approach

tolerate a little bit of inconsistent.

One to One relations

Is when one thing has one of a different thing

 Example:
 Employee has 1 Resume

You can embed one inside the other. To help if you should embed or make two collections in a 1:1 relationship:

- frequency of access
- size of items
- if the document is larger than 16mb
- atomicity of data

One to Many Relationship

Where there is two entities. One example is when there are a lot of examples of one entity that connects to the same entity.

city : person

So in MongoDB when you want to represent a one to many relationship you use true linking

people {
  name : "",
  city : "nyc"
}

city {
  id : "nyc"
}

So this way, you link the city of the people collection using the id of the city collection

One to few

Still one to many, but is easier to model

blog posts : comments

in this example is better to keep the comments inside the post collection.

Many to Many

books : authors
students : teachers

This tends to be the few to few.

books {
  id : 12,
  title : "test"
  authors : [27]
}

authors {
  id : 27,
  author_name "Gabriela",
  books : [12. 7. 8]
}

You can:
1) link it. You can link in only one collection or both. Both is better if in your application you can access each collection independently.
2) embed the documents.this can cause duplication or inconsistent data.

Multikeys:

This is the reason that linking and embeding works so well.

To add an index you do as follow:

db.collection.ensureIndex({"fields" : 1})

And then, you can make the following query:

db.collection.find({"field" :  {$all : [0,1]}})

Benefits of Embedding:
- improved read performance. this is because the way the HD works. so if you can make all the document in one part of the disk, it will be faster to read.
- one round trip to the db

Trees:

How to represent tree inside the database.
You can represent it by adding an array that contains all the subsequent information for a specific document.

{ id : 34,
  name : "test",
  parent_id : 12,
  ancestors : [12, 35, 90]
}


What is an ODM?

Object Document Mapper.

