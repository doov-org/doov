# Live code plan

## New field

- show SampleModel
- add company field (with company field id key)
- add i18n in SampleModelResourceBundle
- add company to rule in RulesOld
- generate code with `./gradlew -p sample :doov-sample-base:build :doov-sample-generated:build`
- show field accountCompany in DslSampleModel

## New rule

- rewrite RuleOld with DSL
- write a new test
- review SampleModels.sample()
- add default value for company
- add asserThat(result).isTrue()
- add sysout(rule.readable())
- run
- add sysout(rule.markdown(Locale.FR))
- run

## Test failure cause 1 (phone number)

- duplicate the test
- add wrong phone number
- run
- review the test failure trace
- change assert and add assertFailureCause
- run

## Test failure cause 2 (company)

- duplicate the test
- add wrong company
- run
- review the test failure trace
- add short circuit
- run
- review the test failure trace
- change assert and add assertFailureCause
- run

