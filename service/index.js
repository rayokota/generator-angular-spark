'use strict';
var util = require('util'),
    yeoman = require('yeoman-generator'),
    fs = require('fs'),
    _ = require('lodash'),
    _s = require('underscore.string'),
    pluralize = require('pluralize');

var ServiceGenerator = module.exports = function ServiceGenerator(args, options, config) {
  // By calling `NamedBase` here, we get the argument to the subgenerator call
  // as `this.name`.
  yeoman.generators.NamedBase.apply(this, arguments);

  console.log('You called the service subgenerator with the argument ' + this.name + '.');

  fs.readFile('generator.json', 'utf8', function (err, data) {
    if (err) {
      console.log('Error: ' + err);
      return;
    }
    this.generatorConfig = JSON.parse(data);
  }.bind(this));
};

util.inherits(ServiceGenerator, yeoman.generators.NamedBase);

ServiceGenerator.prototype.askFor = function askFor() {
  var cb = this.async();

  console.log('\nPlease specify an sub-service name:');

  var prompts = [{
    type: 'input',
    name: 'serviceName',
    message: 'What is the name of this sub-service ?',
    default: 'myService'
  },
  {
    type: 'list',
    name: 'serviceType',
    message: 'What is the method of the REST service ?',
    choices: ['get', 'post', 'put', 'delete', 'options', 'trace', 'connect'],
    default: 'get'
  },
  {
    name: 'returnType',
    message: 'What is the type of return for this service ?',
    choices: ['void', 'Object', 'String', 'List', 'Long', 'Float', 'Double', 'Integer', 'Date', 'other'],
    default: 'void'
  },
  {
    when: function (props) { return (/other/).test(props.attrType); },
    type: 'input',
    name: 'returnType',
    message: 'Enter the object for the returned type, or hit enter:',
    validate: function (input) {
      if (input.length<3) {
        return "Please input a valid object type.";
      }
      return 'Object';
    }
  },
  {
    type: 'confirm',
    name: 'again',
    message: 'Would you like to enter another service or reenter a previous service?',
    default: true
  }];

  this.prompt(prompts, function (props) {
    this.attrs = this.attrs || [];
    var attrType = props.attrType;
    this.attrs = _.reject(this.attrs, function (attr) { return attr.attrName === props.attrName; });
    this.attrs.push({ 
      serviceName: props.attrName, 
      serviceType: attrType 
    });

    if (props.again) {
      this.askFor();
    } else {
      cb();
    }
  }.bind(this));
};

ServiceGenerator.prototype.files = function files() {

  this.baseName = this.generatorConfig.baseName;
  this.packageName = this.generatorConfig.packageName;
  this.services = this.generatorConfig.entities;
  this.services = _.reject(this.entities, function (entity) { return entity.name === this.name; }.bind(this));
  this.services.push({ name: this.name, attrs: this.attrs});
  this.resources = this.generatorConfig.resources;
  this.pluralize = pluralize;
  this.generatorConfig.entities = this.entities;
  this.generatorConfigStr = JSON.stringify(this.generatorConfig, null, '\t');

  var packageFolder = this.packageName.replace(/\./g, '/');
  this.template('_generator.json', 'generator.json');

  var javaDir = 'src/main/java/' + packageFolder + '/';
  var servicesDir = javaDir + 'services/';
  var utilDir = javaDir + 'util/';
  var resourceDir = 'src/main/resources/';
  var assetsDir = resourceDir + 'assets/';
  var assetsAppDir = assetsDir + 'app/';
  this.template('../../app/templates/src/main/java/package/_App.java', javaDir + 'App.java');
  this.template('src/main/java/package/services/_Service.java', servicesDir + _s.capitalize(this.name) + '.java');

  }.bind(this));
};
