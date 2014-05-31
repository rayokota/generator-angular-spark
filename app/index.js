'use strict';
var util = require('util'),
    path = require('path'),
    yeoman = require('yeoman-generator'),
    _ = require('lodash'),
    _s = require('underscore.string'),
    pluralize = require('pluralize'),
    asciify = require('asciify');

var AngularSparkGenerator = module.exports = function AngularSparkGenerator(args, options, config) {
  yeoman.generators.Base.apply(this, arguments);

  this.on('end', function () {
    this.installDependencies({ skipInstall: options['skip-install'] });
  });

  this.pkg = JSON.parse(this.readFileAsString(path.join(__dirname, '../package.json')));
};

util.inherits(AngularSparkGenerator, yeoman.generators.Base);

AngularSparkGenerator.prototype.askFor = function askFor() {

  var cb = this.async();

  console.log('\n' +
    '+-+-+-+-+-+-+-+ +-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+\n' +
    '|a|n|g|u|l|a|r| |s|p|a|r|k| |g|e|n|e|r|a|t|o|r|\n' +
    '+-+-+-+-+-+-+-+ +-+-+-+-+-+ +-+-+-+-+-+-+-+-+-+\n' +
    '\n');

  var prompts = [{
    type: 'input',
    name: 'baseName',
    message: 'What is the base name of your application?',
    default: 'myapp'
  },
  {
    type: 'input',
    name: 'packageName',
    message: 'What is your default package name?',
    default: 'com.mycompany.myapp'
  }];

  this.prompt(prompts, function (props) {
    this.baseName = props.baseName;
    this.packageName = props.packageName;

    cb();
  }.bind(this));
};

AngularSparkGenerator.prototype.app = function app() {

  this.entities = [];
  this.resources = [];
  this.generatorConfig = {
    "baseName": this.baseName,
    "packageName": this.packageName,
    "entities": this.entities,
    "resources": this.resources
  };
  this.generatorConfigStr = JSON.stringify(this.generatorConfig, null, '\t');

  this.template('_generator.json', 'generator.json');
  this.template('_package.json', 'package.json');
  this.template('_bower.json', 'bower.json');
  this.template('bowerrc', '.bowerrc');
  this.template('Gruntfile.js', 'Gruntfile.js');
  this.copy('gitignore', '.gitignore');

  var packageFolder = this.packageName.replace(/\./g, '/');
  this.template('_pom.xml', 'pom.xml');

  var javaDir = 'src/main/java/' + packageFolder + '/';
  var modelsDir = javaDir + 'models/';
  var utilDir = javaDir + 'util/';
  this.mkdir(javaDir);
  this.mkdir(modelsDir);
  this.mkdir(utilDir);
  this.template('_pom.xml', 'pom.xml');
  this.copy('spring_loaded/springloaded-1.2.0.RELEASE.jar', 'spring_loaded/springloaded-1.2.0.RELEASE.jar');
  this.template('src/main/java/package/_App.java', javaDir + 'App.java');
  this.template('src/main/java/package/util/_HibernateUtil.java', utilDir + 'HibernateUtil.java');
  this.template('src/main/java/package/util/_JsonTransformer.java', utilDir + 'JsonTransformer.java');
  this.template('src/main/java/package/util/_JacksonUtil.java', utilDir + 'JacksonUtil.java');

  var resourceDir = 'src/main/resources/';
  this.mkdir(resourceDir);
  this.template('src/main/resources/_hibernate-local.cfg.xml', resourceDir + 'hibernate-local.cfg.xml');
  this.copy('src/main/resources/log4j.properties', resourceDir + 'log4j.properties');

  var publicDir = 'public/'
  this.mkdir(publicDir);

  var publicCssDir = publicDir + 'css/';
  var publicJsDir = publicDir + 'js/';
  var publicViewDir = publicDir + 'views/';
  this.mkdir(publicCssDir);
  this.mkdir(publicJsDir);
  this.mkdir(publicViewDir);
  this.template('public/_index.html', publicDir + 'index.html');
  this.copy('public/css/app.css', publicCssDir + 'app.css');
  this.template('public/js/_app.js', publicJsDir + 'app.js');
  this.template('public/js/home/_home-controller.js', publicJsDir + 'home/home-controller.js');
  this.template('public/views/home/_home.html', publicViewDir + 'home/home.html');
};

AngularSparkGenerator.prototype.projectfiles = function projectfiles() {
  this.copy('editorconfig', '.editorconfig');
  this.copy('jshintrc', '.jshintrc');
};
