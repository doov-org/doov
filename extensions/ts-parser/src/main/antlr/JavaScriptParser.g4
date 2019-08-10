 /*
  * The MIT License (MIT)
  *
  * Copyright (c) 2014 by Bart Kiers (original author) and Alexandre Vitorelli (contributor -> ported to CSharp)
  * Copyright (c) 2017 by Ivan Kochurkin (Positive Technologies):
     added ECMAScript 6 support, cleared and transformed to the universal grammar.
  *
  * Permission is hereby granted, free of charge, to any person
  * obtaining a copy of this software and associated documentation
  * files (the "Software"), to deal in the Software without
  * restriction, including without limitation the rights to use,
  * copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the
  * Software is furnished to do so, subject to the following
  * conditions:
  *
  * The above copyright notice and this permission notice shall be
  * included in all copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
  * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
  * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
  * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
  * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
  * OTHER DEALINGS IN THE SOFTWARE.
  */
 parser grammar JavaScriptParser;

@header {
package io.doov.tsparser;
}

 options {
     tokenVocab=JavaScriptLexer;
     superClass=JavaScriptBaseParser;
 }

 program
     : sourceElements? EOF
     ;

 sourceElement
     : Export? statement
     ;

 statement
     : block
     | variableStatement
     | emptyStatement
     | expressionStatement
     | ifStatement
     | iterationStatement
     | continueStatement
     | breakStatement
     | returnStatement
     | withStatement
     | labelledStatement
     | switchStatement
     | throwStatement
     | tryStatement
     | debuggerStatement
     //| functionDeclaration
     //| classDeclaration CHANGED declarations excluded
     ;

 block
     : '{' statementList? '}'
     ;

