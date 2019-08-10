parser grammar TypeScriptParser;

import JavaScriptParser;

options {
     tokenVocab=TypeScriptLexer;
     superClass=JavaScriptBaseParser;
 }

//
// A.1 Types
//

// - typeParams:
typeParameters: '<' typeParameterList '>';

typeParameterList: typeParameter (',' typeParameter)*;

typeParameter: bindingIdentifier constraint?;

constraint: 'extends' type;


// - typeArguments: (like typeParams but not allowing constraint)

typeArguments: '<' typeArgumentList '>' ;

typeArgumentList: typeArgument (',' typeArgument)* ;

typeArgument: type;


// - Types:
type
    : unionOrIntersectionOrPrimaryType
    | functionType
    | constructorType
    ;

unionOrIntersectionOrPrimaryType
    : unionOrIntersectionOrPrimaryType '|' unionOrIntersectionOrPrimaryType #Union
    | unionOrIntersectionOrPrimaryType '&' unionOrIntersectionOrPrimaryType #Intersection
    | primaryType #Primary
    ;

primaryType
    : '(' type ')'                                  #ParenthesizedPrimType
    | predefinedType                                #PredefinedPrimType
    | typeReference                                 #ReferencePrimType
    | objectType                                    #ObjectPrimType
    | primaryType {notLineTerminator()}? '[' ']'    #ArrayPrimType
    | '[' tupleElementTypes ']'                     #TuplePrimType
    | typeQuery                                     #QueryPrimType
    | thisType                                      #ThisPrimType
    ;

// -- primary Types: paranthesized and predefined

//parenthesizedType: '(' type ')';

predefinedType
    : 'any'
    | 'number'
    | 'boolean'
    | 'string'
    | 'symbol'
    | 'void'
    ;

// -- primary Types: typeReference

typeReference: typeName ({notLineTerminator()}? typeArguments)? ;

typeName
    : identifierReference
    | namespaceName '.' identifierReference
    ;

//typeName: (namespaceName '.')? identifierReference;
//
namespaceName: (identifierReference '.')* identifierReference;




// -- primary Types: objectType

objectType: '{' typeBody? '}';

typeBody: typeMemberList (';' | ',')? ;

typeMemberList: typeMember ((';' | ',') typeMember)* ;

typeMember
    : propertySignatur
    | callSignature
    | constructSignature
    | indexSignature
    | methodSignature
    ;

// -- primary Types: array- and tupleTypes

//arrayType: primaryType {notLineTerminator()}? '[' ']';

//tupleType: '[' tupleElementTypes ']';

tupleElementTypes: tupleElementType (',' tupleElementType)* ;

tupleElementType: type;

// -- union/intersection types + function/constructor types:
// (not sure why they are sorted here in the spec, in the middle of primary Types)

//unionType: unionOrIntersectionOrPrimaryType '|' intersectionOrPrimaryType;

//intersectionType: intersectionOrPrimaryType '&' primaryType;

functionType: typeParameters? '(' parameterList? ')' '=>' type;

constructorType: 'new' typeParameters? '(' parameterList? ')' '=>' type;

// -- primary Types: typeQuery and thisType

typeQuery: 'typeof' typeQueryExpression;

typeQueryExpression
    : identifierReference
    | (identifierName '.')+ identifierName //idName includes idReference
    ;
//original:
//typeQueryExpression
//    : identifierReference        -can end with idRef
//    | typeQueryExpression '.' identifierName -or can end with idName, but then needs at least one ((idRef|idName) .) as prefix
//    ;

thisType: 'this';

// -- typeMembers:
// -- typeMembers: propertySignature

propertySignatur: propertyName '?'? typeAnnotation?;

typeAnnotation: ':' type;

// -- typeMembers: callSignature

callSignature: typeParameters? '(' parameterList? ')' typeAnnotation? ;

parameterList
    : requiredParameterList
    | optionalParameterList
    | restParameter
    | requiredParameterList ',' optionalParameterList
    | requiredParameterList ',' restParameter
    | optionalParameterList ',' restParameter
    | requiredParameterList ',' optionalParameterList ',' restParameter
    ;

requiredParameterList: requiredParameter (',' requiredParameter)* ;

