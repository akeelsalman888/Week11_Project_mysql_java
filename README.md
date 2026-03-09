``` Week11_Project_mysql_java

```

```
mysql-java/                        <- Root folder of your Maven project
│
├── src/                           <- Java source code (required)
│   ├── main/
│   │   └── java/
│   │       └── projects/
│   │           ├── ProjectsApp.java
│   │           ├── ProjectService.java
│   │           ├── ProjectDao.java
│   │           └── (any other Java classes)
│   └── test/                      <- Optional for unit tests
│       └── java/
│           └── (test classes)
│
├── sql/                           <- Folder for your SQL scripts
│   ├── create_tables.sql
│   ├── insert_data.sql
│   ├── update_project.sql
│   └── delete_project.sql
│
├── ERD.png                        <- Screenshot of your Entity Relationship Diagram
├── pom.xml                         <- Maven project descriptor
├── README.md                       <- Optional but recommended for explanation
├── .gitignore                      <- Correct version to ignore target/ and other unnecessary files
├── .classpath                      <- Eclipse project classpath file
├── .project                        <- Eclipse project descriptor file
└── .settings/                      <- Eclipse IDE settings folder (optional)
```
```

Project Name: MySQL-Java Maven Project
Description

This is a Java Maven project that connects to a MySQL database.
It allows you to:

Add new projects

List all projects

Select a project

Update project details

Delete a project

The project uses JDBC for database operations.

Prerequisites

Java JDK 8 or higher

Maven

MySQL database

Database

Database name: projects

Table(s): project

SQL files to create the database and tables are included in the /sql folder.

```