// statementList        CHANGED
//     : statement+
//     ;

 variableStatement
     : varModifier variableDeclarationList eos
     ;

 variableDeclarationList
     : variableDeclaration (',' variableDeclaration)*
     ;

 variableDeclaration
     : (Identifier | arrayLiteral | objectLiteral) ('=' singleExpression)? // ECMAScript 6: Array & Object Matching
     ;

 emptyStatement
     : SemiColon
     ;

 expressionStatement
     : {notOpenBraceAndNotFunction()}? expressionSequence eos
     ;

 ifStatement
     : If '(' expressionSequence ')' statement (Else statement)?
     ;


 iterationStatement
     : Do statement While '(' expressionSequence ')' eos                                                 # DoStatement
     | While '(' expressionSequence ')' statement                                                        # WhileStatement
     | For '(' expressionSequence? ';' expressionSequence? ';' expressionSequence? ')' statement         # ForStatement
     | For '(' varModifier variableDeclarationList ';' expressionSequence? ';' expressionSequence? ')'
           statement                                                                                     # ForVarStatement
     | For '(' singleExpression (In | Identifier{p("of")}?) expressionSequence ')' statement             # ForInStatement
     | For '(' varModifier variableDeclaration (In | Identifier{p("of")}?) expressionSequence ')' statement      # ForVarInStatement
     ;

 varModifier  // let, const - ECMAScript 6
     : Var
     | Let
     | Const
     ;

 continueStatement
     : Continue ({notLineTerminator()}? Identifier)? eos
     ;

 breakStatement
     : Break ({notLineTerminator()}? Identifier)? eos
     ;

 returnStatement
     : Return ({notLineTerminator()}? expressionSequence)? eos
     ;

 withStatement
     : With '(' expressionSequence ')' statement
     ;

 switchStatement
     : Switch '(' expressionSequence ')' caseBlock
     ;

 caseBlock
     : '{' caseClauses? (defaultClause caseClauses?)? '}'
     ;

 caseClauses
     : caseClause+
     ;

 caseClause
     : Case expressionSequence ':' statementList?
     ;

 defaultClause
     : Default ':' statementList?
     ;

 labelledStatement
     : Identifier ':' statement
     ;

 throwStatement
     : Throw {notLineTerminator()}? expressionSequence eos
     ;

 tryStatement
     : Try block (catchProduction finallyProduction? | finallyProduction)
     ;

 catchProduction
     : Catch '(' Identifier ')' block
     ;

 finallyProduction
     : Finally block
     ;

 debuggerStatement
     : Debugger eos
     ;

 functionDeclaration
     : Function Identifier '(' formalParameterList? ')' '{' functionBody '}'
     ;

 classDeclaration
     : Class Identifier classTail
     ;

 classTail
     : classHeritage? '{' classBody '}' //CHANGED: excluded classHeritage into their own rules
     ;

 classHeritage: Extends singleExpression;

 classBody: classElement*;

 classElement
     : Static? methodDefinition
     ;

 methodDefinition
     : propertyName '(' formalParameterList? ')' '{' functionBody '}'
     | getter '(' ')' '{' functionBody '}'
     | setter '(' formalParameterList? ')' '{' functionBody '}'
     | generatorMethod
     ;

 generatorMethod
     : '*'? Identifier '(' formalParameterList? ')' '{' functionBody '}'
     ;

 formalParameterList
     : formalParameterArg (',' formalParameterArg)* (',' lastFormalParameterArg)?
     | lastFormalParameterArg
     | arrayLiteral                            // ECMAScript 6: Parameter Context Matching
     | objectLiteral                           // ECMAScript 6: Parameter Context Matching
     ;

 formalParameterArg
     : Identifier ('=' singleExpression)?      // ECMAScript 6: Initialization
     ;

 lastFormalParameterArg                        // ECMAScript 6: Rest Parameter
     : Ellipsis Identifier
     ;

 functionBody
     : sourceElements?
     ;

 sourceElements
     : sourceElement+
     ;

 arrayLiteral
     : '[' ','* elementList? ','* ']'
     ;

 elementList
     : singleExpression (','+ singleExpression)* (','+ lastElement)?
     | lastElement
     ;

 lastElement                      // ECMAScript 6: Spread Operator
     : Ellipsis Identifier
     ;

 objectLiteral
     : '{' (propertyAssignment (',' propertyAssignment)*)? ','? '}'
     ;

 //this is somewhat different from ES2015 Spec:
 propertyAssignment //propertyAssignment actually called "propertyDefinition" in Spec
     : propertyName (':' |'=') singleExpression       # PropertyExpressionAssignment //original spec: (proName ':' assignmentExpression | coverInitializedName)
     | '[' singleExpression ']' ':' singleExpression  # ComputedPropertyExpressionAssignment
     | getter '(' ')' '{' functionBody '}'            # PropertyGetter    //actually in methodDefinition
     | setter '(' Identifier ')' '{' functionBody '}' # PropertySetter    //actually in methoddefinition
     | generatorMethod                                # MethodProperty    //actually in methoddefinition
     //generator Method = propertyName '(' strictFormalParameters ')' '{' functionBody '}'
     | Identifier                                     # PropertyShorthand //identifierReference
     ;

 propertyName
     : identifierName
     | StringLiteral
     | numericLiteral
     ;

 arguments
     : '(' argumentsList? ')' //CHANGED extracted argumentsList into its own rule
     ;