requiredParameter
    : decoratorList? accessibilityModifier? bindingIdentifierOrPattern typeAnnotation? //added decoratorList for Ext.2
    | decoratorList? bindingIdentifier ':' StringLiteral //added decoratorList for Ext.2
    ;

accessibilityModifier
    : Public
    | Private
    | Protected
    ;

bindingIdentifierOrPattern
    : bindingIdentifier
    | bindingPattern
    ;

optionalParameterList: optionalParameter (',' optionalParameter)* ;

optionalParameter
    : decoratorList? accessibilityModifier? bindingIdentifierOrPattern '?' typeAnnotation? //added decoratorList for Ext.2
    | decoratorList? accessibilityModifier? bindingIdentifierOrPattern typeAnnotation? initializer //added decoratorList for Ext.2
    | decoratorList? bindingIdentifier '?' ':' StringLiteral //added decoratorList for Ext.2
    ;

restParameter: '...' bindingIdentifier typeAnnotation? ;

// -- typeMembers: constructSignature, indexSignature, methodSignature:

constructSignature: 'new' typeParameters? '(' parameterList? ')' typeAnnotation? ;

indexSignature: '[' bindingIdentifier ':' ('string' | 'number') ']' typeAnnotation;

methodSignature: propertyName '?'? callSignature;

// - typeAliasDeclaration (not used yet, but will be in A.3)

typeAliasDeclaration: 'type' bindingIdentifier typeParameters? '=' type ';' ;


//
// A.2 Expressions
//

//Actually called "propertyDefinition" in ES2015 spec, but the imported javascript grammar names it differently:
propertyAssignment //modified
     : identifierReference                            # PropertyShorthand //identifierReference
     | propertyName '=' singleExpression              # CoverInitializedName
     | propertyName ':' singleExpression              # PropertyExpressionAssignment
     | propertyName callSignature '{' functionBody '}'# MethodProperty
     | getAccessor                                    # PropertyGetter
     | setAccessor                                    # PropertySetter
     ;

getAccessor: 'get ' propertyName '(' ')' typeAnnotation? '{' functionBody '}';
setAccessor: 'set ' propertyName '(' bindingIdentifierOrPattern typeAnnotation? ')' '{' functionBody '}';

// Overrides JavaScript Rule:
//     propertyAssignment //propertyAssignment actually called "propertyDefinition" in Spec
//         : propertyName (':' |'=') singleExpression       # PropertyExpressionAssignment //original spec: (proName ':' assignmentExpression | coverInitializedName)
//         | '[' singleExpression ']' ':' singleExpression  # ComputedPropertyExpressionAssignment
//         | getter '(' ')' '{' functionBody '}'            # PropertyGetter    //actually in methodDefinition
//         | setter '(' Identifier ')' '{' functionBody '}' # PropertySetter    //actually in methoddefinition
//         | generatorMethod                                # MethodProperty    //actually in methoddefinition
//            //propertyName '(' strictFormalParameters ')' '{' functionBody '}' missing
//         | Identifier                                     # PropertyShorthand //identifierReference
//         ;


functionExpression: 'function' bindingIdentifier? callSignature '{' functionBody '}';

// Overrides JavaScript Rule:
// functionExpression: Function Identifier? '(' formalParameterList? ')' '{' functionBody '}';


arrowFunctionParameters
     : Identifier
     | callSignature
     ;
// Overrides JavaScript Rule:
//arrowFunctionParameters
//     : Identifier
//     | '(' formalParameterList? ')'
//     ;


 arguments
     : typeArguments? '(' argumentsList?')'
     ;

// Overrides JavaScript Rule:
//arguments
// : '('(
//       singleExpression (',' singleExpression)* (',' lastArgument)?
//       | lastArgument
//    )?')'
// ;

unaryExpression
     : Delete singleExpression          # DeleteExpression
     | Void singleExpression            # VoidExpression
     | Typeof singleExpression          # TypeofExpression
     | '++' singleExpression            # PreIncrementExpression
     | '--' singleExpression            # PreDecreaseExpression
     | '+' singleExpression             # UnaryPlusExpression
     | '-' singleExpression             # UnaryMinusExpression
     | '~' singleExpression             # BitNotExpression
     | '!' singleExpression             # NotExpression
     | '<' type '>' singleExpression    # TypeCastExpression //Added to original expression //note: typescript spec says ('<' type '>' unaryExpression), but imported JavaScript grammar handles this different.
    ;

