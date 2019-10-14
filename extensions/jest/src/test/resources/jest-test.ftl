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
    const rule = ${test.ruleAssertion.rule};
    const result = rule.execute(model);
    expect(result.value).toEqual(${test.ruleAssertion.expected});
    <#list test.fieldAssertions as fieldAssertion>
    expect(${fieldAssertion.fieldName}.get(model)).toEqual(${fieldAssertion.expected});
    </#list>
  });
  </#list>
});