argumentsList
    : singleExpression (',' singleExpression)* (',' lastArgument)?
    | lastArgument;

 lastArgument                                  // ECMAScript 6: Spread Operator
     : Ellipsis Identifier
     ;

 expressionSequence
     : singleExpression (',' singleExpression)*
     ;

 singleExpression
     : functionExpression                                                     # FunctionSingleExpression //CHANGED extracted to its own rule
     | Class Identifier? classTail                                            # ClassExpression
     | singleExpression '[' expressionSequence ']'                            # MemberIndexExpression
     | singleExpression '.' identifierName                                    # MemberDotExpression
     | callExpression                                                         # CallSingleExpression //CHANGED extracted
     | newExpression                                                          # NewSingleExpression //CHANGED extracted
     | singleExpression {notLineTerminator()}? '++'                           # PostIncrementExpression
     | singleExpression {notLineTerminator()}? '--'                           # PostDecreaseExpression
     | unaryExpression  # UnarySingleExpression
    /*
     | '++' singleExpression                                                  # PreIncrementExpression
     | '--' singleExpression                                                  # PreDecreaseExpression
     | '+' singleExpression                                                   # UnaryPlusExpression
     | '-' singleExpression                                                   # UnaryMinusExpression
     | '~' singleExpression                                                   # BitNotExpression
     | '!' singleExpression                                                   # NotExpression
   */
     | singleExpression ('*' | '/' | '%') singleExpression                    # MultiplicativeExpression
     | singleExpression ('+' | '-') singleExpression                          # AdditiveExpression
     | singleExpression ('<<' | '>>' | '>>>') singleExpression                # BitShiftExpression
     | singleExpression ('<' | '>' | '<=' | '>=') singleExpression            # RelationalSingleExpression
     | singleExpression Instanceof singleExpression                           # InstanceofExpression
     | singleExpression In singleExpression                                   # InExpression
     | singleExpression ('==' | '!=' | '===' | '!==') singleExpression        # EqualityExpression
     | singleExpression '&' singleExpression                                  # BitAndExpression
     | singleExpression '^' singleExpression                                  # BitXOrExpression
     | singleExpression '|' singleExpression                                  # BitOrExpression
     | singleExpression '&&' singleExpression                                 # LogicalAndExpression
     | singleExpression '||' singleExpression                                 # LogicalOrExpression
     | singleExpression '?' singleExpression ':' singleExpression             # TernaryExpression
     | singleExpression '=' singleExpression                                  # AssignmentExpression
     | singleExpression assignmentOperator singleExpression                   # AssignmentOperatorExpression
     | singleExpression TemplateStringLiteral                                 # TemplateStringExpression  // ECMAScript 6
     | This                                                                   # ThisExpression
     | Identifier                                                             # IdentifierExpression
     | Super                                                                  # SuperExpression
     | literal                                                                # LiteralExpression
     | arrayLiteral                                                           # ArrayLiteralExpression
     | objectLiteral                                                          # ObjectLiteralExpression
     | '(' expressionSequence ')'                                             # ParenthesizedExpression
     | arrowFunctionParameters '=>' arrowFunctionBody                         # ArrowFunctionExpression   // ECMAScript 6 //CHANGED extracted to its own rule
     ;

//workaround, necessary for typescript grammar to prevent parser recognizing typearguments "foo<T>()" as relational expression:
 callExpression
    : memberExpression arguments
    | superCall
    | callExpression arguments
    | callExpression '['singleExpression ']'
    | callExpression '.' identifierName
    | callExpression TemplateStringLiteral //actually templateLiteral which has itself some more rules...
    ;

 superCall: 'super' arguments;

 memberExpression
    : primaryExpression
    | memberExpression '[' singleExpression ']' //acutally "expression", not "singleexpression"
    | memberExpression '.' identifierName
    | memberExpression '.' TemplateLiteral
    | superProperty
    | metaProperty
//    | 'new' memberExpression arguments already got that with NewExpression alternative in singleexpression
    ;

 metaProperty: 'new' '.' 'target';
 superProperty: 'super' ('.' identifierName | '[' singleExpression ']'); //acutally "expression", not "singleexpression"

primaryExpression
    : 'this'
    | identifierReference
    | literal
    | arrayLiteral
    | objectLiteral
    | functionExpression
//    | classExpression excluding this for now...
//    | generatorExpression
//    | regularExpressionLiteral
//    | templateLiteral
//    | coverParenthesizedExpressionAndArrowParameterList
    ;