//
// A.3 Statements
//

declaration
    : hoistableDeclaration
    | classDeclaration
    | lexicalDeclaration
    | interfaceDeclaration // Added to original expression TODO unit test
    | typeAliasDeclaration // Added to original expression
    | enumDeclaration // Added to original expression TODO unit test
    ;

variableDeclaration
    : simpleVariableDeclaration
    | destructuringVariableDeclaration
    ;

simpleVariableDeclaration: bindingIdentifier typeAnnotation? initializer? ;

destructuringVariableDeclaration: bindingPattern typeAnnotation? initializer ; //initializer not optional!

// Overrides JavaScript Rule:
// variableDeclaration
//     : (Identifier | arrayLiteral | objectLiteral) ('=' singleExpression)? // ECMAScript 6: Array & Object Matching
//     ;

lexicalBinding //TODO unit test / not sure how this rule can ever be reached from scriptbody?!
    : simpleLexicalBinding
    | destructuringLexicalBinding
    ;

simpleLexicalBinding: bindingIdentifier typeAnnotation? initializer? ;

destructuringLexicalBinding: bindingPattern typeAnnotation? initializer? ; //initializer IS optional!

// Overrides JavaScript Rule:
//lexicalBinding
//    : bindingIdentifier initializer?
//    | bindingPattern initializer?
//    ;


//
// A.4 Functions
//

functionDeclaration
    : 'function' bindingIdentifier? callSignature '{' functionBody '}'
    | 'function' bindingIdentifier? callSignature ';'
    ;

// Overrides JavaScript Rule:
//functionDeclaration: Function Identifier '(' formalParameterList? ')' '{' functionBody '}' ;



//
// A.5 Interfaces
//

interfaceDeclaration: 'interface' bindingIdentifier typeParameters? interfaceExtendsClause? objectType;

interfaceExtendsClause: 'extends' classOrInterfaceTypeList;

classOrInterfaceTypeList: classOrInterfaceType (',' classOrInterfaceType)* ;

classOrInterfaceType: typeReference;


//
// A.6 Classes TODO unit tests
//

// - Class Declaration (the 'head' / 'outer frame' of the class)

//classDeclaration: 'class' bindingIdentifier? typeParameters? classHeritage '{' classBody '}';
//decoratorList added for "Ext.2 Decorators"
classDeclaration: decoratorList? 'class' bindingIdentifier? typeParameters? classHeritage '{' classBody '}';

// Overrides JavaScript Rule:
//classDeclaration
//     : Class Identifier classTail
//     ;

classHeritage: classExtendsClause? implementsClause?;
// Overrides JavaScript Rule:
//classHeritage: Extends singleExpression;

classExtendsClause: 'extends' classType;

classType: typeReference;

implementsClause: 'implements' classOrInterfaceTypeList;

// - Class Elements (the elements inside the body of a class)

classElement
    : constructorDeclaration
    | propertyMemberDeclaration
    | indexMemberDeclaration
    ;
// Overrides JavaScript Rule:
// classElement
//     : Static? methodDefinition
//     ;

// - Class Elements - constructors:
constructorDeclaration : accessibilityModifier? 'constructor' '(' parameterList? ')' ('{' functionBody '}')? ;

// - Class Elements - propertiy members:
propertyMemberDeclaration
    : memberVariableDeclaration
    | memberFunctionDeclaration
    | memberAccessorDeclaration
    ;

memberVariableDeclaration: accessibilityModifier? 'static'? propertyName typeAnnotation? initializer? ';';

memberFunctionDeclaration: accessibilityModifier? propertyName callSignature ('{' functionBody '}')? ;

memberAccessorDeclaration: accessibilityModifier? 'static'? (getAccessor | setAccessor);

// - Class Elements - index members:

indexMemberDeclaration: indexSignature ';';




//
// A.7 Enums TODO unit tests
//

