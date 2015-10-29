# The Angular-Spark generator 

A [Yeoman](http://yeoman.io) generator for [AngularJS](http://angularjs.org) and [Spark](http://sparkjava.com/).

Spark is a Java-based micro-framework.  For AngularJS integration with other micro-frameworks, see https://github.com/rayokota/MicroFrameworkRosettaStone.

## Installation

Install [Git](http://git-scm.com), [node.js](http://nodejs.org), [JDK 8](https://www.java.com), and [Maven 3](http://maven.apache.org/).

Install Yeoman:

``` bash
    $> npm install -g yo
```

Install the Angular-Spark generator:

``` bash
    $> npm install -g generator-angular-spark
```

The above prerequisites can be installed to a VM using the [Angular-Spark provisioner](https://github.com/rayokota/provision-angular-spark).

## Creating a Spark service

In a new directory, generate the service:

``` bash
    $> yo angular-spark:app
```

Compile the service:

``` bash
    $> mvn compile
```

Run the service:

``` bash
    $> mvn exec:exec
```

Your service will run at [http://localhost:8080](http://localhost:8080).

## Creating a persistent entity

Generate the entity:

``` bash
    $> yo angular-spark:entity [myentity]
```

You will be asked to specify attributes for the entity, where each attribute has the following:

- a name
- a type (String, Integer, Long, Float, Double, Boolean, Date, Enum)
- for a String attribute, an optional minimum and maximum length
- for a numeric attribute, an optional minimum and maximum value
- for a Date attribute, an optional constraint to either past values or future values
- for an Enum attribute, a list of enumerated values
- whether the attribute is required

## Creating a REST web service

Generate the service:

``` bash
    $> yo angular-spark:service [myservice]
```

You will be asked to specify attributes for the service, where each attribute has the following:

- a serviceName
- a serviceType (get, post, delete, put, options)
- for a return Type (Object, String, Integer, Long, Float, Double, Boolean, Date, other)

## Compile and Run

Compile and rerun the service:

``` bash
    $> mvn compile exec:exec
```    
A client-side AngularJS application will now be available by running

``` bash
	$> grunt server
```

The Grunt server will run at [http://localhost:9000](http://localhost:9000).  It will proxy REST requests to the Spark service running at [http://localhost:8080](http://localhost:8080).

At this point you should be able to navigate to a page to manage your persistent entities.  

The Grunt server supports hot reloading of client-side HTML/CSS/Javascript file changes, while the Spark service supports hot reloading of Java class file changes.