// /end of workaround.


 newExpression: New singleExpression arguments? ;

 unaryExpression
     : Delete singleExpression                                                # DeleteExpression
     | Void singleExpression                                                  # VoidExpression
     | Typeof singleExpression                                                # TypeofExpression
     | '++' singleExpression                                                  # PreIncrementExpression
     | '--' singleExpression                                                  # PreDecreaseExpression
     | '+' singleExpression                                                   # UnaryPlusExpression
     | '-' singleExpression                                                   # UnaryMinusExpression
     | '~' singleExpression                                                   # BitNotExpression
     | '!' singleExpression                                                   # NotExpression
    ;
 functionExpression: Function Identifier? '(' formalParameterList? ')' '{' functionBody '}';

 arrowFunctionParameters
     : Identifier
     | '(' formalParameterList? ')'
     ;

 arrowFunctionBody
     : singleExpression
     | '{' functionBody '}'
     ;

 assignmentOperator
     : '*='
     | '/='
     | '%='
     | '+='
     | '-='
     | '<<='
     | '>>='
     | '>>>='
     | '&='
     | '^='
     | '|='
     ;

 literal
     : NullLiteral
     | BooleanLiteral
     | StringLiteral
     | TemplateStringLiteral
     | RegularExpressionLiteral
     | numericLiteral
     ;

 numericLiteral
     : DecimalLiteral
     | HexIntegerLiteral
     | OctalIntegerLiteral
     | OctalIntegerLiteral2
     | BinaryIntegerLiteral
     ;

 identifierName
     : Identifier
     | reservedWord
     ;

 reservedWord
     : keyword
     | NullLiteral
     | BooleanLiteral
     ;

 keyword
     : Break
     | Do
     | Instanceof
     | Typeof
     | Case
     | Else
     | New
     | Var
     | Catch
     | Finally
     | Return
     | Void
     | Continue
     | For
     | Switch
     | While
     | Debugger
     | Function
     | This
     | With
     | Default
     | If
     | Throw
     | Delete
     | In
     | Try

     | Class
     | Enum
     | Extends
     | Super
     | Const
     | Export
     | Import
     | Implements
     | Let
     | Private
     | Public
     | Interface
     | Package
     | Protected
     | Static
     | Yield
     ;

 getter
     : Identifier{p("get")}? propertyName
     ;

 setter
     : Identifier{p("set")}? propertyName
     ;

 eos
     : SemiColon
     | EOF
     | {lineTerminatorAhead()}?
     | {closeBrace()}?
     ;



///////////////////////////////////////////////////////////
// Added:

// map specification's original terminology to given grammar:
bindingIdentifier: Identifier; //Identifier is like "identifierName but not reservedWord"
formalParameters: formalParameterList;
//

// A.2 Expressions:
initializer: '=' singleExpression; //TODO actually it's: '=' assignmentExpression; but it's not the #AssignmentTexpression -.-
identifierReference: Identifier | Yield;

// ...
//relationalExpression:
//    shi...
//
//equalityExpression
//    : relationalExpression
//    | equalityExpression '==' relationalExpression
//    | equalityExpression '!=' relationalExpression
//    | equalityExpression '===' relationalExpression
//    | equalityExpression '!==' relationalExpression
//    ;
//
//bitwiseANDExpression
//    : equalityExpression
//    | bitwiseANDExpression '&' equalityExpression
//    ;
//
//bitwiseXORExpression
//    : bitwiseANDExpression
//    | bitwiseXORExpression '^' bitwiseANDExpression
//    ;
//
//bitwiseORExpression
//    : bitwiseXORExpression
//    | bitwiseORExpression '|' bitwiseXORExpression
//    ;
//
//logicalANDExpression
//    : bitwiseXORExpression
//    | logicalANDExpression '&&' bitwiseORExpression
//    ;
//
//logicalORExpression
//    : logicalANDExpression
//    | logicalORExpression '||' logicalANDExpression
//    ;
//
//conditionalExpression
//    : logicalORExpression
//    | logicalORExpression '?' assignmentExpression ':' assignmentExpression
//    ;
//
//assignmentExpression
//    : conditionalExpression
//    | yieldExpression
//    | arrowFunction
//    | leftHandSideExpression '=' assignmentExpression
//    | leftHandSideExpression assignmentOperator assignmentExpression
//    ;

