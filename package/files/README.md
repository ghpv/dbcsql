# Design-by-contract SQL generator

This utility can be used to generate SQL implementations of simple operations using a "design-by-contract" approach.
In other words, one can define a **contract** of the operation, and this program will output a conforming SQL implementation.

## Operation

In order for the utility to generate it needs 2 inputs: a [Context](#context) and a [Contract](#contract).
More information is available in the respective sections.

Combining these 2 definitions the utility then translates a contract into a valid SQL function for the database desribed in context.
Only a single contract should be supplied at a time.

Currently, only recent postgres syntax is supported - each operation outputted assumes it will be used in postgres database, in some version that supports having plain SQL operations within the functions.
Additionally, please note that only `UTF-8` encoding is supported, if you are getting weird characters please check whether you are using `UTF-8`.

## Context

**Context** is used to desribe various implementation details of the target database.
This is a limited description that concentrates only on the needed information.
Supported items are available in subsections.

### Table

The most fundamental item in this file - a simple table descriptor.
This should include a table name, along with all its columns.
Note that you do not need to supply any restrictions in here, ones that are necessary are defined separately (have a standalone keyword).
Here is the syntax for a table descriptor:

```txt
table DSL_TABLE_NAME = DB_TABLE_NAME
{
	DSL_COLUMN_NAME1 = DB_TABLE_NAME1 DATA_TYPE;
	DSL_COLUMN_NAME2 = DB_TABLE_NAME2 DATA_TYPE;
	...
};
```

Every single entry (i.e. the table name and column names) can have separate names, this is denoted with a `DSL_NAME = DB_NAME` equation.
`DSL_NAME` is used within the contract, whereas `DB_NAME` is used within the database.
In case these names are equal it is possible to supply one name (i.e. skip the `= DB_NAME` part).

`DATA_TYPE` is used to describe what kind of data type should be given to a _parameter_ of the operation, if it applies to this column (e.g. if you have a parameter that is used to update this column, its type should be equal to that column).
Note that this is strictly about _parameter_ and _not_ the columns in the table.
So, for example, if one has a `CHARACTER VARYING (50)` column the correct datatype for it is `VARCHAR`, not `CHARACTER VARYING`.
Similarly, for `NUMERIC` and other sized data one should not supply the sizing. (So use `NUMERIC` and _not_ `NUMERIC(5, 3)`)
The `DATA_TYPE` should be a single non-spaced "word", it is not processed in any way, but is instead directly passed as a parameter type where applicable.

This descriptor should include every single column that the contracts should be aware of, this includes also _referenced fields_, such as foreign keys.

Note that it's possible for tables to extend a single other table:

```txt
table NAME extends DB_NAME
{
	...
};
```

This is understood that table `NAME` (name can be given as described above) should take contents of table `DB_NAME` (that is, it's columns), and copy them to itself.
Additionally, if a table has an [identifier](#identifier), it is copied as well.
All the other columns (i.e. the ones belonging to this table and not in the parent) can be defined as normally.

### Identifier

This keyword is used to denote an identifier for the table, i.e. it's primary key.
It must be supplied _after_ the table declaration.
Note that _composite_ primary keys are not allowed (even though they are allowed in the database).
Reason for that is that the only use case for this keyword is to support `return _identifier` keyword, and that does not support for returning more than one value implemented.
Syntax is as follows:

```txt
identifier for DB_NAME is DB_COLUMN_NAME;
```

Where `DB_NAME` is the _database_ name of the table, and `DB_COLUMN_NAME` is the _database_ name of one of the table's columns.

### Connection

This keyword is used to denote a foreign key relationship between the tables.
It should be supplied _after_ both tables, and the columns upon which the connection is made, are defined.
Currently, there is no reason for the translator to know which table is the parent, and which is the referencer, so they can be supplied in any order.
Here is the syntax for a connection descriptor:

```txt
connection between DB_TABLE_NAME1 and DB_TABLE_NAME2 called CONNECTION_NAME
{
	DB_COLUMN_NAME_IN_TABLE1 = DB_COLUMN_NAME_IN_TABLE2;
	...
};
```

Here, `DB_TABLE_NAME` (both `1` and `2`) are the _database_ names of the relevant tables (if you are supplying the DSL names you _must_ use the _database_ name).
Similarly, `DB_COLUMN_NAME_IN_TABLE` (both `1` and `2`) are also the _database_ names of the columns, not _dsl_.
As the syntax implies - it is assumed that the columns on the "left" belong to the table on the "left" and the columns on the "right" belong to the table on the "right".

The `called CONNECTION_NAME` part is optional, and can be skipped if it is not necessary to define the connection name.
In case it is supplied it can be used to define precisely which key is to be used (e.g. in a case where you have multiple connections between same 2 tables).
`CONNECTION_NAME` does not have any requirement other than being uniquely identifiable (within connections between the relevant tables) - it can be the name of the foreign key relationship (but it does not have to), or the name of the connection in the design document.

## Contract

**Contract** is used to describe the operation in its "design-by-contract" form.
Here, the "design-by-contract" form is precisely defined as parameters, preconditions and postconditions of the operation.
It is also required that if the operation is called and preconditions hold, the operation should perform some actions in order to satisfy the postconditions.

These contracts can be defined during the analysis of some software system on the analysis-level models.
The [context](#context) file is also required in order to supply all other details that go outside the scope of the analysis-level documents.
Below is the high-level syntax of a contract:

```txt
operation OPERATION_NAME
{
	parameter_name1;
	...
}
preconditions
{
	PRECONDITION;
	...
}
postconditions
{
	POSTCONDITION return RETURN_VALUE;
	...
}
comment 'SQL comment with escaped '' quote'
```

The `OPERATION_NAME` is a name of the operation.
It is recommended to supply it as a single "variable-like" name - so no spaces or UTF-8 (estonian chars are permitted) one can use `_` to separate the words (e.g. `my_operation`, `change_product_status`, `register_person`).
It is, however, possible to use spaces if the name is quoted (i.e. surrounded with `"`).

Parameters have similar restriction as the operation name.
Note that you _may not_ supply the type of the parameter, it must be resolveable from the contract (e.g. if it is compared with/changes value of, some column).

Both parameters and operation name are passed through a "name sanitizer", which, amongst other things, changes estonian chars to english, changes spaces to underscores, and changes camel case notation to snake case.

Pre- and postconditions are described in their own sections below, here it is simply denoted where they should go, and that it's possible to have multiple definitions.
In general, contracts operate on the notion of "variables" (or instances), where, instead of referring to the table (entity) the contract refers to a _named_ table/entity that has specific restrictions.
More info about these variables and restrictions is available later in [exists preconditions](#exists-precondition).
It is also assumed that each postconditions directly translates into a _single_ query.

All postconditions _may_ have a return clause (not compulsory).
It is possible to have multiple postconditions with a return clause, in this case only the last one is actually returned.
`RETURN_VALUE` may be:

- Literal: some literal like a number or a string.
- Parameter: some parameter of the operation.
- Column: column belonging to the _target_ of the postcondition.
- `_identifier`: special keyword that returns the [identifier](#identifier) of the postcondition's _target_.

### Preconditions

**Preconditions** describe the state of the system that must hold for the operation to take place.
The way this is enforced is that preconditions restrict the queries so that they can only succeed when the preconditions are satisfied.
All supported preconditions are described in subsections.

#### Exists precondition

This precondition, as the name implies, states that some type of data must exist.
Below is the syntax:

```txt
exists DSL_TABLE_NAME ALIAS (RESTRICTIONS);
```

`DSL_TABLE_NAME` is a _dsl_ name of a [table](#table).

`ALIAS` is some name to refer to this specific instance, if it helps, you can think of it as a variable name.

`RESTRICTIONS` is optional restrictions that must be placed upon this table, these have the following syntax:

```txt
DSL_COLUMN_NAME COMPARISON TARGET
```

`DSL_COLUMN_NAME` refers to the [column](#table) of the table upon which the restriction is placed.

`COMPARISON` is some comparison symbol between the two entries, this includes: `=`, `<>`, `!=`, `>`, `>=`, `<`, `<=`, `in`, `not in`, `is` and `is not`.

`TARGET` is some literal (or parameter name) to compare against, in case there are multiple (e.g. for `in` and `not in`, these can be supplied within brackets like `(5, 'str', parameter)`)

These `RESTRICTIONS` can be combined in a straighforward way using `AND`, `OR`, `!`, and `()`:

```txt
(RESTRICTION1 AND RESTRICTION2) OR (RESTRICTION3 AND !RESTRICTION4);
```

Last part, `!RESTRICTION4` denotes a _not_ operation, i.e. the _opposite_ of `RESTRICTION4` must hold.

#### Connection precondition

This precondition is used to denote that some variables must be connected.
This exists because in an analysis-level document tables (or more specifically, entities) would not have columns (attributes) that are used in the connection, but instead have a _relationship_.
As such, this precondition is used to facilitate such description by allowing one to say that instances must be connected, instead of operating on attributes that the instance does not have.
Below is the syntax:

```txt
connection between ALIAS1 and ALIAS2 through NAME1, NAME2, ...;
```

The `ALIAS` (`1` and `2`) refer to the instance aliases defined in [exists precondition](#exists-precondition).
Note that you do not necessarily have to connect 2 instances that are connected directly (i.e. have a relationship between the entities), the translator can resolve the connection even if the entities have other entities between them (i.e. the connection is not exactly between these 2 instances, but they are connected through other entity(ies).)

`through NAME1, NAME2, ...` part is optional, it can be used when it's necessary to distinguish which connections must be used (e.g. when you have multiple connections between 2 entities/tables).
These names must be given in the order of the connection (i.e. from `ALIAS1` to `ALIAS2`).
The resolution for these works in a queue-like manner - translator tries to use the first name given, if the name can be applied that name is used and the translator moves to the next one. If it can't apply the name then translator picks the first connection it can find between the tables, and tries again on the next connection.

### Postconditions

**Postcondition** is used to describe the state of the system after the operation takes place, provided the preconditions hold.
All supported postconditions are described in subsections.

### Deleted postcondition

The most simple of postconditions - deletion.
This simply denotes that the instance in question must be deleted.
Below is the syntax:

```txt
deleted ALIAS;
```

Where `ALIAS` is some alias defined earlier, e.g. in [exists precondition](#exists-precondition).

Below are some oddities that one must keep in mind:

- After the `deleted` postcondition the alias is added to the internal ignore list, and restrictions placed upon it are no longer considered.
- Foreign keys must be kept in mind when deleting - it is not possible to delete the parent table (if it does not have `ON DELETE` option in the database) while it has some data that references it. (But it is possible to delete first all the referencing data, and then all the parent data).

### Updated postcondition

This postcondition updates the data of some instace.
Below is the syntax:

```txt
updated ALIAS
{
	DSL_COLUMN_NAME = TARGET;
	...
	link with ALIAS;
	...
	unlink from ALIAS;
};
```

`DSL_COLUMN_NAME = TARGET` denotes a desired update, `DSL_COLUMN_NAME` refers to the [column](#table) of the table that the `ALIAS` belongs to, and a `TARGET` is some literal or parameter name.
`link with ALIAS` is used to link the two instances (as again, in analysis-level we do not have precise columns, but instead only relationships).
`unlink from ALIAS` is used to unlink (i.e. set to `null`) the relevant column in the target table, note that this does _not_ imply `connection between` precondition, and it must be supplied separately if the _instances_ are actually supposed to be linked.

### Inserted postcondition

This postcondition denotes that some data must be inserted.
Unlike the previous postconditions, this one operates on a table and defines its own variable (as we can't have it in preconditions, but all resolution logic works on variables).
Below is the syntax:

```txt
inserted DSL_TABLE_NAME ALIAS
{
	DSL_COLUMN_NAME = TARGET;
	...
	link with ALIAS;
	...
}
```

The inner part of the postcondition works the same way as in [updated](#updated-postcondition).
