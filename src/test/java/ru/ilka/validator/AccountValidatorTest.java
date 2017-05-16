package ru.ilka.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * Here could be your advertisement +375 29 3880490
 */
@RunWith(Parameterized.class)
public class AccountValidatorTest {

    private enum TestTarget {
        LOGIN(AccountValidator::validateLogin),
        EMAIL(AccountValidator::validateEmail),
        PASSWORD(AccountValidator::validatePassword),
        GENDER(AccountValidator::validateGender),
        INVITE_CODE(AccountValidator::validateInviteCode);

        private Predicate<String> test;

        TestTarget(Predicate<String> test) {
            this.test = test;
        }
    }

    private String testString;
    private TestTarget target;
    private boolean expectedResult;

    public AccountValidatorTest(String testString, TestTarget target, boolean expectedResult) {
        this.testString = testString;
        this.target = target;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: {1} - {0} - {2}")
    public static Collection testData() {
        return Arrays.asList(new Object[][] {
                {"русский_логин",TestTarget.LOGIN, false},
                {"qwe",TestTarget.LOGIN, false},
                {"",TestTarget.LOGIN, false},
                {"-Ilka",TestTarget.LOGIN, false},
                {"_Ilka",TestTarget.LOGIN, true},
                {"06_06_1996",TestTarget.LOGIN, true},

                {"русский@мфил.ру",TestTarget.EMAIL, false},
                {"русский@asd.ru",TestTarget.EMAIL, false},
                {"werASAA.saqw.qw.",TestTarget.EMAIL, false},
                {"@mail.ru",TestTarget.EMAIL, false},
                {"norm@mail.NeNormTutBy",TestTarget.EMAIL, false},
                {"norm@HBHBHBHBK-wff.ru",TestTarget.EMAIL, false},
                {"st.ilka@mail.ru",TestTarget.EMAIL, true},
                {"Blinov.I@mail.ru",TestTarget.EMAIL, true},
                {"2342-qwq@aadsas.qw",TestTarget.EMAIL, true},

                {"",TestTarget.PASSWORD,false},
                {"рсский121УВУЦВ",TestTarget.PASSWORD,false},
                {"NoNumbers",TestTarget.PASSWORD,false},
                {"nouppercase",TestTarget.PASSWORD,false},
                {"-@233@#2e24564;SWWqweq12",TestTarget.PASSWORD,false},
                {"Qwe1",TestTarget.PASSWORD,false},
                {"Qwe76771 qwqeQSQ1",TestTarget.PASSWORD,false},
                {"Qwerty123",TestTarget.PASSWORD,true},

                {"",TestTarget.GENDER,false},
                {"Мужик",TestTarget.GENDER,false},
                {"Мужчина",TestTarget.GENDER,true},
                {"йццуййц",TestTarget.GENDER,false},
                {"Женщина",TestTarget.GENDER,true},
                {"Male",TestTarget.GENDER,true},
                {"Female",TestTarget.GENDER,true},

                {"",TestTarget.INVITE_CODE, false},
                {"qwew-1312",TestTarget.INVITE_CODE, false},
                {"QWEQQWEQ",TestTarget.INVITE_CODE, false},
                {"1234-1234",TestTarget.INVITE_CODE, true},
                {"@##@-AAWW",TestTarget.INVITE_CODE, false},
                {"РУСС-1234",TestTarget.INVITE_CODE, false},
                {"ASDF-AS12",TestTarget.INVITE_CODE, true}
        });
    }

    @Test
    public void validateInput(){
        Assert.assertEquals(expectedResult,target.test.test(testString));
    }
}