//assignmentExpression: singleExpression '=' singleExpression; //TODO not sure this is correct, but better than completing the above commented lines

// A.3 Statements:

letOrConst: Let | Const;

bindingList
    : lexicalBinding
    | bindingList ',' lexicalBinding
    ;

lexicalBinding
    : bindingIdentifier initializer?
    | bindingPattern initializer?
    ;

bindingPattern: objectBindingPattern | arrayBindingPattern;

objectBindingPattern
    : '{' '}'
    | '{' bindingPropertyList '}'
    | '{' bindingPropertyList ',' '}'
    ;

arrayBindingPattern
    : '[' ','* bindingRestElement? ']'
    | '[' bindingElementList ']'
    | '[' bindingElementList ','* bindingRestElement? ']'
    ;

bindingElementList
    : bindingElisionElement
    | bindingElementList ',' bindingElisionElement;

bindingElisionElement: ','* bindingElement;

bindingRestElement: '...' bindingIdentifier;

bindingPropertyList
    : bindingProperty
    | bindingPropertyList ',' bindingProperty
    ;

bindingProperty
    : singleNameBinding
    | propertyName ':' bindingElement
    ;

bindingElement
    : singleNameBinding
    | propertyName ':' bindingElement
    ;

singleNameBinding: bindingIdentifier initializer?;

declaration
    : hoistableDeclaration
    | classDeclaration
    | lexicalDeclaration
    ;

hoistableDeclaration
    : functionDeclaration
    | generatorDeclaration
    ;

lexicalDeclaration: letOrConst bindingList;

statementList: statementListItem+;

statementListItem: statement | declaration;

//A.4 Functions and Classes:
generatorDeclaration
    : 'function' '*' bindingIdentifier '(' formalParameters ')' '{' generatorBody '}'
    | 'function' '*' '(' formalParameters ')' '{' generatorBody '}'
    ;

generatorBody: functionBody;


// A.5 Scripts and Modules

script: scriptBody? ;

scriptBody: statementList;

module: moduleBody? ;

moduleBody: moduleItemList;

moduleItemList: moduleItem+ ;//moduleItem | moduleItemList moduleItem;

moduleItem: importDeclaration | exportDeclaration | statementListItem;

importDeclaration
    : 'import' importClause fromClause ';'
    | 'import' moduleSpecifier ';'
    ;

importClause
 : importedDefaultBinding
 | nameSpaceImport
 | namedImports
 | importedDefaultBinding ',' nameSpaceImport
 | importedDefaultBinding ',' namedImports
 ;

importedDefaultBinding: importedBinding;

nameSpaceImport: '*' 'as' importedBinding;

namedImports: '{' importsList? ','? '}' ;
//    : '{' '}'
//    | '{' importsList '}'
//    | '{' importsList ',' '}'
//    ;

importsList: importSpecifier (',' importSpecifier)*;
//    : importSpecifier
//    | importsList ',' importSpecifier
//    ;

fromClause: 'from' moduleSpecifier;


importSpecifier: (identifierName 'as')? importedBinding;
//    : importedBinding
//    | identifierName 'as' importedBinding
//    ;

moduleSpecifier: StringLiteral;

importedBinding: bindingIdentifier;

exportDeclaration
    : 'export' '*' fromClause ';'
    | 'export' exportClause fromClause ';'
    | 'export' exportClause ';'
    | 'export' variableStatement
    | 'export' declaration
    | 'export' 'default' hoistableDeclaration
    | 'export' 'default' classDeclaration
    // TODO: | 'export' 'default' [lookahead not element { function, class }] AssignmentExpression[In] ';' WHUT?!
    ;

exportClause
    : '{' '}'
    | '{' exportsList '}'
    | '{' exportsList ',' '}'
    ;

exportsList
    : exportSpecifier
    | exportsList ',' exportSpecifier
    ;

exportSpecifier
    : identifierName
    | identifierName 'as' identifierName
    ;