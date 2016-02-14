To start H2 and init the DB with provided shell scripts you have to have `java` and `groovy`
command on your `PATH` environment variable. Alternatively you can modify variable settings
at the start of `start-h2.sh` script.

Then you just run the script with the name of directory with create/init db scrips as a parameter:
```
$ ./start-h2.sh basic
```

It starts H2 (or throws exception that the port is already bound if it has been running already,
which you may ignore) and then uses Groovy script to initialize DB. Groovy is used because it
offers much shorter insert syntax. Sure it needs support of my custom `DbInit.groovy` class
but you get more with it. It not just inserts, but also can update if the data is there already,
you can easily assign returned primary key values to variables and reuse them as foreign keys, etc.