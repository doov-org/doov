package org.modelmap.sample2.field;

import org.modelmap.core.PathConstraint;

public enum Sample2Constraint implements PathConstraint {
    NONE(""), //
    ACCOUNT_1("getAccount1()"), //
    ACCOUNT_2("getAccount2()"), //
    ACCOUNT_3("getAccount3()"), //
    ACCOUNT_4("getAccount4()"), //
    ACCOUNT_5("getAccount5()"), //
    ACCOUNT_6("getAccount6()"), //
    ACCOUNT_7("getAccount7()"), //
    ACCOUNT_8("getAccount8()"), //
    ACCOUNT_9("getAccount9()"), //
    ACCOUNT_10("getAccount10()"), //
    ACCOUNT_11("getAccount11()"), //
    ACCOUNT_12("getAccount12()"), //
    ACCOUNT_13("getAccount13()"), //
    ACCOUNT_14("getAccount14()"), //
    ACCOUNT_15("getAccount15()"), //
    ACCOUNT_16("getAccount16()"), //
    ACCOUNT_17("getAccount17()"), //
    ACCOUNT_18("getAccount18()"), //
    ACCOUNT_19("getAccount19()"), //
    ACCOUNT_20("getAccount20()"), //
    ACCOUNT_21("getAccount21()"), //
    ACCOUNT_22("getAccount22()"), //
    ACCOUNT_23("getAccount23()"), //
    ACCOUNT_24("getAccount24()"), //
    ACCOUNT_25("getAccount25()"), //
    ACCOUNT_26("getAccount26()"), //
    ACCOUNT_27("getAccount27()"), //
    ACCOUNT_28("getAccount28()"), //
    ACCOUNT_29("getAccount29()"), //
    ACCOUNT_30("getAccount30()"), //
    ACCOUNT_31("getAccount31()"), //
    ACCOUNT_32("getAccount32()"), //
    ACCOUNT_33("getAccount33()"), //
    ACCOUNT_34("getAccount34()"), //
    ACCOUNT_35("getAccount35()"), //
    ACCOUNT_36("getAccount36()"), //
    ACCOUNT_37("getAccount37()"), //
    ACCOUNT_38("getAccount38()"), //
    ACCOUNT_39("getAccount39()"), //
    ACCOUNT_40("getAccount40()"), //
    ACCOUNT_41("getAccount41()"), //
    ACCOUNT_42("getAccount42()"), //
    ACCOUNT_43("getAccount43()"), //
    ACCOUNT_44("getAccount44()"), //
    ACCOUNT_45("getAccount45()"), //
    ACCOUNT_46("getAccount46()"), //
    ACCOUNT_47("getAccount47()"), //
    ACCOUNT_48("getAccount48()"), //
    ACCOUNT_49("getAccount49()"), //
    ACCOUNT_50("getAccount50()"), //
    ACCOUNT_51("getAccount51()"), //
    ACCOUNT_52("getAccount52()"), //
    ACCOUNT_53("getAccount53()"), //
    ACCOUNT_54("getAccount54()"), //
    ACCOUNT_55("getAccount55()"), //
    ACCOUNT_56("getAccount56()"), //
    ACCOUNT_57("getAccount57()"), //
    ACCOUNT_58("getAccount58()"), //
    ACCOUNT_59("getAccount59()"), //
    ACCOUNT_60("getAccount60()"), //
    ACCOUNT_61("getAccount61()"), //
    ACCOUNT_62("getAccount62()"), //
    ACCOUNT_63("getAccount63()"), //
    ACCOUNT_64("getAccount64()"), //
    ACCOUNT_65("getAccount65()"), //
    ACCOUNT_66("getAccount66()"), //
    ACCOUNT_67("getAccount67()"), //
    ACCOUNT_68("getAccount68()"), //
    ACCOUNT_69("getAccount69()"), //
    ACCOUNT_70("getAccount70()"), //
    ACCOUNT_71("getAccount71()"), //
    ACCOUNT_72("getAccount72()"), //
    ACCOUNT_73("getAccount73()"), //
    ACCOUNT_74("getAccount74()"), //
    ACCOUNT_75("getAccount75()"), //
    ACCOUNT_76("getAccount76()"), //
    ACCOUNT_77("getAccount77()"), //
    ACCOUNT_78("getAccount78()"), //
    ACCOUNT_79("getAccount79()"), //
    ACCOUNT_80("getAccount80()"), //
    ACCOUNT_81("getAccount81()"), //
    ACCOUNT_82("getAccount82()"), //
    ACCOUNT_83("getAccount83()"), //
    ACCOUNT_84("getAccount84()"), //
    ACCOUNT_85("getAccount85()"), //
    ACCOUNT_86("getAccount86()"), //
    ACCOUNT_87("getAccount87()"), //
    ACCOUNT_88("getAccount88()"), //
    ACCOUNT_89("getAccount89()"), //
    ACCOUNT_90("getAccount90()"), //
    ACCOUNT_91("getAccount91()"), //
    ACCOUNT_92("getAccount92()"), //
    ACCOUNT_93("getAccount93()"), //
    ACCOUNT_94("getAccount94()"), //
    ACCOUNT_95("getAccount95()"), //
    ACCOUNT_96("getAccount96()"), //
    ACCOUNT_97("getAccount97()"), //
    ACCOUNT_98("getAccount98()"), //
    ACCOUNT_99("getAccount99()"), //
    ACCOUNT_100("getAccount100()"), //
    ;

    private final String includePath;

    Sample2Constraint(String includePath) {
        this.includePath = includePath;
    }

    public String includePath() {
        return includePath;
    }
}
