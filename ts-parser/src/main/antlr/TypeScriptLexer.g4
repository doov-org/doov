lexer grammar TypeScriptLexer;

import JavaScriptLexer;

options {
    superClass=JavaScriptBaseLexer;
}

//keywords:

Any : 'any';
Number: 'number';
Boolean: 'boolean';
String: 'string';
Symbol: 'symbol';
Void: 'void';

Type: 'type';

Get: 'get ';
Set: 'set ';

Interface: 'interface';
Implements: 'implements';
Constructor: 'constructor';
Static: 'static';
Namespace: 'namespace';
Require: 'require';
Module: 'module';
Declare: 'declare';

Let: 'let';
Const: 'const';

//
// Ext.2 Additions to 1.8: Decorators
//
At: '@';