enumDeclaration: 'const'? 'enum' bindingIdentifier '{' enumBody? '}';

enumBody: enumMember (',' enumMember)* ','? ;

enumMember: propertyName ('=' enumValue)? ;

enumValue: '=' singleExpression; //actually " '=' assignmentExpression "


//
// A.8 Namespaces TODO unit tests
//

namespaceDeclaration: 'namespace' identifierPath '{' namespaceBody '}';

identifierPath: (bindingIdentifier '.')* bindingIdentifier;

namespaceBody: namespaceElements? ;

namespaceElements: namespaceElement+ ;

namespaceElement
    : statement
    | lexicalDeclaration
    | functionDeclaration
    | generatorDeclaration
    | classDeclaration
    | interfaceDeclaration
    | typeAliasDeclaration
    | enumDeclaration
    | namespaceDeclaration
    //TODO | ambientDeclaration
    | importAliasDeclaration
    | exportNamespaceElement
    ;

exportNamespaceElement
    : 'export' variableStatement
    | 'export' lexicalDeclaration
    | 'export' functionDeclaration
    | 'export' generatorDeclaration
    | 'export' classDeclaration
    | 'export' interfaceDeclaration
    | 'export' typeAliasDeclaration
    | 'export' enumDeclaration
    | 'export' namespaceDeclaration
    //TODO | 'export' ambientDeclaration
    | 'export' importAliasDeclaration
    ;

importAliasDeclaration: 'import' bindingIdentifier '=' entityName ';' ;

entityName : namespaceName ('.' identifierReference)?;


//
// A.9 Scripts and Modules TODO unit tests
//

// - Source File Types:

sourceFile: implementationSourceFile | declarationSourceFile;

implementationSourceFile: implementationScript | implementationModule;

declarationSourceFile: declarationScript | declarationModule;

// - implementation scripts:

implementationScript: implementationScriptElement* ;

implementationScriptElement
    : implementationElement
    //TODO | ambientModuleDeclaration
    ;


implementationElement
    : /*statement -> pushing this to bottom as workaround
    | */lexicalDeclaration
    | functionDeclaration
    | generatorDeclaration
    | classDeclaration
    | interfaceDeclaration
    | typeAliasDeclaration
    | enumDeclaration
    | namespaceDeclaration
    //TODO| ambientDeclaration
    | importAliasDeclaration
    | statement //see comment on above first alternative -^
    ;

// - declaration scripts:

declarationScript: declarationScriptElement* ;

declarationScriptElement
    : declarationElement
    //TODO| ambientModuleDeclaration
    ;

declarationElement
    : interfaceDeclaration
    | typeAliasDeclaration
    | namespaceDeclaration
    //TODO | ambientDeclaration
    | importAliasDeclaration
    ;

// - implementation module:

implementationModule: implementationModuleElement* ;

implementationModuleElement
    : implementationElement
    | importDeclaration
    | importAliasDeclaration
    | importRequireDeclaration
    | exportImplementationElement
    | exportDefaultImplementationElement
    | exportListDeclaration
    | exportAssignment
    ;

// - implementation module:

declarationModule: declarationModuleElement* ;

declarationModuleElement
    : declarationElement
    | importDeclaration
    | importAliasDeclaration
    | exportDeclarationElement
    | exportDefaultDeclarationElement
    | exportListDeclaration
    | exportAssignment
    ;

// - details of alternatives for  implementation / declarationModuleElement

importRequireDeclaration: 'import' bindingIdentifier '=' 'require' '(' StringLiteral ')' ';' ;

exportImplementationElement
    : 'export' variableStatement
    | 'export' lexicalDeclaration
    | 'export' functionDeclaration
    | 'export' generatorDeclaration
    | decoratorList? 'export' classDeclaration // decoratorList added for Ext.2
    | 'export' interfaceDeclaration
    | 'export' typeAliasDeclaration
    | 'export' enumDeclaration
    | 'export' namespaceDeclaration
    //TODO | 'export' ambientDeclaration
    | 'export' importAliasDeclaration
    ;

exportDeclarationElement
    : 'export' interfaceDeclaration
    | 'export' typeAliasDeclaration
    //TODO| 'export' ambientDeclaration
    | 'export' importAliasDeclaration
    ;

