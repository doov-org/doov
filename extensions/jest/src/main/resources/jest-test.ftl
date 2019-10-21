<#list imports as import>
${import.toImportStatement()}
</#list>

<#list fields as field>
${field.toConstDeclaration()}
</#list>

<#list testStates as testState>
${testState}
</#list>

beforeEach(() => {
<#list beforeEachs as beforeEach>
  ${beforeEach}
</#list>
});

describe('${suiteName}', () => {
  <#list tests as test>
  it('${test.description}', () => {
    <#list test.testStates as testState>
    ${testState}
    </#list>
    <#list test.ruleAssertions as ruleAssertion>
    expect(${ruleAssertion.value}).toEqual(${ruleAssertion.expected});
    </#list>
    <#list test.fieldAssertions as fieldAssertion>
    expect(${fieldAssertion.fieldName}.get(model)).toEqual(${fieldAssertion.expected});
    </#list>
  });
  </#list>
});