exportDefaultImplementationElement
    : 'export' 'default' functionDeclaration
    | 'export' 'default' generatorDeclaration
    | 'export' 'default' classDeclaration
    | 'export' 'default' '=' singleExpression ';' //actually: assignmentexpression
    ;

exportDefaultDeclarationElement
    ://TODO 'export' 'default' ambientFunctionDeclaration
    //TODO| 'export' 'default' ambientClassDeclaration
    /*TODO|*/ 'export' 'default' identifierReference ';'
    ;

exportListDeclaration
    : 'export' '*' fromClause ';'
    | 'export' exportClause fromClause ';'
    | 'export' exportClause ';'
    ;

exportAssignment: 'export' '=' identifierReference ';' ;


//
// A.10 Ambients TODO unit tests
//

ambientDeclaration
	: 'declare' ambientVariableDeclaration
    | 'declare' ambientFunctionDeclaration
    | 'declare' ambientClassDeclaration
    | 'declare' ambientEnumDeclaration
    | 'declare' ambientNamespaceDeclaration
    ;

ambientVariableDeclaration
	: 'var' ambientBindingList ';'
    | 'let' ambientBindingList ';'
    | 'const' ambientBindingList ';'
    ;

ambientBindingList: ambientBinding+ ;

ambientBinding : bindingIdentifier typeAnnotation? ;

ambientFunctionDeclaration : 'function' bindingIdentifier callSignature ';' ;

ambientClassDeclaration : 'class' bindingIdentifier typeParameters? classHeritage '{' ambientClassBody '}';

ambientClassBody: ambientClassBodyElement* ;

ambientClassBodyElement
	: ambientConstructorDeclaration
    | ambientPropertyMemberDeclaration
    | indexSignature
    ;

ambientConstructorDeclaration: 'constructor' '(' parameterList? ')' ';' ;

ambientPropertyMemberDeclaration: accessibilityModifier? 'static'? propertyName (typeAnnotation | callSignature)? ';' ;

ambientEnumDeclaration: enumDeclaration;

ambientNamespaceDeclaration: 'namespace' identifierPath '{' ambientNamespaceBody '}' ;

ambientNamespaceBody: ambientNamespaceElement* ;

ambientNamespaceElement
    : 'export'? ambientVariableDeclaration
    //| 'export'? ambientLexicalDeclaration - see https://github.com/Microsoft/TypeScript/issues/14534
    | 'export'? ambientFunctionDeclaration
    | 'export'? ambientClassDeclaration
    | 'export'? interfaceDeclaration
    | 'export'? ambientEnumDeclaration
    | 'export'? ambientNamespaceDeclaration
    | 'export'? importAliasDeclaration
    ;

ambientModuleDeclaration: 'declare' 'module' StringLiteral '{' declarationModule '}' ;


//
// Ext.1 Workarounds caused by non-standard JavaScript Grammar:
//
/* The following additions are not part of the TypeScript 1.8 spec,
 * but I needed to add them to make the parser work as expected.
 */

//Problem: in "new Foo<T1>(...);" the '<' is interpreted as relational operator. Actually it should be the typeArguments of the 'arguments' rule.
newExpression
    : 'new' identifierReference arguments?
    | 'new' singleExpression arguments?;

//Problem: in "foo<T1>(...);" the '<' is interpreted as relational operator. Actually it should be the typeArguments of the 'arguments' rule.


//
// Ext.2 Additions to 1.8: Decorators
//
/* https://github.com/Microsoft/TypeScript-Handbook/blob/master/pages/Decorators.md:
 * "A Decorator is a special kind of declaration that can be attached to a
 * class declaration, method, accessor, property, or parameter."
 */
/* First try: using the grammar of the official ecmascript proposal: https://tc39.github.io/proposal-decorators/  */

decoratorList: decorator+ ; //TODO unit test

decorator: '@' (decoratorMemberExpression | decoratorCallExpression);

decoratorMemberExpression
    : identifierReference
    | decoratorMemberExpression '.' identifierName
    | '(' singleExpression ')' //actually: 'expression', not singleexpression
    ;

decoratorCallExpression: decoratorMemberExpression arguments;












